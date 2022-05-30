package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.ast.types.EBinOp
import tsql.ast.types.EType
import tsql.ast.types.JoinType
import tsql.database.Row
import tsql.database.Table
import tsql.error.SemanticError
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import kotlin.math.max
import kotlin.math.min
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class JoinAST(
    // override val position: Pair<Pair<Int, Int>, Pair<Int, Int>>,
    val joinType: JoinType,
    val left: DataSourceI,
    val right: DataSourceI,
    val leftAttributeAST: AttributeAST,
    val rightAttributeAST: AttributeAST

) : AstNode, Visitable(), DataSourceI {
    override val id: NodeId = AstNode.getId()
    val maxTime = Long.MAX_VALUE
    val minTime = Long.MIN_VALUE
    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        scope: SymbolTableInterface
    ) {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getType(type: EType): KType {
        return when (type) {
            EType.STRING -> typeOf<String>()
            EType.INT -> typeOf<Int>()
            EType.BOOL -> typeOf<Boolean>()
            EType.DATE -> typeOf<Date>()
            EType.DOUBLE -> typeOf<Double>()
            EType.FLOAT -> typeOf<Float>()
            EType.NUM -> typeOf<Number>()
            EType.BLOB -> typeOf<Byte>()
            EType.TIMESTAMP -> typeOf<Timestamp>()
            EType.DATETIME -> typeOf<Time>()
            else -> typeOf<Any>()
        }
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        if (leftAttributeAST.type != rightAttributeAST.type) {
            throw SemanticError("Invalid attributes to join on")
        }
        fun <T : Comparable<T>> execMergeJoin(leftAttribute: T, rightAttribute: T): Table {
            fun compareLeftRight(comparator: EBinOp): Boolean {
                return when (comparator) {
                    EBinOp.AND -> leftAttribute.toString().isNullOrBlank() && rightAttribute.toString().isNullOrBlank()
                    EBinOp.EQUAL -> leftAttribute == rightAttribute
                    EBinOp.GREATER -> leftAttribute > rightAttribute
                    EBinOp.GREATER_EQUAL -> leftAttribute >= rightAttribute
                    EBinOp.LESS -> leftAttribute < rightAttribute
                    EBinOp.LESS_EQUAL -> leftAttribute <= rightAttribute
                    EBinOp.NOT_EQUAL -> leftAttribute != rightAttribute
                    EBinOp.OR -> leftAttribute.toString().isNullOrBlank() && rightAttribute.toString().isNullOrBlank()
                }
            }

            val type = getType(rightAttributeAST.type)
            val leftTable = left.getDataSortedBy(leftAttributeAST.value)
            val rightTable = right.getDataSortedBy(rightAttributeAST.value)
            var count = 0
            val leftAttributeIndex = leftTable.getColumnIndex(leftAttributeAST.value)
            val rightAttributeIndex = rightTable.getColumnIndex(rightAttributeAST.value)

            val combinedTable = Table()
            val combinedColumns = leftTable.columnNames.toMutableList()
            combinedColumns.removeAt(leftAttributeIndex)
            combinedColumns.addAll(rightTable.columnNames)
            combinedTable.columnNames = combinedColumns
            combinedTable.numberOfColumns = leftTable.numberOfColumns + rightTable.numberOfColumns - 1
            combinedTable.rows = mutableListOf()
            for (i in 0 until leftTable.rows.size) {
                var j = 0
                var stop = false
                loop@ while (!stop && j < rightTable.rows.size) {
                    val leftRow = leftTable.rows[i]
                    val rightRow = rightTable.rows[j]
                    if (compareLeftRight(EBinOp.GREATER)) {
                        j++
                    } else if (compareLeftRight(EBinOp.EQUAL) and overlap(leftRow, rightRow)) {
                        val combinedRow = Row()
                        when (joinType) {
                            JoinType.LEFT -> {}
                            JoinType.RIGHT -> {}
                            JoinType.UNTIL -> {
                                if ((leftRow.startTime != minTime || leftRow.endTime != minTime) && (rightRow.startTime != minTime || rightRow.endTime != minTime)) {
                                    var startTime = leftRow.endTime - 1
                                    var endTime = min(leftRow.endTime, rightRow.endTime) - 1
                                    startTime = if (startTime < minTime) minTime else startTime
                                    endTime = if (endTime < minTime) minTime else endTime
                                    val combinedData = leftRow.data.toMutableList()
                                    combinedData.removeAt(leftAttributeIndex)
                                    combinedData.addAll(rightRow.data)
                                    combinedRow.startTime = startTime
                                    combinedRow.endTime = endTime
                                    combinedRow.data = combinedData
                                    combinedTable.rows.add(combinedRow)
                                    count++
                                    break@loop
                                }
                            }
                            JoinType.SINCE -> {
                                if ((leftRow.startTime != maxTime || leftRow.endTime != maxTime) && (rightRow.startTime != maxTime || rightRow.endTime != maxTime)) {
                                    var startTime = max(leftRow.startTime, rightRow.startTime) + 1
                                    var endTime = leftRow.endTime + 1
                                    startTime = if (startTime > maxTime) maxTime else startTime
                                    endTime = if (endTime > maxTime) maxTime else endTime
                                    val combinedData = leftRow.data.toMutableList()
                                    combinedData.removeAt(leftAttributeIndex)
                                    combinedData.addAll(rightRow.data)
                                    combinedRow.startTime = startTime
                                    combinedRow.endTime = endTime
                                    combinedRow.data = combinedData
                                    combinedTable.rows.add(combinedRow)
                                    count++
                                    break@loop
                                }
                            }
                            JoinType.INNER -> {
                                val startTime = max(leftRow.startTime, rightRow.startTime)
                                val endTime = min(leftRow.endTime, rightRow.endTime)
                                val combinedData = leftRow.data.toMutableList()
                                combinedData.removeAt(leftAttributeIndex)
                                combinedData.addAll(rightRow.data)
                                combinedRow.startTime = startTime
                                combinedRow.endTime = endTime
                                combinedRow.data = combinedData
                                combinedTable.rows.add(combinedRow)
                                count++
                                break@loop
                            }
                            JoinType.CROSS -> {}
                            JoinType.FULL -> {}
                        }
                    } else {
                        stop = true
                    }
                }
            }
            return Table()
        }
        return when (leftAttributeAST.type) {
            EType.STRING -> execMergeJoin(leftAttributeAST.value, rightAttributeAST.value)
            EType.INT -> execMergeJoin(leftAttributeAST.value.toInt(), rightAttributeAST.value.toInt())
            EType.BOOL -> execMergeJoin(leftAttributeAST.value.toBoolean(), rightAttributeAST.value.toBoolean())
            EType.DATE -> execMergeJoin(leftAttributeAST.value.toLong(), rightAttributeAST.value.toLong())
            EType.DOUBLE -> execMergeJoin(leftAttributeAST.value.toDouble(), rightAttributeAST.value.toDouble())
            EType.FLOAT -> execMergeJoin(leftAttributeAST.value.toFloat(), rightAttributeAST.value.toFloat())
            EType.NUM -> execMergeJoin(leftAttributeAST.value.toBigDecimal(), rightAttributeAST.value.toBigDecimal())
            EType.BLOB -> execMergeJoin(leftAttributeAST.value.toByte(), rightAttributeAST.value.toByte())
            EType.TIMESTAMP -> execMergeJoin(leftAttributeAST.value.toLong(), rightAttributeAST.value.toLong())
            EType.DATETIME -> execMergeJoin(leftAttributeAST.value.toLong(), rightAttributeAST.value.toLong())
            else -> return null
        }
    }

    fun overlap(left: Row, right: Row): Boolean {
        return left.startTime <= right.endTime && right.startTime <= left.endTime
    }

    override fun getData(): Table {
        TODO("Not yet implemented")
    }

    override fun clone(): Table {
        TODO("Not yet implemented")
    }
}

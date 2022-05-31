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
import kotlin.math.max
import kotlin.math.min

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

    fun execMergeJoin(
        leftTable: Table,
        rightTable: Table,
        leftAttributeIndex: Int,
        rightAttributeIndex: Int,
        leftType: EType,
        rightType: EType
    ): Table {
        fun compareLeftRight(comparator: EBinOp): (Row) -> Boolean {
            if (leftType == rightType) {
                return leftTable.convertToFunctionIndexIndex(
                    leftAttributeIndex,
                    rightAttributeIndex,
                    comparator,
                    leftType
                )
            } else if (leftType.isNumeric() && rightType.isNumeric()) {
                return leftTable.convertToFunctionIndexIndexNumeric(
                    leftAttributeIndex,
                    rightAttributeIndex,
                    comparator,
                    EType.NUM
                )
            }
            return { false }
        }

        var count = 0
        val combinedTable = Table()
        val combinedColumnNames = leftTable.columnNames.toMutableList()
        // combinedColumnNames.removeAt(leftAttributeIndex)
        combinedColumnNames.addAll(rightTable.columnNames)
        val combinedColumnTypes = leftTable.columnTypes.toMutableList()
        // combinedColumnTypes.removeAt(leftAttributeIndex)
        combinedColumnTypes.addAll(rightTable.columnTypes)
        combinedTable.columnNames = combinedColumnNames
        combinedTable.columnTypes = combinedColumnTypes
        combinedTable.numberOfColumns = leftTable.numberOfColumns + rightTable.numberOfColumns - 1
        combinedTable.rows = mutableListOf()
        for (i in 0 until leftTable.rows.size) {
            var j = 0
            var stop = false
            loop@ while (!stop && j < rightTable.rows.size) {
                val leftRow = leftTable.rows[i]
                val rightRow = rightTable.rows[j]
                val combinedTempRow = Row()
                val combinedTempData = leftRow.data.toMutableList()
                combinedTempData.addAll(rightRow.data.toMutableList())
                combinedTempRow.data = combinedTempData
                if (compareLeftRight(EBinOp.GREATER)(combinedTempRow)) {
                    j++
                } else if (compareLeftRight(EBinOp.EQUAL)(combinedTempRow) && overlap(leftRow, rightRow)) {
                    val combinedRow = Row()
                    when (joinType) {
                        JoinType.LEFT -> {}
                        JoinType.RIGHT -> {}
                        JoinType.UNTIL -> {
                            if ((leftRow.startTime != minTime || leftRow.endTime != minTime) && (rightRow.startTime != minTime || rightRow.endTime != minTime)) {
                                var startTime = rightRow.endTime - 1
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
        return combinedTable
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {

        val leftTable = left.getDataSortedBy(leftAttributeAST.value)
        val rightTable = right.getDataSortedBy(rightAttributeAST.value)
        val leftAttributeIndex = leftTable.getColumnIndex(leftAttributeAST.value)
        val rightAttributeIndex = rightTable.getColumnIndex(rightAttributeAST.value)
        if (!leftAttributeAST.isLiteral) {
            leftAttributeAST.type = leftTable.columnTypes[leftAttributeIndex]
        }
        if (!rightAttributeAST.isLiteral) {
            rightAttributeAST.type = leftTable.columnTypes[rightAttributeIndex]
        }
        if (leftAttributeAST.type != rightAttributeAST.type) {
            throw SemanticError("Invalid attributes to join on")
        }
        return execMergeJoin(
            leftTable,
            rightTable,
            leftAttributeIndex,
            rightAttributeIndex,
            leftAttributeAST.type,
            rightAttributeAST.type
        )

        // return when (leftAttributeAST.type) {
        //     EType.STRING ->
        //     EType.INT -> execMergeJoin(leftAttributeAST.value.toInt(), rightAttributeAST.value.toInt())
        //     EType.BOOL -> execMergeJoin(leftAttributeAST.value.toBoolean(), rightAttributeAST.value.toBoolean())
        //     EType.DATE -> execMergeJoin(leftAttributeAST.value.toLong(), rightAttributeAST.value.toLong())
        //     EType.DOUBLE -> execMergeJoin(leftAttributeAST.value.toDouble(), rightAttributeAST.value.toDouble())
        //     EType.BIGINT -> execMergeJoin(leftAttributeAST.value.toBigInteger(), rightAttributeAST.value.toBigInteger())
        //     EType.DECIMAL -> execMergeJoin(leftAttributeAST.value.toBigDecimal(), rightAttributeAST.value.toBigDecimal())
        //     EType.FLOAT -> execMergeJoin(leftAttributeAST.value.toFloat(), rightAttributeAST.value.toFloat())
        //     EType.NUM -> execMergeJoin((leftAttributeAST.value as Number).toDouble(), (rightAttributeAST.value as Number)).toDouble())
        //     EType.BLOB -> execMergeJoin(leftAttributeAST.value.toByte(), rightAttributeAST.value.toByte())
        //     EType.TIMESTAMP -> execMergeJoin(leftAttributeAST.value.toLong(), rightAttributeAST.value.toLong())
        //     EType.DATETIME -> execMergeJoin(leftAttributeAST.value.toLong(), rightAttributeAST.value.toLong())
        //     else -> return null
        // }
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

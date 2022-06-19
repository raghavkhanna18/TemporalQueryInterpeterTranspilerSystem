package tsql.ast.nodes

import tsql.Utils.MAX_TIME
import tsql.Utils.MIN_TIME
import tsql.Utils.TIME_UNITS
import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.ast.types.EBinOp
import tsql.ast.types.EType
import tsql.ast.types.JoinType
import tsql.database.Row
import tsql.database.Table
import tsql.decrementTime
import tsql.error.SemanticError
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener
import tsql.incrementTime
import kotlin.math.max
import kotlin.math.min

class JoinAST(
    // override val position: Pair<Pair<Int, Int>, Pair<Int, Int>>,
    val joinType: JoinType,
    val left: DataSourceI,
    val right: DataSourceI,
    val leftAttributeAST: AttributeAST,
    val rightAttributeAST: AttributeAST

) : AstNodeI, Visitable(), DataSourceI {
    override val id: NodeId = AstNodeI.getId()
    val maxTime = MAX_TIME
    val minTime = MIN_TIME
    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        queryInfo: SymbolTableInterface
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
        fun compareLeftRight(comparator: EBinOp, combinedLeftIndex: Int, combinedRightIndex: Int): (Row) -> Boolean {
            if (leftType == rightType) {
                return leftTable.convertToFunctionIndexIndex(
                    combinedLeftIndex,
                    combinedRightIndex,
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
        val combinedTableName = "${leftTable.name}_${rightTable.name}"
        combinedTable.name = combinedTableName
        val combinedColumnNames = leftTable.columnNames.toMutableList().map { "${leftTable.name}.$it" }.toMutableList()
        // combinedColumnNames.removeAt(leftAttributeIndex)
        combinedColumnNames.addAll(rightTable.columnNames.toMutableList().map { "${rightTable.name}.$it"})
        val combinedColumnTypes = leftTable.columnTypes.toMutableList()
        // combinedColumnTypes.removeAt(leftAttributeIndex)
        combinedColumnTypes.addAll(rightTable.columnTypes)
        combinedTable.columnNames = combinedColumnNames
        combinedTable.columnTypes = combinedColumnTypes
        combinedTable.numberOfColumns = leftTable.numberOfColumns + rightTable.numberOfColumns
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
                if (compareLeftRight(EBinOp.GREATER, leftAttributeIndex, leftTable.numberOfColumns + rightAttributeIndex)(combinedTempRow)) {
                    j++
                } else if (compareLeftRight(EBinOp.EQUAL, leftAttributeIndex, leftTable.numberOfColumns + rightAttributeIndex)(combinedTempRow) && !overlap(leftRow, rightRow)) {
                    j++
                } else if (compareLeftRight(EBinOp.EQUAL, leftAttributeIndex, leftTable.numberOfColumns + rightAttributeIndex)(combinedTempRow) && overlap(leftRow, rightRow)) {
                    val combinedRow = Row()
                    when (joinType) {
                        JoinType.LEFT -> {}
                        JoinType.RIGHT -> {}
                        JoinType.UNTIL -> {
                            if ((leftRow.startTime != minTime || leftRow.endTime != minTime) && (rightRow.startTime != minTime || rightRow.endTime != minTime)) {
                                var startTime = decrementTime(leftRow.startTime, TIME_UNITS)
                                var endTime = decrementTime( min(leftRow.endTime, rightRow.endTime), TIME_UNITS)
                                startTime = if (startTime < minTime) minTime else startTime
                                endTime = if (endTime < minTime) minTime else endTime
                                val combinedData = leftRow.data.toMutableList()
                                // combinedData.removeAt(leftAttributeIndex)
                                combinedData.addAll(rightRow.data)
                                combinedRow.startTime = startTime
                                combinedRow.endTime = endTime
                                combinedRow.data = combinedData
                                combinedTable.rows.add(combinedRow)
                                count++

                                // break@loop
                            }
                            j++
                        }
                        JoinType.SINCE -> {
                            if ((leftRow.startTime != maxTime || leftRow.endTime != maxTime) && (rightRow.startTime != maxTime || rightRow.endTime != maxTime)) {
                                var startTime = incrementTime(max(leftRow.startTime, rightRow.startTime), TIME_UNITS)
                                var endTime = incrementTime(leftRow.endTime, TIME_UNITS)
                                startTime = if (startTime > maxTime) maxTime else startTime
                                endTime = if (endTime > maxTime) maxTime else endTime
                                val combinedData = leftRow.data.toMutableList()
                                // combinedData.removeAt(leftAttributeIndex)
                                combinedData.addAll(rightRow.data)
                                combinedRow.startTime = startTime
                                combinedRow.endTime = endTime
                                combinedRow.data = combinedData
                                combinedTable.rows.add(combinedRow)
                                count++


                                // break@loop
                            }
                            j++
                        }
                        JoinType.INNER -> {
                            val startTime = max(leftRow.startTime, rightRow.startTime)
                            val endTime = min(leftRow.endTime, rightRow.endTime)
                            val combinedData = leftRow.data.toMutableList()
                            // combinedData.removeAt(leftAttributeIndex)
                            combinedData.addAll(rightRow.data)
                            combinedRow.startTime = startTime
                            combinedRow.endTime = endTime
                            combinedRow.data = combinedData
                            combinedTable.rows.add(combinedRow)
                            count++
                            j++
                            // break@loop
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

        val leftTable = left.getDataSortedBy(leftAttributeAST.value, true)
        leftTable.name = leftAttributeAST.tableName
        val rightTable = right.getDataSortedBy(rightAttributeAST.value)
        rightTable.name = rightAttributeAST.tableName
        val leftAttributeIndex = leftTable.getColumnIndex(leftAttributeAST.value)
        val rightAttributeIndex = rightTable.getColumnIndex(rightAttributeAST.value)
        if (!leftAttributeAST.isLiteral) {
            leftAttributeAST.type = leftTable.columnTypes[leftAttributeIndex]
        }
        if (!rightAttributeAST.isLiteral) {
            rightAttributeAST.type = rightTable.columnTypes[rightAttributeIndex]
        }
        if (leftAttributeAST.type != rightAttributeAST.type && !(leftAttributeAST.type.isNumeric() && rightAttributeAST.type.isNumeric())) {
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
    }

    fun overlap(left: Row, right: Row): Boolean {
        return left.startTime < right.endTime && right.startTime < left.endTime
    }

    override fun getData(): Table {
        TODO("Not yet implemented")
    }

    override fun clone(): Table {
        TODO("Not yet implemented")
    }
}

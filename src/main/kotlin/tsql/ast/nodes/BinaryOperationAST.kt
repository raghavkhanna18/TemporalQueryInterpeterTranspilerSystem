package tsql.ast.nodes

import tsql.Utils
import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTable
import tsql.database.Row
import tsql.database.Table
import tsql.decrementTime
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener
import tsql.getOverlapContion
import tsql.getTimeUnitString
import tsql.incrementTime
import tsql.overlap
import kotlin.math.max
import kotlin.math.min

class BinaryOperationAST(
    val operator: BinaryOperatorAST,
    val lhs: DataSourceI,
    val rhs: DataSourceI
) : AstNodeI, Visitable(), DataSourceI {



    override val id: NodeId = AstNodeI.getId()
    val maxTime = Utils.MAX_TIME
    val minTime = Utils.MIN_TIME
    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        queryInfo: SymbolTable
    ) {
        lhs.checkNode(syntaxErrorListener, semanticErrorListener, queryInfo)
        rhs.checkNode(syntaxErrorListener, semanticErrorListener, queryInfo)

    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        val leftTable = lhs.getData() as Table
        val rightTable = rhs.getData() as Table
        val combinedTable = Table()
        combinedTable.numberOfColumns = leftTable.numberOfColumns + rightTable.numberOfColumns
        combinedTable.name = leftTable.name + rightTable.name
        combinedTable.columnTypes = (leftTable.columnTypes + rightTable.columnTypes).toMutableList()
        val leftNewColumnNames = mutableListOf<String>()
        for (col in leftTable.columnNames) {
            val newName = leftTable.name +"." + col
            leftNewColumnNames.add(newName)
        }
        val rightNewColumnNames = mutableListOf<String>()
        for (col in rightTable.columnNames) {
            val newName = rightTable.name +"." + col
            rightNewColumnNames.add(newName)
        }
        combinedTable.columnNames = (leftNewColumnNames + rightNewColumnNames).toMutableList()
        combinedTable.rows = mutableListOf()
        var count = 0
        for (lrow in leftTable.rows) {
            for (rrow in rightTable.rows) {
                if (overlap(lrow, rrow)) {
                    when (operator.operator) {
                        BinaryOperatorEnum.TIMES -> {
                            val startTime = max(lrow.startTime, rrow.startTime)
                            val endTime = min(lrow.endTime, rrow.endTime)
                            val combinedData = lrow.data.toMutableList()
                            combinedData.addAll(rrow.data)
                            val newRow = Row(startTime, endTime, combinedData)
                            combinedTable.rows.add(newRow)
                            count++
                        }
                        BinaryOperatorEnum.SINCE -> {
                            if ((lrow.startTime != maxTime || lrow.endTime != maxTime) && (rrow.startTime != maxTime || rrow.endTime != maxTime)) {
                                var startTime = incrementTime(max(lrow.startTime, rrow.startTime), Utils.TIME_UNITS)
                                var endTime = incrementTime(lrow.endTime, Utils.TIME_UNITS)
                                startTime = if (startTime > maxTime) maxTime else startTime
                                endTime = if (endTime > maxTime) maxTime else endTime
                                val combinedData = lrow.data.toMutableList()
                                combinedData.addAll(rrow.data)
                                val newRow = Row(startTime, endTime, combinedData)
                                combinedTable.rows.add(newRow)
                                count++
                            }
                        }
                        BinaryOperatorEnum.UNTIL -> {
                            if ((lrow.startTime != minTime || lrow.endTime != minTime) && (rrow.startTime != minTime || rrow.endTime != minTime)) {
                                var startTime = decrementTime(lrow.startTime, Utils.TIME_UNITS)
                                var endTime = decrementTime(min(lrow.endTime, rrow.endTime), Utils.TIME_UNITS)
                                startTime = if (startTime < minTime) minTime else startTime
                                endTime = if (endTime < minTime) minTime else endTime
                                val combinedData = lrow.data.toMutableList()
                                combinedData.addAll(rrow.data)
                                val newRow = Row(startTime, endTime, combinedData)
                                combinedTable.rows.add(newRow)
                                count++
                            }
                        }
                    }
                }
            }
        }
        return combinedTable
    }

    override fun toSQL(symbolTable: SymbolTable?): Pair<String, Pair<String, String>> {
        val leftSQL = lhs.toSQL(symbolTable)
        val rightSQL = rhs.toSQL(symbolTable)
        val leftTableSQL = leftSQL.first
        val rightTableSQl = rightSQL.first
        val productSQL = convertProductToSQL(symbolTable!!)
        return Pair(
            "$leftTableSQL CROSS JOIN  $rightTableSQl ", productSQL.second
        )
    }

    fun convertProductToSQL(symbolTable: SymbolTable): Pair<String, Pair<String, String>> {
        val tableNames = symbolTable.getTableNames()
        val firstTable = tableNames.first()
        val firstTableName = if (firstTable.second != "") firstTable.second else firstTable.first
        val secondTable = tableNames.get(1)
        val secondTableName = if (secondTable.second != "") secondTable.second else secondTable.first
        val timeUnit = getTimeUnitString()
        when (operator.operator) {
            BinaryOperatorEnum.SINCE -> {
                val plusClause = "+ INTERVAL $timeUnit"
                val additionalSelect =
                    "(GREATEST($firstTableName.start_time, $secondTableName.start_time) $plusClause) AS s, ($firstTableName.end_time $plusClause) AS e"
                return Pair("CROSS JOIN", Pair(getOverlapContion(symbolTable), additionalSelect))
            }
            BinaryOperatorEnum.UNTIL -> {
                val minusClause = "- INTERVAL $timeUnit"
                val additionalSelect =
                    "($firstTableName.start_time $minusClause) AS s, (LEAST($firstTableName.end_time, $secondTableName.end_time) $minusClause) AS e"
                return Pair("CROSS JOIN", Pair(getOverlapContion(symbolTable), additionalSelect))
            }
            BinaryOperatorEnum.TIMES -> {
                val additionalSelect =
                    "GREATEST($firstTableName.start_time, $secondTableName.start_time) AS s, (LEAST($firstTableName.end_time, $secondTableName.end_time) AS e"
                return Pair("CROSS JOIN", Pair(getOverlapContion(symbolTable), additionalSelect))
            }
        }
        return Pair("", Pair("", ""))
    }
    override fun getData(): DataSourceI {
        return execute()!!
    }
    override fun clone(): DataSourceI {
       return getData()
    }


}

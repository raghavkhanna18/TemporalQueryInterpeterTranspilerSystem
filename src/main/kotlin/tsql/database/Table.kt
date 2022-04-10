package tsql.database

import tsql.ast.nodes.DataSourceI
import tsql.ast.types.EBinOp
import tsql.ast.types.EType
import tsql.error.SemanticError

class Table(
    var columnNames: List<String> = mutableListOf(),
    var columnTypes: MutableList<Int> = mutableListOf(),
    var rows: MutableList<Row> = mutableListOf(),
    var numberOfColumns: Int = 0
) : DataSourceI, Cloneable {

    fun putCollumns(numberOfColumns: Int, columnNames: List<String>, columnTypes: MutableList<Int>) {
        this.numberOfColumns = numberOfColumns
        this.columnNames = columnNames
        this.columnTypes = columnTypes
    }

    fun putRow(rowValues: Array<Any>, start_time: Int, end_time: Int) {
        val row = Row(start_time, end_time, rowValues.toMutableList())
        rows.add(row)
    }

    override fun getData(): Table {
        return this;
    }

    override fun clone(): Table {
        return Table(columnNames.toMutableList(), columnTypes.toMutableList(), ArrayList(rows.map { it.copy() }), numberOfColumns)
    }

   fun removeColumn(columnName: String) {
        for (i in columnNames.indices) {
            if (columnNames[i] == columnName) {
                rows.map { row -> row.deleteColumn(i) }
                columnTypes.removeAt(i)
            }
        }
       columnNames = columnNames.filter { it != columnName }
    }

    fun removeColumns(colNames: List<String>) {
        for (i in this.columnNames.indices) {
            if (colNames.contains(this.columnNames[i])){
                rows.map { row -> row.deleteColumn(i) }
                columnTypes.removeAt(i)
            }
        }
        this.columnNames = this.columnNames.minus(colNames)
    }

    override fun project(columns: List<String>) {
        if (this.columnNames.containsAll(columns)) {
            val columnsToRemove = this.columnNames.minus(columns)
            this.removeColumns(columnsToRemove)
        } else {
            throw SemanticError("Invalid Columns Provided")
        }
    }

    fun filter(conditions: Pair<MutableList<EBinOp>, MutableList<Condition>>) {
        val conditionFunctions = conditions.second.map { createFilterCondition(it) }.toMutableList()
        conditionFunctions.reverse()
        val conjunctions = conditions.first.toMutableList()
        conjunctions.reverse()
        val tempConditions = mutableListOf<(row: Row) -> Boolean>()
        for (i in conjunctions.indices) {
            if (conjunctions[i] == EBinOp.AND) {
                tempConditions.add(fun(row: Row): Boolean {
                    return conditionFunctions[i](row) && conditionFunctions[i + 1](row)
                })
            } else {
                tempConditions.add(conditionFunctions[i])
            }
        }
        val reduced = tempConditions.reduce { a, b -> { i -> a(i) || b(i) } }
        rows.filter { reduced(it) }
    }

    private fun createFilterCondition(condition: Condition): (row: Row) -> Boolean {
        if (condition.lhsIsLiteral && condition.rhsIsLiteral) {
            if (condition.lhsType == EType.STRING && condition.rhsType == EType.STRING) {
                return convertToFunction(condition.lhs, condition.rhs, condition.comparator)
            }
            if (condition.lhsType == EType.INT && condition.rhsType == EType.INT) {
                return convertToFunction(condition.lhs.toInt(), condition.rhs.toInt(), condition.comparator)
            }
            if (condition.lhsType == EType.DOUBLE && condition.rhsType == EType.DOUBLE) {
                return convertToFunction(condition.lhs.toDouble(), condition.rhs.toDouble(), condition.comparator)
            }
            if (condition.lhsType == EType.FLOAT && condition.rhsType == EType.FLOAT) {
                return convertToFunction(condition.lhs.toFloat(), condition.rhs.toFloat(), condition.comparator)
            }
        }

        if (!condition.lhsIsLiteral && condition.rhsIsLiteral) {
            if (condition.lhsType == EType.STRING && condition.rhsType == EType.STRING) {
                return convertToFunctionIndex(getColumnIndex(condition.lhs), condition.rhs, condition.comparator)
            }
            if (condition.lhsType == EType.INT && condition.rhsType == EType.INT) {
                return convertToFunctionIndex(
                    getColumnIndex(condition.lhs), condition.rhs.toInt(), condition.comparator
                )
            }
            if (condition.lhsType == EType.DOUBLE && condition.rhsType == EType.DOUBLE) {
                return convertToFunctionIndex(
                    getColumnIndex(condition.lhs), condition.rhs.toDouble(), condition.comparator
                )
            }
            if (condition.lhsType == EType.FLOAT && condition.rhsType == EType.FLOAT) {
                return convertToFunctionIndex(
                    getColumnIndex(condition.lhs), condition.rhs.toFloat(), condition.comparator
                )
            }
        }

        if (!condition.lhsIsLiteral && !condition.rhsIsLiteral) {
            if (condition.lhsType == condition.rhsType) {
                return convertToFunctionIndexIndex(
                    getColumnIndex(condition.lhs),
                    getColumnIndex(condition.rhs),
                    condition.comparator,
                    condition.lhsType
                )
            }
        }

        if (condition.lhsIsLiteral && !condition.rhsIsLiteral) {
            return createFilterCondition(condition.flip())
        }

        throw IllegalArgumentException("LHS and RHS types dont match: LHS Type is ${condition.lhsType}, RHS Type is ${condition.rhsType}")
    }

    private fun <T : Comparable<T>> convertToFunction(lhs: T, rhs: T, operator: EBinOp): (Row) -> Boolean {
        return when (operator) {
            EBinOp.EQUAL -> { row: Row -> lhs == rhs }
            EBinOp.LESS_EQUAL -> { row: Row -> (lhs <= rhs) }
            EBinOp.GREATER_EQUAL -> { row: Row -> (lhs > rhs) }
            EBinOp.LESS -> { row: Row -> (lhs < rhs) }
            EBinOp.GREATER -> { row: Row -> (lhs > rhs) }
            EBinOp.NOT_EQUAL -> { row: Row -> (lhs != rhs) }
            else -> { row: Row -> false }
        }
    }

    private fun <T : Comparable<T>> convertToFunctionIndex(index: Int, rhs: T, operator: EBinOp): (Row) -> Boolean {
        return when (operator) {
            EBinOp.EQUAL -> { row: Row -> row.data[index] == rhs }
            EBinOp.LESS_EQUAL -> { row: Row -> (row.data[index] as T <= rhs) }
            EBinOp.GREATER_EQUAL -> { row: Row -> (row.data[index] as T >= rhs) }
            EBinOp.LESS -> { row: Row -> ((row.data[index] as T) < rhs) }
            EBinOp.GREATER -> { row: Row -> (row.data[index] as T > rhs) }
            EBinOp.NOT_EQUAL -> { row: Row -> (row.data[index] != rhs) }
            else -> { row: Row -> false }
        }
    }

    private fun convertToFunctionIndexIndex(
        indexLhs: Int, indexRhs: Int, operator: EBinOp, type: EType
    ): (Row) -> Boolean {

        return when (operator) {
            EBinOp.EQUAL -> {
                fun(row: Row): Boolean {
                    when (type) {
                        EType.STRING -> {
                            val lhsData = getDataAsString(row, indexLhs)
                            val rhsData = getDataAsString(row, indexRhs)
                            return lhsData == rhsData
                        }
                        EType.INT -> {
                            val lhsData = getDataAsInt(row, indexLhs)
                            val rhsData = getDataAsInt(row, indexRhs)
                            return lhsData == rhsData
                        }
                        EType.BOOL -> {
                            val lhsData = getDataAsBoolean(row, indexLhs)
                            val rhsData = getDataAsBoolean(row, indexRhs)
                            return lhsData == rhsData
                        }
                        EType.DATE -> TODO()
                        EType.DOUBLE -> {
                            val lhsData = getDataAsDouble(row, indexLhs)
                            val rhsData = getDataAsDouble(row, indexRhs)
                            return lhsData == rhsData
                        }
                        EType.FLOAT -> {
                            val lhsData = getDataAsFloat(row, indexLhs)
                            val rhsData = getDataAsFloat(row, indexRhs)
                            return lhsData == rhsData
                        }
                        EType.UNKNOWN -> TODO()
                        EType.NULL -> TODO()
                        EType.STATEMENT -> TODO()
                        EType.ERROR -> TODO()
                        EType.NUMBER -> TODO()
                        EType.BLOB -> TODO()
                        EType.TIMESTAMP -> TODO()
                        EType.DATETIME -> TODO()
                    }
                }
            }
            EBinOp.LESS_EQUAL -> {
                fun(row: Row): Boolean {
                    when (type) {
                        EType.STRING -> {
                            val lhsData = getDataAsString(row, indexLhs)
                            val rhsData = getDataAsString(row, indexRhs)
                            return lhsData <= rhsData
                        }
                        EType.INT -> {
                            val lhsData = getDataAsInt(row, indexLhs)
                            val rhsData = getDataAsInt(row, indexRhs)
                            return lhsData <= rhsData
                        }
                        EType.BOOL -> {
                            val lhsData = getDataAsBoolean(row, indexLhs)
                            val rhsData = getDataAsBoolean(row, indexRhs)
                            return lhsData <= rhsData
                        }
                        EType.DATE -> TODO()
                        EType.DOUBLE -> {
                            val lhsData = getDataAsDouble(row, indexLhs)
                            val rhsData = getDataAsDouble(row, indexRhs)
                            return lhsData <= rhsData
                        }
                        EType.FLOAT -> {
                            val lhsData = getDataAsFloat(row, indexLhs)
                            val rhsData = getDataAsFloat(row, indexRhs)
                            return lhsData <= rhsData
                        }
                        EType.UNKNOWN -> TODO()
                        EType.NULL -> TODO()
                        EType.STATEMENT -> TODO()
                        EType.ERROR -> TODO()
                        EType.NUMBER -> TODO()
                        EType.BLOB -> TODO()
                        EType.TIMESTAMP -> TODO()
                        EType.DATETIME -> TODO()
                    }
                }
            }
            EBinOp.GREATER_EQUAL -> {
                fun(row: Row): Boolean {
                    when (type) {
                        EType.STRING -> {
                            val lhsData = getDataAsString(row, indexLhs)
                            val rhsData = getDataAsString(row, indexRhs)
                            return lhsData >= rhsData
                        }
                        EType.INT -> {
                            val lhsData = getDataAsInt(row, indexLhs)
                            val rhsData = getDataAsInt(row, indexRhs)
                            return lhsData >= rhsData
                        }
                        EType.BOOL -> {
                            val lhsData = getDataAsBoolean(row, indexLhs)
                            val rhsData = getDataAsBoolean(row, indexRhs)
                            return lhsData >= rhsData
                        }
                        EType.DATE -> TODO()
                        EType.DOUBLE -> {
                            val lhsData = getDataAsDouble(row, indexLhs)
                            val rhsData = getDataAsDouble(row, indexRhs)
                            return lhsData >= rhsData
                        }
                        EType.FLOAT -> {
                            val lhsData = getDataAsFloat(row, indexLhs)
                            val rhsData = getDataAsFloat(row, indexRhs)
                            return lhsData >= rhsData
                        }
                        EType.UNKNOWN -> TODO()
                        EType.NULL -> TODO()
                        EType.STATEMENT -> TODO()
                        EType.ERROR -> TODO()
                        EType.NUMBER -> TODO()
                        EType.BLOB -> TODO()
                        EType.TIMESTAMP -> TODO()
                        EType.DATETIME -> TODO()
                    }
                }
            }
            EBinOp.LESS -> {
                fun(row: Row): Boolean {
                    when (type) {
                        EType.STRING -> {
                            val lhsData = getDataAsString(row, indexLhs)
                            val rhsData = getDataAsString(row, indexRhs)
                            return lhsData < rhsData
                        }
                        EType.INT -> {
                            val lhsData = getDataAsInt(row, indexLhs)
                            val rhsData = getDataAsInt(row, indexRhs)
                            return lhsData < rhsData
                        }
                        EType.BOOL -> {
                            val lhsData = getDataAsBoolean(row, indexLhs)
                            val rhsData = getDataAsBoolean(row, indexRhs)
                            return lhsData < rhsData
                        }
                        EType.DATE -> TODO()
                        EType.DOUBLE -> {
                            val lhsData = getDataAsDouble(row, indexLhs)
                            val rhsData = getDataAsDouble(row, indexRhs)
                            return lhsData < rhsData
                        }
                        EType.FLOAT -> {
                            val lhsData = getDataAsFloat(row, indexLhs)
                            val rhsData = getDataAsFloat(row, indexRhs)
                            return lhsData < rhsData
                        }
                        EType.UNKNOWN -> TODO()
                        EType.NULL -> TODO()
                        EType.STATEMENT -> TODO()
                        EType.ERROR -> TODO()
                        EType.NUMBER -> TODO()
                        EType.BLOB -> TODO()
                        EType.TIMESTAMP -> TODO()
                        EType.DATETIME -> TODO()
                    }
                }
            }
            EBinOp.GREATER -> {
                fun(row: Row): Boolean {
                    when (type) {
                        EType.STRING -> {
                            val lhsData = getDataAsString(row, indexLhs)
                            val rhsData = getDataAsString(row, indexRhs)
                            return lhsData > rhsData
                        }
                        EType.INT -> {
                            val lhsData = getDataAsInt(row, indexLhs)
                            val rhsData = getDataAsInt(row, indexRhs)
                            return lhsData > rhsData
                        }
                        EType.BOOL -> {
                            val lhsData = getDataAsBoolean(row, indexLhs)
                            val rhsData = getDataAsBoolean(row, indexRhs)
                            return lhsData > rhsData
                        }
                        EType.DATE -> TODO()
                        EType.DOUBLE -> {
                            val lhsData = getDataAsDouble(row, indexLhs)
                            val rhsData = getDataAsDouble(row, indexRhs)
                            return lhsData > rhsData
                        }
                        EType.FLOAT -> {
                            val lhsData = getDataAsFloat(row, indexLhs)
                            val rhsData = getDataAsFloat(row, indexRhs)
                            return lhsData > rhsData
                        }
                        EType.UNKNOWN -> TODO()
                        EType.NULL -> TODO()
                        EType.STATEMENT -> TODO()
                        EType.ERROR -> TODO()
                        EType.NUMBER -> TODO()
                        EType.BLOB -> TODO()
                        EType.TIMESTAMP -> TODO()
                        EType.DATETIME -> TODO()
                    }
                }
            }
            EBinOp.NOT_EQUAL -> {
                fun(row: Row): Boolean {
                    when (type) {
                        EType.STRING -> {
                            val lhsData = getDataAsString(row, indexLhs)
                            val rhsData = getDataAsString(row, indexRhs)
                            return lhsData != rhsData
                        }
                        EType.INT -> {
                            val lhsData = getDataAsInt(row, indexLhs)
                            val rhsData = getDataAsInt(row, indexRhs)
                            return lhsData != rhsData
                        }
                        EType.BOOL -> {
                            val lhsData = getDataAsBoolean(row, indexLhs)
                            val rhsData = getDataAsBoolean(row, indexRhs)
                            return lhsData != rhsData
                        }
                        EType.DATE -> TODO()
                        EType.DOUBLE -> {
                            val lhsData = getDataAsDouble(row, indexLhs)
                            val rhsData = getDataAsDouble(row, indexRhs)
                            return lhsData != rhsData
                        }
                        EType.FLOAT -> {
                            val lhsData = getDataAsFloat(row, indexLhs)
                            val rhsData = getDataAsFloat(row, indexRhs)
                            return lhsData != rhsData
                        }
                        EType.UNKNOWN -> TODO()
                        EType.NULL -> TODO()
                        EType.STATEMENT -> TODO()
                        EType.ERROR -> TODO()
                        EType.NUMBER -> TODO()
                        EType.BLOB -> TODO()
                        EType.TIMESTAMP -> TODO()
                        EType.DATETIME -> TODO()
                    }
                }
            }
            else -> { row: Row -> false }
        }
    }

    fun getColumnIndex(column: String): Int {
        return columnNames.indexOf(column)
    }

    private fun getDataAsString(row: Row, index: Int): String {
        return row.data[index] as String
    }

    private fun getDataAsInt(row: Row, index: Int): Int {
        return row.data[index] as Int
    }

    private fun getDataAsBoolean(row: Row, index: Int): Boolean {
        return row.data[index] as Boolean
    }

    private fun getDataAsDouble(row: Row, index: Int): Double {
        return row.data[index] as Double
    }

    private fun getDataAsFloat(row: Row, index: Int): Float {
        return row.data[index] as Float
    }
}

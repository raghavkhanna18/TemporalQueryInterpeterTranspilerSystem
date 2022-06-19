package tsql.database

import tsql.ast.nodes.AstNodeI
import tsql.ast.nodes.DataSourceI
import tsql.ast.nodes.NodeId
import tsql.ast.symbol_table.SymbolTable
import tsql.ast.types.EBinOp
import tsql.ast.types.EType
import tsql.error.SemanticError
import tsql.error.SyntaxErrorListener
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant

class Table(
    var columnNames: MutableList<String> = mutableListOf(),
    var columnTypes: MutableList<EType> = mutableListOf(),
    var rows: MutableList<Row> = mutableListOf(),
    var numberOfColumns: Int = 0,
    var name: String = ""
) : DataSourceI, Cloneable {
    override val id: NodeId = AstNodeI.getId()
    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        queryInfo: SymbolTable
    ) {
        TODO("Not yet implemented")
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        TODO("Not yet implemented")
    }

    fun putCollumns(numberOfColumns: Int, columnNames: MutableList<String>, columnTypes: MutableList<EType>) {
        this.numberOfColumns = numberOfColumns
        this.columnNames = columnNames
        this.columnTypes = columnTypes
    }

    fun putRow(rowValues: Array<Any>, start_time: Long, end_time: Long) {
        val row = Row(start_time, end_time, rowValues.toMutableList())
        rows.add(row)
    }

    override fun getData(): Table {
        return this
    }

    override fun clone(): Table {
        return Table(
            columnNames.toMutableList(),
            columnTypes.toMutableList(),
            ArrayList(rows.map { it.copy() }),
            numberOfColumns
        )
    }

    fun removeColumn(columnName: String) {
        for (i in columnNames.indices) {
            if (columnNames[i] == columnName) {
                rows.map { row -> row.deleteColumn(i) }
                columnTypes.removeAt(i)
            }
        }
        columnNames = columnNames.filter { it != columnName }.toMutableList()
        numberOfColumns--
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun removeColumns(colNames: List<String>) {
        val indicesToRemove = ArrayDeque<Int>()
        for (i in this.columnNames.indices) {
            if (colNames.contains(this.columnNames[i])) {
                indicesToRemove.addLast(i)
            }
        }
        while (indicesToRemove.isNotEmpty()) {
            val i = indicesToRemove.removeLast()
            this.columnNames.removeAt(i)
            this.columnTypes.removeAt(i)
            this.rows.map { row -> row.deleteColumn(i) }
            numberOfColumns--
        }
    }

    override fun project(columns: List<String>) {

        if (this.columnNames.containsAll(columns)) {
            val columnsToRemove = this.columnNames.toMutableList().minus(columns)
            this.removeColumns(columnsToRemove)
        } else {
            for (column in columns) {
                if (column == "*") {
                    continue
                } else if (this.columnNames.contains(column)) {
                    this.removeColumn(column)
                } else {
                    throw SemanticError("Invalid Columns Provided")
                }
            }
        }
    }

    fun filter(conditions: Pair<MutableList<EBinOp>, MutableList<Condition>>) {
        if (conditions.second.isEmpty()) {
            return
        }
        val conditionFunctions = conditions.second.map { createFilterCondition(it) }.toMutableList()
        conditionFunctions.reverse()
        val conjunctions = conditions.first.toMutableList()
        conjunctions.reverse()
        val tempConditions = mutableListOf<(row: Row) -> Boolean>()
        if (conjunctions.isEmpty()) {
            tempConditions.addAll(conditionFunctions)
        } else {
            for (i in conjunctions.indices) {
                if (conjunctions[i] == EBinOp.AND && i < conditionFunctions.size - 1) {
                    tempConditions.add(fun(row: Row): Boolean {
                        return conditionFunctions[i](row) && conditionFunctions[i + 1](row)
                    })
                } else if (conjunctions[i] == EBinOp.AND) {
                    tempConditions.add(fun(row: Row): Boolean {
                        return conditionFunctions[i](row)
                    })
                } else if (i < conjunctions.size - 1) {
                    tempConditions.add(conditionFunctions[i])
                    tempConditions.add(conditionFunctions[i + 1])
                } else {
                    tempConditions.add(conditionFunctions[i])
                }
            }
        }
        val reduced = tempConditions.reduce { a, b -> { i -> a(i) || b(i) } }
        val newRows = mutableListOf<Row>()
        for (row in rows) {
            if (reduced(row)) {
                newRows.add(row)
            }
        }
        this.rows = newRows
    }

    private fun createFilterCondition(condition: Condition): (row: Row) -> Boolean {
        if (condition.lhsIsLiteral && condition.rhsIsLiteral) {

            if (condition.lhsType == EType.STRING && condition.rhsType == EType.STRING) {
                return convertToFunction(condition.lhs, condition.rhs, condition.comparator)
            }
            if (condition.lhsType == EType.INT && condition.rhsType == EType.INT) {
                return convertToFunction(condition.lhs.toInt(), condition.rhs.toInt(), condition.comparator)
            }
            if (condition.lhsType == EType.BIGINT && condition.rhsType == EType.BIGINT) {
                return convertToFunction(
                    condition.lhs.toBigInteger(),
                    condition.rhs.toBigInteger(),
                    condition.comparator
                )
            }
            if (condition.lhsType == EType.DOUBLE && condition.rhsType == EType.DOUBLE) {
                return convertToFunction(condition.lhs.toDouble(), condition.rhs.toDouble(), condition.comparator)
            }
            if (condition.lhsType == EType.FLOAT && condition.rhsType == EType.FLOAT) {
                return convertToFunction(condition.lhs.toFloat(), condition.rhs.toFloat(), condition.comparator)
            }
            if (condition.lhsType == EType.LONG && condition.rhsType == EType.LONG) {
                return convertToFunction(condition.lhs.toLong(), condition.rhs.toLong(), condition.comparator)
            }
            if (condition.lhsType == EType.BIGINT && condition.rhsType == EType.BIGINT) {
                return convertToFunction(
                    condition.lhs.toBigInteger(),
                    condition.rhs.toBigInteger(),
                    condition.comparator
                )
            }
            if (condition.lhsType == EType.INT && condition.rhsType == EType.INT) {
                return convertToFunction(
                    condition.lhs.toInt(),
                    condition.rhs.toInt(),
                    condition.comparator
                )
            }
            if (condition.lhsType == EType.DECIMAL && condition.rhsType == EType.DECIMAL) {
                return convertToFunction(
                    condition.lhs.toBigDecimal(),
                    condition.rhs.toBigDecimal(),
                    condition.comparator
                )
            }
            if (condition.lhsType.isNumeric() && condition.rhsType.isNumeric()) {
                return convertToFunction(condition.lhs.toDouble(), condition.rhs.toDouble(), condition.comparator)
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
            if (condition.lhsType == EType.LONG && condition.rhsType == EType.LONG) {
                return convertToFunctionIndex(
                    getColumnIndex(condition.lhs), condition.rhs.toLong(), condition.comparator
                )
            }
            if (condition.lhsType == EType.BIGINT && condition.rhsType == EType.BIGINT) {
                return convertToFunctionIndex(
                    getColumnIndex(condition.lhs), condition.rhs.toBigInteger(), condition.comparator
                )
            }
            if (condition.lhsType == EType.INT && condition.rhsType == EType.INT) {
                return convertToFunctionIndex(
                    getColumnIndex(condition.lhs), condition.rhs.toInt(), condition.comparator
                )
            }
            if (condition.lhsType == EType.DECIMAL && condition.rhsType == EType.DECIMAL) {
                return convertToFunctionIndex(
                    getColumnIndex(condition.lhs), condition.rhs.toBigDecimal(), condition.comparator
                )
            }
            if (condition.lhsType.isNumeric() && condition.rhsType.isNumeric()) {
                return convertToFunctionIndexNumeric(
                    getColumnIndex(condition.lhs),
                    condition.rhs.toDouble(),
                    condition.comparator
                )
            }
        }

        if (!condition.lhsIsLiteral && !condition.rhsIsLiteral) {

            if (condition.lhsType.isNumeric() && condition.rhsType.isNumeric()) {
                return convertToFunctionIndexIndexNumeric(
                    getColumnIndex(condition.lhs),
                    getColumnIndex(condition.rhs),
                    condition.comparator,
                    condition.lhsType
                )
            }
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

    fun <T : Comparable<T>> convertToFunction(lhs: T, rhs: T, operator: EBinOp): (Row) -> Boolean {
        return when (operator) {
            EBinOp.EQUAL -> { _: Row -> lhs == rhs }
            EBinOp.LESS_EQUAL -> { _: Row -> (lhs <= rhs) }
            EBinOp.GREATER_EQUAL -> { _: Row -> (lhs > rhs) }
            EBinOp.LESS -> { _: Row -> (lhs < rhs) }
            EBinOp.GREATER -> { _: Row -> (lhs > rhs) }
            EBinOp.NOT_EQUAL -> { _: Row -> (lhs != rhs) }
            else -> { _: Row -> false }
        }
    }

    fun convertToFunction(lhs: Number, rhs: Number, operator: EBinOp): (Row) -> Boolean {
        return when (operator) {
            EBinOp.EQUAL -> { _: Row -> lhs.toDouble() == rhs.toDouble() }
            EBinOp.LESS_EQUAL -> { _: Row -> (lhs.toDouble() <= rhs.toDouble()) }
            EBinOp.GREATER_EQUAL -> { _: Row -> (lhs.toDouble() > rhs.toDouble()) }
            EBinOp.LESS -> { _: Row -> (lhs.toDouble() < rhs.toDouble()) }
            EBinOp.GREATER -> { _: Row -> (lhs.toDouble() > rhs.toDouble()) }
            EBinOp.NOT_EQUAL -> { _: Row -> (lhs.toDouble() != rhs.toDouble()) }
            else -> { _: Row -> false }
        }
    }

    fun convertToFunctionIndexNumeric(index: Int, rhs: Number, operator: EBinOp): (Row) -> Boolean {
        return when (operator) {
            EBinOp.EQUAL -> { row: Row -> (row.data[index] as Number).toDouble() == rhs.toDouble() }
            EBinOp.LESS_EQUAL -> { row: Row -> ((row.data[index] as Number).toDouble() <= rhs.toDouble()) }
            EBinOp.GREATER_EQUAL -> { row: Row -> ((row.data[index] as Number).toDouble() > rhs.toDouble()) }
            EBinOp.LESS -> { row: Row -> ((row.data[index] as Number).toDouble() < rhs.toDouble()) }
            EBinOp.GREATER -> { row: Row -> ((row.data[index] as Number).toDouble() > rhs.toDouble()) }
            EBinOp.NOT_EQUAL -> { row: Row -> ((row.data[index] as Number).toDouble() != rhs.toDouble()) }
            else -> { _: Row -> false }
        }
    }

    fun <T : Comparable<T>> convertToFunctionIndex(index: Int, rhs: T, operator: EBinOp): (Row) -> Boolean {
        return when (operator) {
            EBinOp.EQUAL -> { row: Row -> row.data[index] as T == rhs }
            EBinOp.LESS_EQUAL -> { row: Row -> (row.data[index] as T <= rhs) }
            EBinOp.GREATER_EQUAL -> { row: Row -> (row.data[index] as T >= rhs) }
            EBinOp.LESS -> { row: Row -> ((row.data[index] as T) < rhs) }
            EBinOp.GREATER -> { row: Row -> (row.data[index] as T > rhs) }
            EBinOp.NOT_EQUAL -> { row: Row -> (row.data[index] as T != rhs) }
            else -> { _: Row -> false }
        }
    }

    fun convertToFunctionIndexIndex(
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
                        EType.LONG -> {
                            val lhsData = getDataAsLong(row, indexLhs)
                            val rhsData = getDataAsLong(row, indexRhs)
                            return lhsData == rhsData
                        }
                        EType.BIGINT -> {
                            val lhsData = getDataAsBigInt(row, indexLhs)
                            val rhsData = getDataAsBigInt(row, indexRhs)
                            return lhsData == rhsData
                        }
                        EType.DECIMAL -> {
                            val lhsData = getDataAsDecimal(row, indexLhs)
                            val rhsData = getDataAsDecimal(row, indexRhs)
                            return lhsData == rhsData
                        }
                        EType.NUM -> {
                            val lhsData = getDataAsNumber(row, indexLhs)
                            val rhsData = getDataAsNumber(row, indexRhs)
                            return lhsData.toDouble() > rhsData.toDouble()
                        }
                        else -> return false
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
                        EType.LONG -> {
                            val lhsData = getDataAsLong(row, indexLhs)
                            val rhsData = getDataAsLong(row, indexRhs)
                            return lhsData <= rhsData
                        }
                        EType.BIGINT -> {
                            val lhsData = getDataAsBigInt(row, indexLhs)
                            val rhsData = getDataAsBigInt(row, indexRhs)
                            return lhsData <= rhsData
                        }
                        EType.DECIMAL -> {
                            val lhsData = getDataAsDecimal(row, indexLhs)
                            val rhsData = getDataAsDecimal(row, indexRhs)
                            return lhsData <= rhsData
                        }
                        EType.NUM -> {
                            val lhsData = getDataAsNumber(row, indexLhs)
                            val rhsData = getDataAsNumber(row, indexRhs)
                            return lhsData.toDouble() > rhsData.toDouble()
                        }
                        else -> return false
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
                        EType.LONG -> {
                            val lhsData = getDataAsLong(row, indexLhs)
                            val rhsData = getDataAsLong(row, indexRhs)
                            return lhsData >= rhsData
                        }
                        EType.BIGINT -> {
                            val lhsData = getDataAsBigInt(row, indexLhs)
                            val rhsData = getDataAsBigInt(row, indexRhs)
                            return lhsData >= rhsData
                        }
                        EType.DECIMAL -> {
                            val lhsData = getDataAsDecimal(row, indexLhs)
                            val rhsData = getDataAsDecimal(row, indexRhs)
                            return lhsData >= rhsData
                        }
                        EType.NUM -> {
                            val lhsData = getDataAsNumber(row, indexLhs)
                            val rhsData = getDataAsNumber(row, indexRhs)
                            return lhsData.toDouble() > rhsData.toDouble()
                        }
                        else -> return false
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
                        EType.LONG -> {
                            val lhsData = getDataAsLong(row, indexLhs)
                            val rhsData = getDataAsLong(row, indexRhs)
                            return lhsData < rhsData
                        }
                        EType.BIGINT -> {
                            val lhsData = getDataAsBigInt(row, indexLhs)
                            val rhsData = getDataAsBigInt(row, indexRhs)
                            return lhsData < rhsData
                        }
                        EType.DECIMAL -> {
                            val lhsData = getDataAsDecimal(row, indexLhs)
                            val rhsData = getDataAsDecimal(row, indexRhs)
                            return lhsData < rhsData
                        }
                        EType.NUM -> {
                            val lhsData = getDataAsNumber(row, indexLhs)
                            val rhsData = getDataAsNumber(row, indexRhs)
                            return lhsData.toDouble() > rhsData.toDouble()
                        }
                        else -> return false
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
                        EType.LONG -> {
                            val lhsData = getDataAsLong(row, indexLhs)
                            val rhsData = getDataAsLong(row, indexRhs)
                            return lhsData > rhsData
                        }
                        EType.BIGINT -> {
                            val lhsData = getDataAsBigInt(row, indexLhs)
                            val rhsData = getDataAsBigInt(row, indexRhs)
                            return lhsData > rhsData
                        }
                        EType.DECIMAL -> {
                            val lhsData = getDataAsDecimal(row, indexLhs)
                            val rhsData = getDataAsDecimal(row, indexRhs)
                            return lhsData > rhsData
                        }
                        EType.NUM -> {
                            val lhsData = getDataAsNumber(row, indexLhs)
                            val rhsData = getDataAsNumber(row, indexRhs)
                            return lhsData.toDouble() > rhsData.toDouble()
                        }
                        else -> return false
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
                        EType.LONG -> {
                            val lhsData = getDataAsLong(row, indexLhs)
                            val rhsData = getDataAsLong(row, indexRhs)
                            return lhsData != rhsData
                        }
                        EType.BIGINT -> {
                            val lhsData = getDataAsBigInt(row, indexLhs)
                            val rhsData = getDataAsBigInt(row, indexRhs)
                            return lhsData != rhsData
                        }
                        EType.DECIMAL -> {
                            val lhsData = getDataAsDecimal(row, indexLhs)
                            val rhsData = getDataAsDecimal(row, indexRhs)
                            return lhsData != rhsData
                        }
                        EType.NUM -> {
                            val lhsData = getDataAsNumber(row, indexLhs)
                            val rhsData = getDataAsNumber(row, indexRhs)
                            return lhsData.toDouble() > rhsData.toDouble()
                        }
                        else -> return false
                    }
                }
            }
            else -> { _: Row -> false }
        }
    }

    fun convertToFunctionIndexIndexNumeric(
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

                        EType.BOOL -> {
                            val lhsData = getDataAsBoolean(row, indexLhs)
                            val rhsData = getDataAsBoolean(row, indexRhs)
                            return lhsData == rhsData
                        }
                        EType.INT, EType.DOUBLE, EType.FLOAT,
                        EType.DECIMAL, EType.NUM, EType.BIGINT, EType.LONG -> {
                            val lhsData = getDataAsNumber(row, indexLhs)
                            val rhsData = getDataAsNumber(row, indexRhs)
                            return lhsData.toDouble() == rhsData.toDouble()
                        }
                        else -> return false
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
                        EType.BOOL -> {
                            val lhsData = getDataAsBoolean(row, indexLhs)
                            val rhsData = getDataAsBoolean(row, indexRhs)
                            return lhsData <= rhsData
                        }
                        EType.INT, EType.DOUBLE, EType.FLOAT,
                        EType.DECIMAL, EType.NUM, EType.BIGINT, EType.LONG -> {
                            val lhsData = getDataAsNumber(row, indexLhs)
                            val rhsData = getDataAsNumber(row, indexRhs)
                            return lhsData.toDouble() <= rhsData.toDouble()
                        }
                        else -> return false
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
                        EType.BOOL -> {
                            val lhsData = getDataAsBoolean(row, indexLhs)
                            val rhsData = getDataAsBoolean(row, indexRhs)
                            return lhsData >= rhsData
                        }
                        EType.INT, EType.DOUBLE, EType.FLOAT,
                        EType.DECIMAL, EType.NUM, EType.BIGINT, EType.LONG -> {
                            val lhsData = getDataAsNumber(row, indexLhs)
                            val rhsData = getDataAsNumber(row, indexRhs)
                            return lhsData.toDouble() >= rhsData.toDouble()
                        }
                        else -> return false
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

                        EType.BOOL -> {
                            val lhsData = getDataAsBoolean(row, indexLhs)
                            val rhsData = getDataAsBoolean(row, indexRhs)
                            return lhsData < rhsData
                        }
                        EType.INT, EType.DOUBLE, EType.FLOAT,
                        EType.DECIMAL, EType.NUM, EType.BIGINT, EType.LONG -> {
                            val lhsData = getDataAsNumber(row, indexLhs)
                            val rhsData = getDataAsNumber(row, indexRhs)
                            return lhsData.toDouble() < rhsData.toDouble()
                        }
                        else -> return false
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
                        EType.BOOL -> {
                            val lhsData = getDataAsBoolean(row, indexLhs)
                            val rhsData = getDataAsBoolean(row, indexRhs)
                            return lhsData > rhsData
                        }
                        EType.INT, EType.DOUBLE, EType.FLOAT,
                        EType.DECIMAL, EType.NUM, EType.BIGINT, EType.LONG -> {
                            val lhsData = getDataAsNumber(row, indexLhs)
                            val rhsData = getDataAsNumber(row, indexRhs)
                            return lhsData.toDouble() > rhsData.toDouble()
                        }
                        else -> return false
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

                        EType.BOOL -> {
                            val lhsData = getDataAsBoolean(row, indexLhs)
                            val rhsData = getDataAsBoolean(row, indexRhs)
                            return lhsData != rhsData
                        }
                        EType.INT, EType.DOUBLE, EType.FLOAT,
                        EType.DECIMAL, EType.NUM, EType.BIGINT, EType.LONG -> {
                            val lhsData = getDataAsNumber(row, indexLhs)
                            val rhsData = getDataAsNumber(row, indexRhs)
                            return lhsData.toDouble() != rhsData.toDouble()
                        }
                        else -> return false
                    }
                }
            }
            else -> { _: Row -> false }
        }
    }

    fun getColumnIndex(column: String): Int {
        return columnNames.indexOf(column)
    }

    fun getDataAsString(row: Row, index: Int): String {
        return row.data[index] as String
    }

    fun getDataAsInt(row: Row, index: Int): Int {
        return row.data[index] as Int
    }

    fun getDataAsBoolean(row: Row, index: Int): Boolean {
        return row.data[index] as Boolean
    }

    fun getDataAsDouble(row: Row, index: Int): Double {
        return row.data[index] as Double
    }

    fun getDataAsFloat(row: Row, index: Int): Float {
        return row.data[index] as Float
    }

    fun getDataAsLong(row: Row, index: Int): Long {
        return row.data[index] as Long
    }

    fun getDataAsBigInt(row: Row, index: Int): BigInteger {
        return row.data[index] as BigInteger
    }

    fun getDataAsDecimal(row: Row, index: Int): BigDecimal {
        return row.data[index] as BigDecimal
    }

    fun getDataAsNumber(row: Row, index: Int): Number {
        return row.data[index] as Number
    }

    override fun print() {

        println("Number of Columns=" + this.numberOfColumns)
        println("Number of Rows=" + this.rows.size)

        for (i in 0 until this.numberOfColumns) {
            System.out.print("<" + this.columnNames[i] + "> | ")
        }
        println()

        for (i in 0 until this.numberOfColumns) {
            System.out.print("<" + this.columnTypes[i] + "> | ")
        }
        println()

        for (i in 0 until this.rows.size) {
            val r = this.rows[i]
            val values = r.data

            for (j in values.indices) {
                print(values[j].toString() + " | ")
            }
            println(
                " [" + Instant.ofEpochMilli(r.startTime) + ", " + Instant.ofEpochMilli(r.endTime) + "]"
            )
        }
        println("Number of Rows=" + this.rows.size)

    }

    override fun coalesce() {
        for (row in rows){
            for (rowi in rows) {
                if (row == rowi && row.startTime == rowi.endTime){
                    rowi.endTime = row.startTime
                    row.startTime = rowi.endTime
                }
            }
        }
        rows = rows.distinct().toMutableList()
    }
}

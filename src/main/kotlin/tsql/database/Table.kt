package tsql.database

import tsql.ast.nodes.DataSourceI
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
            if (columnNames[i] === columnName){
                rows.map { row -> row.deleteColumn(i) }
                columnTypes.removeAt(i)
            }
        }
        columnNames = columnNames.filter { it !== columnName }
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
        if(this.columnNames.containsAll(columns)){
            val columnsToRemove = this.columnNames.minus(columns)
            this.removeColumns(columnsToRemove)
        } else {
            throw SemanticError("Invalid Columns Provided")
        }
    }
}

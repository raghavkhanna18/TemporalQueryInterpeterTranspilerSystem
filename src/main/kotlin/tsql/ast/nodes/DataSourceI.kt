package tsql.ast.nodes

import tsql.database.Table

interface DataSourceI : Cloneable{
    fun getData(): Table
    fun getDataSortedBy(attribute: String, asc: Boolean = true): Table {
        val table = getData()
        table.sort(attribute, asc)
        return table
    }

    fun project(columns: List<String>) {
    }

    fun sort(attribute: String, asc: Boolean = true) {
    }

    override fun clone(): Table
    fun print() {
    }
}

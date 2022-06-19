package tsql.ast.nodes

import tsql.ast.symbol_table.SymbolTable
import tsql.database.Table

interface DataSourceI : Cloneable, AstNodeI {
    fun getData(): DataSourceI
    fun getDataSortedBy(attribute: String, asc: Boolean = true): DataSourceI {
        val table = getData()
        table.sort(attribute, asc)
        return table
    }

    fun project(columns: List<String>) {
    }

    fun sort(attribute: String, asc: Boolean = true) {
    }

    override fun clone(): DataSourceI
    fun print() {
    }
    fun coalesce(){

    }
}

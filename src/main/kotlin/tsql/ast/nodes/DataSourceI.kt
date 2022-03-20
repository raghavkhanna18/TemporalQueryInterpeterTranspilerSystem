package tsql.ast.nodes

import tsql.database.Table

interface DataSourceI : Cloneable{
    fun getData() : Table
    fun project(columns: List<String>)
    override fun clone(): Table
}

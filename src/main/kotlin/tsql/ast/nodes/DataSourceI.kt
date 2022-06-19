package tsql.ast.nodes

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

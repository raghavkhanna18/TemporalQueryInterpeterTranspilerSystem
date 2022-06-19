package tsql.ast.symbol_table

class SymbolTable(val enclosure: SymbolTableInterface? = null) :
    SymbolTableInterface {
    override fun getColumnNamesForTable(tableName: String): List<String> {
        TODO("Not yet implemented")
    }

    override fun getOrCreateTableAlias(tableName: String): String {
        TODO("Not yet implemented")
    }

    override fun getTableNameFromAlias(alias: String): String {
        TODO("Not yet implemented")
    }
}

package tsql.ast.symbol_table

import tsql.ast.types.AbstractType

interface SymbolTableInterface {
    fun getColumnNamesForTable(tableName: String): List<String>
    fun getOrCreateTableAlias(tableName: String): String
    fun getTableNameFromAlias(alias: String): String
}

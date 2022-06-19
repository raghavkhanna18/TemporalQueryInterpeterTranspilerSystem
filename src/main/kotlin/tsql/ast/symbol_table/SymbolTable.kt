package tsql.ast.symbol_table

class SymbolTable(val enclosure: SymbolTableInterface? = null) :
    SymbolTableInterface {
    var aliasChar = 'Z'
    val tableNameToAlias = hashMapOf<String, String>()
    val aliasToTableName = hashMapOf<String, String>()
    val tableToColumnNames = hashMapOf<String, List<String>>()
    override fun getColumnNamesForTable(tableName: String): List<String> {
        return tableToColumnNames[tableName] ?: listOf()
    }

    override fun getOrCreateTableAlias(tableName: String): String {
        var alias = tableNameToAlias[tableName]
        if (alias == null) {
            alias  = (--aliasChar).toString()
            while (aliasToTableName[alias] == null){
                alias = (--aliasChar).toString()
            }
            updateMaps(tableName, alias!!)
        }
        return alias
    }
    fun updateMaps(tableName: String, alias: String): String {
        tableNameToAlias[tableName] = alias
        aliasToTableName[alias] = tableName
        return alias
    }
    fun createTableAlias(tableName: String, alias: String) : String {
        var extraAlias = 'A'
        var newAlias = alias
        if (aliasToTableName[newAlias] != null) {
            newAlias = alias + extraAlias
            while ( aliasToTableName[newAlias] != null) {
                extraAlias++
                newAlias= alias + extraAlias
            }
        }
        return updateMaps(tableName, alias)
    }
    override fun getTableNameFromAlias(alias: String): String {
        return aliasToTableName[alias] ?: ""
    }

    override fun getTableNames(): List<Pair<String, String>> {
        return tableNameToAlias.toList()
    }
}

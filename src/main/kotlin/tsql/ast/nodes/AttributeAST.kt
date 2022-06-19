package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTable
import tsql.ast.types.EType
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

open class AttributeAST(
    val value: String,
    val isLiteral: Boolean = false,
    var tableName: String = "",
    var rename: String = value,
    var type: EType = EType.STRING
) : AstNodeI,
    Visitable() {
    override val id: NodeId = AstNodeI.getId()
    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        queryInfo: SymbolTable
    ) {
        if (tableName != "" && !isLiteral) {
            var columnNames = queryInfo.tableToColumnNames[tableName]
            if (columnNames != null) {
                columnNames.toMutableList().add(value)
            } else {
                columnNames =  listOf(value)
            }
            queryInfo.tableToColumnNames[tableName] = columnNames

        } else if (tableName == ""){
            val tableNames = queryInfo.getTableNames()
            for (tName in tableNames) {
                var columnNames = queryInfo.tableToColumnNames[tName.first]
                if (columnNames != null) {
                    if (value in columnNames){
                        tableName = tName.first
                    }
                }
            }
            tableName = tableNames.first().first
        }
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        return dataSourceI
    }

    fun getColumnName(): String {
        if (tableName == "") return value else return "$tableName.$value"
    }

    override fun toSQL(symbolTable: SymbolTable?): Pair<String, Pair<String, String>> {
        val sql = if (rename != value) "${getColumnName()} AS $rename" else getColumnName()
        return Pair(sql, Pair("",""))
    }
}

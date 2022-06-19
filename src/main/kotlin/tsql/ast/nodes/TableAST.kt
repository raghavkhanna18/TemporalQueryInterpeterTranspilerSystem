package tsql.ast.nodes

import tsql.ast.symbol_table.SymbolTable
import tsql.database.Query
import tsql.database.Table
import tsql.error.SyntaxErrorListener

class TableAST(
    val name: String,
    var alias: String = ""
) : AstNodeI, DataSourceI {
    override val id: NodeId = AstNodeI.getId()
    var table: Table? = null

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        queryInfo: SymbolTable
    ) {
        alias = alias.removePrefix("'")
        alias = alias.removeSuffix("'")
        if (alias != "") {
            alias = queryInfo.createTableAlias(name, alias)
        } else {
            alias = queryInfo.getOrCreateTableAlias(name)
        }
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        val query = "SELECT * FROM $name;"
        this.table = Query.execQuery(query)
        this.table!!.name = if (alias != "") alias else name
        return this.table!!
    }

    override fun getDataSortedBy(attribute: String, asc: Boolean): Table {
        val direction = if (asc) "ASC" else "DESC"
        val query = "SELECT * FROM $name ORDER BY \"$attribute\" $direction, start_time $direction, end_time $direction;"
        this.table = Query.execQuery(query)
        this.table!!.name = if (alias != "") alias else name
        return this.table!!
    }

    override fun sort(attribute: String, asc: Boolean) {
        getDataSortedBy(attribute, asc)
    }

    override fun getData(): Table {
        if (this.table == null) {
            this.execute()
        }
        return this.table!!
    }

    override fun project(columns: List<String>) {
        TODO("Not yet implemented")
    }

    override fun clone(): Table {
        TODO("Not yet implemented")
    }

    override fun toSQL(symbolTable: SymbolTable?): Pair<String, Pair<String, String>> {
        if (alias != ""){
            return Pair("$name $alias", Pair("",""))
        }
        return Pair("$name", Pair("",""))
    }
}

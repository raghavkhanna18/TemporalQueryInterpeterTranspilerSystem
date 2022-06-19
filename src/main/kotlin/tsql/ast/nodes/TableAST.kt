package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.database.Query
import tsql.database.Table
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class TableAST(
    val name: String,
    val alias: String = ""
) : AstNodeI, Visitable(), DataSourceI {
    override val id: NodeId = AstNodeI.getId()
    var table: Table? = null

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        queryInfo: SymbolTableInterface
    ) {
        TODO("Not yet implemented")
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
}

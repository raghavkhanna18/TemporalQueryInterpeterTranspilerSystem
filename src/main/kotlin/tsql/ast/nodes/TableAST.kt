package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.database.Query
import tsql.database.Table
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class TableAST (
    val name: String,
    val alias: String = ""
)  : AstNode, Visitable(), DataSourceI {
    override val id: NodeId = AstNode.getId()
    var table: Table? = null

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        scope: SymbolTableInterface
    ) {
        TODO("Not yet implemented")
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        TODO("Not yet implemented")
    }

    fun exec() : DataSourceI {
        val query = "SELECT * FROM $name;"
        this.table = Query.execQuery(query)
        return this.table!!
    }

    override fun getData(): Table {
        if (this.table == null){
            this.exec()
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

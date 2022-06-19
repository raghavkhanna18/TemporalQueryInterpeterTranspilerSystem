package tsql.ast.nodes

import tsql.ast.symbol_table.SymbolTableInterface
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

typealias NodeId = Int

interface AstNodeI {
    val id: NodeId

    fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        queryInfo: SymbolTableInterface
    )

    fun execute(dataSourceI: DataSourceI? = null): DataSourceI?

    companion object {
        private var id = 0
        fun getId(): NodeId {
            return id++
        }
    }
}

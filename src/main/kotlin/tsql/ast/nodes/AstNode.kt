package tsql.ast.nodes

import tsql.ast.symbol_table.SymbolTableInterface
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

typealias NodeId = Int

interface AstNode {
    // Node's position in the source file.
    // val position: Pair<Pair<Int, Int>, Pair<Int, Int>>
    // Unique Integer ID of node.
    val id: NodeId

    fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        scope: SymbolTableInterface
    )

    companion object {
        private var id = 0
        fun getId(): NodeId {
            return id++
        }

        fun restId() {
            id = 0
        }
    }
}

package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.ast.types.AbstractType
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

abstract class AstExprNode : AstNodeI, Visitable() {
    lateinit var evaledType: AbstractType
    abstract val weight: Int

    abstract fun checkExprNode(
        scope: SymbolTableInterface,
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener
    ): AbstractType

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        queryInfo: SymbolTableInterface
    ) {
        checkExprNode(queryInfo, syntaxErrorListener, semanticErrorListener)
    }
}

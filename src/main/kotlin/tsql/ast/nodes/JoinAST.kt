package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.ast.types.JoinType
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class JoinAST(
    // override val position: Pair<Pair<Int, Int>, Pair<Int, Int>>,
    joinType: JoinType,
    left: AttributeAST,
    right: AttributeAST
) : AstNode, Visitable(), DataSourceI {
    override val id: NodeId = AstNode.getId()

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        scope: SymbolTableInterface
    ) {
        TODO("Not yet implemented")
    }
}

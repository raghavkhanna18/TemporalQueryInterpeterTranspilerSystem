package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.ast.types.EBinOp
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class WhereExpressionAST (
    // override val position: Pair<Pair<Int, Int>, Pair<Int, Int>> = Pair(Pair(0, 0), Pair(0, 0)),
    val lhs: AttributeAST,
    val rhs: AttributeAST,
    val comparator: ComparatorAST
)  : AstNode, Visitable() {
    override val id: NodeId = AstNode.getId()

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        scope: SymbolTableInterface
    ) {
        TODO("Not yet implemented")
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI {
        TODO("Not yet implemented")
    }
}

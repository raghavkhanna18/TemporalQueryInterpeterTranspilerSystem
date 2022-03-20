package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.ast.types.EBinOp
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class WhereOperationAST(
    // override val position: Pair<Pair<Int, Int>, Pair<Int, Int>> = Pair(Pair(0, 0), Pair(0, 0)),
    val lhs: WhereExpressionAST,
    val rhs: WhereOperationAST? = null,
    val conjuction: EBinOp? = null
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

    fun flatten(): Pair<MutableCollection<EBinOp>, MutableCollection<WhereExpressionAST>> {
        if (rhs != null && conjuction != null) {
            val flat_rhs = rhs.flatten()
            flat_rhs.first.add(conjuction)
            flat_rhs.second.add(lhs)
            return Pair(flat_rhs.first, flat_rhs.second)
        }
        return Pair(mutableListOf(), mutableListOf(lhs))
    }
}

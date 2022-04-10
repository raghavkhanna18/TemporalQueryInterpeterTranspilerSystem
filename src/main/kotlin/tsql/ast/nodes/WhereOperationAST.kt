package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.ast.types.EBinOp
import tsql.database.Condition
import tsql.database.Table
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

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        if (dataSourceI == null) {
            return dataSourceI
        }
        val conditions = this.flatten()
        when(dataSourceI){
            is Table -> {
                dataSourceI.filter(conditions)
            }
        }
        return dataSourceI
    }

    fun flatten(): Pair<MutableList<EBinOp>, MutableList<Condition>> {
        if (rhs != null && conjuction != null) {
            val flat_rhs = rhs.flatten()
            flat_rhs.first.add(conjuction)
            flat_rhs.second.add(lhs.toCondition())
            return Pair(flat_rhs.first, flat_rhs.second)
        }
        return Pair(mutableListOf(), mutableListOf(lhs.toCondition()))
    }
}

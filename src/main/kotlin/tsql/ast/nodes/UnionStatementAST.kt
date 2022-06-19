package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class UnionStatementAST(
    // override val position: Pair<Pair<Int, Int>, Pair<Int, Int>>,
    val statementAST: StatementAST
) : AstNodeI,
    Visitable() {
    override val id: NodeId = AstNodeI.getId()

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        queryInfo: SymbolTableInterface
    ) {
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        return statementAST.execute()
    }

    override fun toString(): String {
        return "coalesce statement $id"
    }
}

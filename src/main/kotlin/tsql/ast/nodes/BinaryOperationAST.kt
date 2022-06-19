package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.database.Table
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class BinaryOperationAST(
    // override val position: Pair<Pair<Int, Int>, Pair<Int, Int>> = Pair(Pair(0, 0), Pair(0, 0)),
    val operator: BinaryOperatorAST,
    val lhs: DataSourceI,
    val rhs: DataSourceI
) : AstNodeI, Visitable(), DataSourceI {
    override val id: NodeId = AstNodeI.getId()

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        queryInfo: SymbolTableInterface
    ) {
        TODO("Not yet implemented")
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        TODO("Not yet implemented")
    }

    override fun getData(): Table {
        TODO("Not yet implemented")
    }

    override fun project(columns: List<String>) {
        TODO("Not yet implemented")
    }

    override fun clone(): Table {
        TODO("Not yet implemented")
    }
}

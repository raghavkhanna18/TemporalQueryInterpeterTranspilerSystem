package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.ast.types.JoinType
import tsql.database.Table
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class JoinAST(
    // override val position: Pair<Pair<Int, Int>, Pair<Int, Int>>,
    val joinType: JoinType,
    val left: DataSourceI,
    val right: DataSourceI,
    val leftAttributeAST: AttributeAST,
    val rightAttributeAST: AttributeAST

) : AstNode, Visitable(), DataSourceI {
    override val id: NodeId = AstNode.getId()

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

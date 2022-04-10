package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class AttributesAST(
    // override val position: Pair<Pair<Int, Int>, Pair<Int, Int>>,
    val attributes: MutableCollection<AttributeAST>
) : AstNode,
    Visitable() {
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

    fun getColumnNames(): List<String> {
        return attributes.map { it.getColumnName()}
    }
}

package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.ast.types.EType
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

open class AttributeAST(
    override val position: Pair<Pair<Int, Int>, Pair<Int, Int>>,
    value: String,
    isLiteral: Boolean = false,
    tableName:String = "unknown",
    rename: String = value,
    type: EType = EType.STRING
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
}

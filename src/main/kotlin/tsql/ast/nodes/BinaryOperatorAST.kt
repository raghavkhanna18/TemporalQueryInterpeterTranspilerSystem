package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTable
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener


class BinaryOperatorAST(
    val operator: BinaryOperatorEnum
) : AstNodeI, Visitable() {
    override val id: NodeId = AstNodeI.getId()

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        queryInfo: SymbolTable
    ) {
        return
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
       return dataSourceI
    }
}

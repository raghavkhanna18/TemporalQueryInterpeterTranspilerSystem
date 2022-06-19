package tsql.ast.nodes

import tsql.ast.symbol_table.SymbolTable
import tsql.ast.types.BinaryOperatorEnum
import tsql.error.SyntaxErrorListener


class BinaryOperatorAST(
    val operator: BinaryOperatorEnum
) : AstNodeI {
    override val id: NodeId = AstNodeI.getId()

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        queryInfo: SymbolTable
    ) {
        return
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
       return dataSourceI
    }
}

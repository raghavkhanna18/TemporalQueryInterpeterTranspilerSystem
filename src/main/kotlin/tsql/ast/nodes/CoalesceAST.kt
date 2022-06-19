package tsql.ast.nodes

import tsql.ast.symbol_table.SymbolTable
import tsql.error.SyntaxErrorListener

class CoalesceAST : AstNodeI  {
    override val id: NodeId = AstNodeI.getId()
    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        queryInfo: SymbolTable
    ) {
        TODO("Not yet implemented")
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        TODO("Not yet implemented")
    }
}

package tsql.ast.nodes

import tsql.ast.symbol_table.SymbolTable
import tsql.ast.types.EBinOp
import tsql.error.SyntaxErrorListener

class ComparatorAST(
    val comparator: EBinOp
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

    override fun toSQL(symbolTable: SymbolTable?): Pair<String, Pair<String, String>> {
        return Pair(comparator.toString(), Pair("", ""))
    }
}

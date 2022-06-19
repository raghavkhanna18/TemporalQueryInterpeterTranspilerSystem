package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTable
import tsql.ast.types.EBinOp
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class ComparatorAST(
    val comparator: EBinOp
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

    override fun toSQL(symbolTable: SymbolTable?): Pair<String, Pair<String, String>> {
        return Pair(comparator.toString(), Pair("", ""))
    }
}

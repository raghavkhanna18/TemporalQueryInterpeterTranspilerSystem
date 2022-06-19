package tsql.ast.nodes

import tsql.ast.symbol_table.SymbolTableInterface
import tsql.ast.types.EType
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class LiteralValueAST (
    // position: Pair<Pair<Int, Int>, Pair<Int, Int>>,
    value: String, type: EType):
    AttributeAST(
        // position = position,
        value= value, isLiteral = true, type=type) {
    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        return super.execute(dataSourceI)
    }

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        queryInfo: SymbolTableInterface
    ) {
        super.checkNode(syntaxErrorListener, semanticErrorListener, queryInfo)
    }
}


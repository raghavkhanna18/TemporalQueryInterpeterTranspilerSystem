package tsql.error

import tsql.ast.nodes.AstExprNode

class SemanticErrorListener(private val errorAccumulator: ErrorAccumulator) {
    fun semanticError(src: AstExprNode, msg: String) {
        errorAccumulator.addError(SemanticError(src.position.first.first, src.position.first.second, msg))
    }
}

package tsql.ast.nodes.visitor

abstract class Visitable {
    fun <T : Any> accept(visitor: ASTvisitor<T>): T {
        when (this) {
        }
        throw Exception("Invalid visitor type")
    }
}

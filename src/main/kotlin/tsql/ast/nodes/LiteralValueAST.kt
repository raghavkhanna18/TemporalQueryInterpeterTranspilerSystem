package tsql.ast.nodes

import tsql.ast.types.EType

class LiteralValueAST(
    // position: Pair<Pair<Int, Int>, Pair<Int, Int>>,
    value: String, type: EType):
    AttributeAST(
        // position = position,
        value= value, isLiteral = true, type=type) {
}

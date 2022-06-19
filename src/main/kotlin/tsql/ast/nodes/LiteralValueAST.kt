package tsql.ast.nodes

import tsql.ast.types.EType

class LiteralValueAST (
    value: String, type: EType):
    AttributeAST (
        value= value, isLiteral = true, type=type) {
}


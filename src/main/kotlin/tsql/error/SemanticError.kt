package tsql.error

class SemanticError(
    msg: String
) : CompileError("Semantic Error", msg)

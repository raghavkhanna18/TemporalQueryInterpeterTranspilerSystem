package tsql.error

class SemanticError(
    line: Int,
    charPositionInLine: Int,
    msg: String
) : CompileError("Semantic Error", line, charPositionInLine, msg)

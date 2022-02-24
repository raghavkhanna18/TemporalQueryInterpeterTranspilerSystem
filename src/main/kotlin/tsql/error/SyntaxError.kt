package tsql.error

class SyntaxError(
    line: Int,
    charPositionInLine: Int,
    msg: String
) : CompileError("Syntax Error", line, charPositionInLine, msg)

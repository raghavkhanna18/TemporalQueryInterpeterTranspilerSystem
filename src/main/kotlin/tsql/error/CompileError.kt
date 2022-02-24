package tsql.error

open class CompileError(val errorType: String, val line: Int, val charPositionInLine: Int, val msg: String)

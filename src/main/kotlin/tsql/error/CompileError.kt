package tsql.error

open class CompileError(val errorType: String,
    val msg: String
    ) : Throwable(message = msg)

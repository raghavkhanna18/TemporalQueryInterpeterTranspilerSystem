package tsql.error

interface ErrorPrinter {
    fun printError(error: CompileError)
}

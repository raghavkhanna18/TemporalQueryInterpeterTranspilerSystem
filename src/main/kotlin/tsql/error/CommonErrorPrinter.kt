package tsql.error

class CommonErrorPrinter(val fileContent: Array<String>) : ErrorPrinter {
    override fun printError(error: CompileError) {
        println("${error.errorType}: ${error.msg}")
    }
}

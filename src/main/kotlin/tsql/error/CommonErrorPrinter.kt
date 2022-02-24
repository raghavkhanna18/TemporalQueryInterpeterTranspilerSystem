package tsql.error

class CommonErrorPrinter(val fileContent: Array<String>) : ErrorPrinter {
    override fun printError(error: CompileError) {
        println("${error.errorType} at ${error.line}, ${error.charPositionInLine}: ${error.msg}")

        // Put carrot at the correct position in the file.
        println(fileContent[error.line - 1])
        for (i in 1..error.charPositionInLine) {
            print(' ')
        }
        println('^')
        println()
    }
}

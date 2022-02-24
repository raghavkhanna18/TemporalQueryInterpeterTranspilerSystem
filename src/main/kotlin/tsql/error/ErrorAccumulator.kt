package tsql.error

import kotlin.system.exitProcess

class ErrorAccumulator(val exitCode: Int, val errorPrinter: ErrorPrinter) {
    private val errors = mutableListOf<CompileError>()

    fun addError(error: CompileError) {
        errors.add(error)
    }

    fun throwErrorsAndExit() {
        val errCode = throwErrorsAndReturnExitCode()
        if (errCode != 0) {
            exitProcess(errCode)
        }
    }

    fun throwErrorsAndReturnExitCode(): Int {
        if (errors.isNotEmpty()) {
            errors.sortedBy(CompileError::line)
                .forEach {
                errorPrinter.printError(it)
            }
            println("Exit code $exitCode")
            return exitCode
        }
        return 0
    }
}

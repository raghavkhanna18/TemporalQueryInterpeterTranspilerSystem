package tsql

data class ShellResult(val exitCode: Int, val output: String, val errorOutput: String)

object TestUtil {
    // fun parseExpression(inputString: String, inputInArray: Array<String> = arrayOf(inputString)): Int {
    //     val syntaxErrorAccumulator = ErrorAccumulator(SYNTAX_EXIT_CODE, CommonErrorPrinter(inputInArray))
    //     constructParser(syntaxErrorAccumulator, inputString).expression()
    //     return syntaxErrorAccumulator.throwErrorsAndReturnExitCode()
    // }
    //
    // private fun parseStatement(inputString: String, inputInArray: Array<String> = arrayOf(inputString)): Int {
    //     val syntaxErrorAccumulator = ErrorAccumulator(SYNTAX_EXIT_CODE, CommonErrorPrinter(inputInArray))
    //     constructParser(syntaxErrorAccumulator, inputString).statement()
    //     return syntaxErrorAccumulator.throwErrorsAndReturnExitCode()
    // }
    //
    // private fun parseProgram(inputString: String, inputInArray: Array<String> = arrayOf(inputString)): Int {
    //     val syntaxErrorAccumulator = ErrorAccumulator(SYNTAX_EXIT_CODE, CommonErrorPrinter(inputInArray))
    //     constructParser(syntaxErrorAccumulator, inputString).program()
    //     return syntaxErrorAccumulator.throwErrorsAndReturnExitCode()
    // }
    //
    // private fun parseProgramAndConstructAST(
    //     inputString: String,
    //     inputInArray: Array<String> = arrayOf(inputString)
    // ): Int {
    //     val syntaxErrorAccumulator = ErrorAccumulator(SYNTAX_EXIT_CODE, CommonErrorPrinter(inputInArray))
    //     val semanticErrorAccumulator = ErrorAccumulator(SEMANTIC_EXIT_CODE, CommonErrorPrinter(inputInArray))
    //     constructAndCreateAST(syntaxErrorAccumulator, semanticErrorAccumulator, inputString)
    //     return syntaxErrorAccumulator.throwErrorsAndReturnExitCode()
    // }
    //
    // // Indicates a file is a WACC file and is not excluded from the testing
    // private fun isWACCFileCheckable(file: File, exceptions: List<String>, only: List<String>) =
    //     file.isFile && file.extension == "tsql" && file.nameWithoutExtension !in exceptions &&
    //         (only.isEmpty() || file.nameWithoutExtension in only)
    //
    // // Gets the main body statement from a program file's contents
    // private fun getStatFromProg(programText: String) =
    //     programText.substringAfter("begin").substringBeforeLast("end").trim()
    //
    // // Assert that the WACC files in the given directory parse correctly
    // fun validParse(
    //     dir: String,
    //     exceptions: List<String> = emptyList(),
    //     only: List<String> = emptyList(),
    //     rule: String = "statement"
    // ) = File(dir).walk()
    //     // Filter out specific exceptions
    //     .filter { isWACCFileCheckable(it, exceptions, only) }.map {
    //         // Create a test per WACC file being checked
    //         dynamicParseTest(it, rule, SUCCESS_EXIT_CODE)
    //     }.toList()
    //
    // // Assert that the WACC files in the given directory cause an error when parsed
    // fun invalidParse(
    //     dir: String,
    //     exceptions: List<String> = emptyList(),
    //     only: List<String> = emptyList(),
    //     rule: String = "statement"
    // ) = File(dir).walk()
    //     // Filter out specific exceptions
    //     .filter { isWACCFileCheckable(it, exceptions, only) }.map {
    //         // Create a test per WACC file being checked
    //         dynamicParseTest(it, rule, SYNTAX_EXIT_CODE)
    //     }.toList()
    //
    // private fun dynamicParseTest(it: File, rule: String, expectedExitCode: Int): DynamicTest? {
    //     return DynamicTest.dynamicTest("parsing wacc file ${it.path} as a $rule returns exit code $expectedExitCode") {
    //         val programText = it.readText(Charsets.UTF_8)
    //
    //         println("WACC example source: $it")
    //         println("Rule to evaluate: $rule")
    //         println("Source code: $programText")
    //
    //         val fileContent = readFileToArray(it.absolutePath)
    //
    //         val exitCode = when (rule) {
    //             "statement" -> parseStatement(
    //                 getStatFromProg(programText),
    //                 fileContent
    //             )
    //             "program" -> parseProgram(programText, fileContent)
    //             "programDeep" -> parseProgramAndConstructAST(it.path, fileContent)
    //             else -> -1
    //         }
    //         assertEquals(expectedExitCode, exitCode)
    //     }
    // }
    //
    // fun validIntegrationTests(path: String) {
    //     val fileContent = readFileToArray(path)
    //
    //     val syntaxErrorAccumulator = ErrorAccumulator(SYNTAX_EXIT_CODE, CommonErrorPrinter(fileContent))
    //     val semanticErrorAccumulator = ErrorAccumulator(SEMANTIC_EXIT_CODE, CommonErrorPrinter(fileContent))
    //     constructAndCreateAST(
    //         syntaxErrorAccumulator,
    //         semanticErrorAccumulator, path
    //     )
    //     Assertions.assertEquals(SUCCESS_EXIT_CODE, syntaxErrorAccumulator.throwErrorsAndReturnExitCode())
    //     Assertions.assertEquals(SUCCESS_EXIT_CODE, semanticErrorAccumulator.throwErrorsAndReturnExitCode())
    // }
    //
    // fun invalidSyntacticIntegrationTests(path: String) {
    //     val fileContent = readFileToArray(path)
    //
    //     val syntaxErrorAccumulator = ErrorAccumulator(SYNTAX_EXIT_CODE, CommonErrorPrinter(fileContent))
    //     val semanticErrorAccumulator = ErrorAccumulator(SEMANTIC_EXIT_CODE, CommonErrorPrinter(fileContent))
    //     constructAndCreateAST(
    //         syntaxErrorAccumulator,
    //         semanticErrorAccumulator, path
    //     )
    //     Assertions.assertEquals(SYNTAX_EXIT_CODE, syntaxErrorAccumulator.throwErrorsAndReturnExitCode())
    // }
    //
    // fun invalidSemanticIntegrationTests(path: String) {
    //     val fileContent = readFileToArray(path)
    //
    //     val syntaxErrorAccumulator = ErrorAccumulator(SYNTAX_EXIT_CODE, CommonErrorPrinter(fileContent))
    //     val semanticErrorAccumulator = ErrorAccumulator(SEMANTIC_EXIT_CODE, CommonErrorPrinter(fileContent))
    //     constructAndCreateAST(
    //         syntaxErrorAccumulator,
    //         semanticErrorAccumulator, path
    //     )
    //     Assertions.assertEquals(SUCCESS_EXIT_CODE, syntaxErrorAccumulator.throwErrorsAndReturnExitCode())
    //     Assertions.assertEquals(SEMANTIC_EXIT_CODE, semanticErrorAccumulator.throwErrorsAndReturnExitCode())
    // }
    //
    // // Execute a shell command using a subprocess
    // private fun runShellCommand(
    //     command: String,
    //     currentDir: File = File("./"),
    //     inputDir: File = File("./testInput.txt")
    // ): ShellResult {
    //     val parts = command.split(" ")
    //     val proc = ProcessBuilder(*parts.toTypedArray())
    //         .directory(currentDir)
    //         .redirectInput(ProcessBuilder.Redirect.from(inputDir))
    //         .start()
    //
    //     proc.waitFor()
    //
    //     return ShellResult(
    //         exitCode = proc.exitValue(),
    //         output = proc.inputStream.bufferedReader().readText().trim(),
    //         errorOutput = proc.errorStream.bufferedReader().readText().trim()
    //     )
    // }
    //
    // private fun compileAndExecute(waccFilePath: String): ShellResult {
    //     val programFilename = File(waccFilePath).nameWithoutExtension
    //     val assemblyFilename = "$programFilename.s"
    //     val assemblyFile = File(assemblyFilename)
    //
    //     try {
    //         compile(waccFilePath = waccFilePath)
    //         return assembleAndExecute(programFilename)
    //     } finally {
    //         // Delete any test artifacts
    //         assemblyFile.delete()
    //     }
    // }
    //
    // private fun assembleAndExecute(programFilename: String): ShellResult {
    //     val assemblyFilename = "$programFilename.s"
    //
    //     val programFile = File(programFilename)
    //     if (!programFile.exists()) {
    //         programFile.createNewFile()
    //     }
    //
    //     try {
    //         val assembleCommand =
    //             "arm-linux-gnueabi-gcc -o $programFilename -mcpu=arm1176jzf-s -mtune=arm1176jzf-s $assemblyFilename"
    //         val assembleResult = runShellCommand(assembleCommand)
    //
    //         Assertions.assertEquals(
    //             SUCCESS_EXIT_CODE, assembleResult.exitCode,
    //             "Assembly did not complete successfully. \n" +
    //                 "Returned with exit code ${assembleResult.exitCode}. \n" +
    //                 "Error message: ${assembleResult.errorOutput} \n"
    //         )
    //
    //         val executeCommand = "qemu-arm -L /usr/arm-linux-gnueabi/ $programFilename"
    //         return runShellCommand(executeCommand)
    //     } finally {
    //         // Delete any test artifacts
    //         programFile.delete()
    //     }
    // }
    //
    // // Assert that the compiler's output is the same as the reference compiler's
    // private fun assertExecuteMatchesRef(waccFilePath: String) {
    //     println("Checking results for WACC file: $waccFilePath...")
    //
    //     val result = compileAndExecute(waccFilePath)
    //     println("Our compiler's output: \n${result.output}")
    //     println("================================================")
    //     println("Our compiler's exit code: ${result.exitCode}")
    //     println("================================================")
    //
    //     println()
    //
    //     val refOutput = refCompile(waccFilePath)
    //     println("Reference compiler's output: \n${refOutput.output}")
    //     println("================================================")
    //     println("Reference compiler's exit code: ${refOutput.exitCode}")
    //     println("================================================")
    //
    //     Assertions.assertEquals(
    //         refOutput.output, result.output,
    //         "Compiler output does not match reference compiler's"
    //     )
    //     Assertions.assertEquals(
    //         refOutput.exitCode, result.exitCode,
    //         "Compiler exit code does not match reference compiler's"
    //     )
    // }
    //
    // fun matchingExecution(
    //     dir: String,
    //     exceptions: List<String> = emptyList(),
    //     only: List<String> = emptyList()
    // ) = File(dir).walk()
    //     .filter { isWACCFileCheckable(it, exceptions, only) }.map {
    //         dynamicExecutionTest(it, TestUtil::assertExecuteMatchesRef)
    //     }.toList()
    //
    // private fun dynamicExecutionTest(it: File, func: (String) -> Unit): DynamicTest? {
    //     return DynamicTest.dynamicTest("Executing test") {
    //         val programText = it.readText(Charsets.UTF_8)
    //
    //         println("WACC example source: $it")
    //         println("Source code: $programText")
    //
    //         func(it.path)
    //     }
    // }
    //
    // // Run the reference compiler on a WACC file
    // private fun refCompile(waccFilePath: String): ShellResult {
    //     val barrier = "==========================================================="
    //
    //     // Regex patterns to extract data from reference compiler result
    //     val exitCodePattern = "The exit code is (\\d+).\\s*-- Finished\\s*".toRegex()
    //     val outputPattern = "$barrier\\n((?:\\s|.)*)?\\n$barrier\\s*".toRegex()
    //
    //     // Run the reference compiler
    //     val refCompileResult = runShellCommand("ruby wacc_examples/refCompile -x $waccFilePath")
    //
    //     // Get refCompile returning output for the given input
    //     val outputMatch = outputPattern.find(refCompileResult.output)
    //     val output = if (outputMatch == null) "" else outputMatch.groupValues[1]
    //
    //     // Get exit code either from the refCompile output or from its process
    //     val exitCodeResult = exitCodePattern.find(refCompileResult.output)
    //     val exitCode = if (exitCodeResult == null) refCompileResult.exitCode else exitCodeResult.groupValues[1].toInt()
    //
    //     return ShellResult(exitCode, output.trim(), refCompileResult.errorOutput)
    // }
}

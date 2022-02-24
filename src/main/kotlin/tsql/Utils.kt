package tsql

import tsql.Constants.SEMANTIC_EXIT_CODE
import tsql.Constants.SYNTAX_EXIT_CODE
import tsql.ast.constructAndCreateAST
import tsql.error.CommonErrorPrinter
import tsql.error.ErrorAccumulator
import java.io.BufferedReader
import java.io.FileReader
import java.util.ArrayList

fun compile(
    waccFilePath: String,
    compileJS: Boolean = false,
    writeAST: Boolean = false,
    compileARM: Boolean = true,
    verbose: Boolean = false
) {
    val fileContent = readFileToArray(waccFilePath)
    val syntaxErrorAccumulator = ErrorAccumulator(
        SYNTAX_EXIT_CODE,
        CommonErrorPrinter(fileContent)
    )
    val semanticErrorAccumulator = ErrorAccumulator(
        SEMANTIC_EXIT_CODE,
        CommonErrorPrinter(fileContent)
    )

    val absSynTree = constructAndCreateAST(
        syntaxErrorAccumulator,
        semanticErrorAccumulator,
        waccFilePath
    )
    syntaxErrorAccumulator.throwErrorsAndExit()
    semanticErrorAccumulator.throwErrorsAndExit()
}

fun readFileToArray(filename: String): Array<String> {
    val fileReader = FileReader(filename)
    val bufferedReader = BufferedReader(fileReader)
    val lines: MutableList<String> = ArrayList()
    var line: String?
    while (bufferedReader.readLine().also { line = it } != null) {
        if (line != null) {
            lines.add(line!!)
        }
    }
    bufferedReader.close()
    lines.add("")

    return lines.toTypedArray()
}

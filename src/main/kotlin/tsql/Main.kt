package tsql

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import tsql.ast.constructAndCreateAST
import tsql.error.CommonErrorPrinter
import tsql.error.ErrorAccumulator
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) = mainBody {
    // ArgParser(args).parseInto(::CompileArgs).run {
    //     val file = File(filepath)
    //
    //     if (!file.exists()) {
    //         println("Input file does not exist")
    //         exitProcess(1)
    //     }
    //
    //     compile(
    //         waccFilePath = filepath,
    //         compileJS = js,
    //         compileARM = arm || !(js || ast),
    //         writeAST = ast,
    //         verbose = verbose
    //     )
    // }
    val input = "SELECT t.a, t.b FROM t SINCE JOIN z ON t.a = z.b WHERE a = 1 and b < c at 2;"
    val syntaxErrorAccumulator = ErrorAccumulator(
        Constants.SYNTAX_EXIT_CODE,
        CommonErrorPrinter(arrayOf( input))
    )
    val semanticErrorAccumulator = ErrorAccumulator(
        Constants.SEMANTIC_EXIT_CODE,
        CommonErrorPrinter(arrayOf( input))
    )

    val absSynTree = constructAndCreateAST(
        syntaxErrorAccumulator,
        semanticErrorAccumulator,
        input
    )
}

class CompileArgs(parser: ArgParser) {
    val js by parser.flagging("enable js compilation")

    val arm by parser.flagging("enable arm assembly compilation")

    val ast by parser.flagging("enable AST writing")

    val filepath by parser.positional("source WACC path")

    val verbose by parser.flagging("enable verbose mode")
}

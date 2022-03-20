package tsql

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import tsql.ast.constructAndCreateAST
import tsql.error.CommonErrorPrinter
import tsql.error.ErrorAccumulator
fun main(args: Array<String>) = mainBody {
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

package tsql

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import tsql.ast.constructAndCreateAST
import tsql.error.CommonErrorPrinter
import tsql.error.ErrorAccumulator
fun main(args: Array<String>) = mainBody {
    val input =
        "SELECT * FROM accounts UNTIL JOIN movement on id = movement_id;"
    val syntaxErrorAccumulator = ErrorAccumulator(
        Constants.SYNTAX_EXIT_CODE,
        CommonErrorPrinter(arrayOf(input))
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

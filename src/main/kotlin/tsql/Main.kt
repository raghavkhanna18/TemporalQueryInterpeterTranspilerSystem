package tsql

import com.xenomachina.argparser.mainBody
import tsql.ast.constructAndCreateAST
import tsql.error.CommonErrorPrinter
import tsql.error.ErrorAccumulator
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) = mainBody {
    val st = "SELECT a.artist, a.rank, b.title, b.rank FROM  top_100_artists as 'a' UNTIL top_100_songs as 'b'  WHERE a.artist = 'BTS' OR (a.rank = 1 AND b.rank = 2);"
val time  = measureTimeMillis { val input =
    st
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
    println(time)
}

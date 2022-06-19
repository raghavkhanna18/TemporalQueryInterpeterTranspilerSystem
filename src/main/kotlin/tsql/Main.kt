package tsql

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import tsql.ast.constructAndCreateAST
import tsql.error.CommonErrorPrinter
import tsql.error.ErrorAccumulator
fun main(args: Array<String>) = mainBody {
    val input =
        "SELECT  * FROM  base_artists  UNTIL JOIN base_songs  on base_artists.artist = base_songs.artist where base_artists.rank != 1 and (base_artists.artist != 'BTS' or base_artists.artist = 'Adele');"
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

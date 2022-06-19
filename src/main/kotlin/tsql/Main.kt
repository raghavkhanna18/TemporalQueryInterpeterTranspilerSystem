package tsql

import com.xenomachina.argparser.mainBody
import tsql.ast.constructAndCreateAST
import tsql.error.CommonErrorPrinter
import tsql.error.ErrorAccumulator

fun main(args: Array<String>) = mainBody {
    val input = "SELECT a.artist, a.rank, b.title, b.rank FROM  top_100_artists as 'a' UNTIL top_100_songs as 'b'  WHERE a.artist = 'BTS' OR (a.rank = 1 AND b.rank = 2);"
    val syntaxErrorAccumulator = ErrorAccumulator(
        Constants.SYNTAX_EXIT_CODE,
        CommonErrorPrinter(arrayOf(input))
    )

    val absSynTree = constructAndCreateAST(
        syntaxErrorAccumulator,
        input
    )
    val table = absSynTree.execute()
    if (table != null){
        table.print()
    }
}

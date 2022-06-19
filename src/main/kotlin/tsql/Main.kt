package tsql

import com.xenomachina.argparser.mainBody
import tsql.ast.constructAndCreateAST
import tsql.ast.nodes.ProgramAST
import tsql.ast.symbol_table.SymbolTable
import tsql.database.Table
import tsql.error.CommonErrorPrinter
import tsql.error.ErrorAccumulator
import tsql.error.SyntaxErrorListener
import java.io.File

fun main(args: Array<String>) = mainBody {
    Main.main(args)
}

object Main {

    fun main(args: Array<String>) {
        val transpile = true
        val input =
            "SELECT a.artist, a.rank, b.title, b.rank FROM  top_100_artists as 'a' UNTIL JOIN top_100_songs as 'b' on a.artist = b.artist WHERE a.artist = 'BTS' ;"
        val syntaxErrorAccumulator = ErrorAccumulator(
            Constants.SYNTAX_EXIT_CODE,
            CommonErrorPrinter(arrayOf(input))
        )

        val (absSynTree, symbolTable) = constructAndCreateAST(
            syntaxErrorAccumulator,
            input
        )

        if (transpile) {
           print(transpile(absSynTree, symbolTable))
        } else {
            execute(absSynTree, symbolTable).print()
        }
    }

    fun transpile(absSynTree: ProgramAST, symbolTable: SymbolTable, file: File? = null): String {

        return absSynTree.toSQL(symbolTable).first
    }

    fun execute(absSynTree: ProgramAST, symbolTable: SymbolTable, file: File? = null): Table {
        return absSynTree.execute() as Table
    }
}

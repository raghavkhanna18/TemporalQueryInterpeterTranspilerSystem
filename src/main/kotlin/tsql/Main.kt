package tsql

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import tsql.ast.constructAndCreateAST
import tsql.ast.nodes.ProgramAST
import tsql.ast.symbol_table.SymbolTable
import tsql.database.Table
import tsql.error.CommonErrorPrinter
import tsql.error.ErrorAccumulator
import java.io.File

fun main(args: Array<String>) = mainBody {
    Main.main(args)
}

class ProgramArgs(parser: ArgParser) {
    val filepath by parser.storing(
        "-f", "--file",
        help = "filepath to print output to"
    ).default("")

    val transpile by parser.flagging(
        "-t", "--transpile",
        help = "Should transpiler be used"
    )
    val query by parser.positional("Query to be executed")
}

object Main {
    fun main(args: Array<String>) {
        ArgParser(args).parseInto(::ProgramArgs).run {
            val input = query
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
    }

    fun transpile(absSynTree: ProgramAST, symbolTable: SymbolTable, file: File? = null): String {
        return absSynTree.toSQL(symbolTable).first
    }

    fun execute(absSynTree: ProgramAST, symbolTable: SymbolTable, file: File? = null): Table {
        return absSynTree.execute() as Table
    }
}

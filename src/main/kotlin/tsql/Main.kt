package tsql

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import tsql.ast.constructAndCreateAST
import tsql.ast.nodes.ProgramAST
import tsql.ast.symbol_table.SymbolTable
import tsql.database.Query
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
        help = "Filepath to print output to"
    ).default("")

    val transpile by parser.flagging(
        "-t", "--transpile",
        help = "Should transpiler be used"
    )

    val sqlfilepath by parser.storing(
        "-s", "--sqlfile",
        help = "Filepath to print transpiled sql too. Should be used in conjunction with -t."
    ).default("")

    val execSQL by parser.flagging(
        "-e", "--executesql",
        help = "Option to execute transpiled SQL"
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
                var fileP: File? = null
                if (sqlfilepath != "") {
                    fileP = File(sqlfilepath)
                }
                var file: File? = null
                if (filepath != "") {
                    file = File(filepath)
                }
                print(transpile(absSynTree, symbolTable, fileP, execSQL, file))
            } else {
                var fileP: File? = null
                if (filepath != "") {
                    fileP = File(filepath)
                }
                execute(absSynTree, symbolTable, fileP).print()
            }
        }
    }

    fun transpile(
        absSynTree: ProgramAST,
        symbolTable: SymbolTable,
        sqlFile: File? = null,
        execSQl: Boolean = false,
        file: File? = null
    ): String {
        val sqlString = absSynTree.toSQL(symbolTable).first
        sqlFile?.writeText(sqlString)
        if (execSQl){
            val resultTable = Query.exec(sqlString)
            if (file != null){
                resultTable.toCSV(file)
                resultTable.print()
            }
        }
        return sqlString
    }

    fun execute(absSynTree: ProgramAST, symbolTable: SymbolTable, file: File? = null): Table {
        val resultTable = absSynTree.execute() as Table
        if (file != null) {
            resultTable.toCSV(file)
        }
        return resultTable
    }
}

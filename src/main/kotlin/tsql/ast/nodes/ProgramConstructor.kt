package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener
import java.lang.Exception

class ProgramConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<ProgramAST>() {
    // Construct the program ast
    override fun visitProgram(ctx: TSQLParser.ProgramContext): ProgramAST {
        lateinit var program: ProgramAST
        try {
            program = ProgramAST(
                // position = Pair(Pair(0, 0), Pair(ctx.EOF().symbol.line, ctx.EOF().symbol.charPositionInLine + ctx.EOF().symbol.text.length))
            )
        } catch (e: Exception) {
            program = ProgramAST()
        }
        println(ctx.toStringTree())
        // Attempt to construct each function
        for (statement in ctx.coalesce_statement()) {
            try {
                program.statementList.add(
                    statement.accept(CoalesceStatementConstructor(syntaxErrorListener)))
            } catch (e: Exception) {
                println(e)
            }
        }
        return program
    }
}

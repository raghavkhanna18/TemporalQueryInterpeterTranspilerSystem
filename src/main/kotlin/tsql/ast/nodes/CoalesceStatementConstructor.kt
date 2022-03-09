package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class CoalesceStatementConstructor(val syntaxErrorListener: SyntaxErrorListener) :
    TSQLParserBaseVisitor<CoalesceStatementAST>() {
    override fun visitCoalesce_statement(ctx: TSQLParser.Coalesce_statementContext): CoalesceStatementAST {
        val statt = ctx.statement()
        val statt_a = statt.accept(StatementConstructor(syntaxErrorListener))
        return CoalesceStatementAST(
            // Pair(
            //     Pair(ctx.start.line, ctx.start.charPositionInLine),
            //     Pair(
            //         ctx.stop.line,
            //         ctx.stop.charPositionInLine
            //     )
            // ),
            statt_a
        )
    }
}

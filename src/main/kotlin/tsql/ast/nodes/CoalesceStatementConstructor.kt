package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class CoalesceStatementConstructor(val syntaxErrorListener: SyntaxErrorListener) :
    TSQLParserBaseVisitor<CoalesceStatementAST>() {
    override fun visitCoalesce_statement(ctx: TSQLParser.Coalesce_statementContext): CoalesceStatementAST {

        return CoalesceStatementAST(
            Pair(
                Pair(ctx.COALESCE().symbol.line, ctx.COALESCE().symbol.charPositionInLine),
                Pair(
                    ctx.statement().stop.line,
                    ctx.statement().start.charPositionInLine
                )
            ),
            ctx.statement().accept(StatementConstructor(syntaxErrorListener))
        )
    }
}

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
                    ctx.statement().SCOL().symbol.line,
                    ctx.statement().SCOL().symbol.charPositionInLine + ctx.statement().SCOL().symbol.text.length
                )
            ),
            ctx.statement().accept(StatementConstructor(syntaxErrorListener))
        )
    }
}

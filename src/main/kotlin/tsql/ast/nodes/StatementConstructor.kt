package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class StatementConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<StatementAST>() {
    override fun visitStatement(ctx: TSQLParser.StatementContext): StatementAST {
        return StatementAST(
            Pair(
                Pair(
                    ctx.select_operator().SELECT_().symbol.line,
                    ctx.select_operator().SELECT_().symbol.charPositionInLine
                ),
                Pair(ctx.SCOL().symbol.line, ctx.SCOL().symbol.charPositionInLine + ctx.SCOL().text.length)
            ),
            ctx.select_operator().accept(SelectConstructor(syntaxErrorListener)),
            ctx.table().accept(TableConstructor(syntaxErrorListener)),
            ctx.binary_operation().accept(BinaryOperationConstructor(syntaxErrorListener)),
            ctx.where_operation().accept(WhereOperationConstructor(syntaxErrorListener)),
            ctx.modal_operation().accept(ModalOperationConstructor(syntaxErrorListener)),
            ctx.at_operation().accept(AtOperationConstructor(syntaxErrorListener))
        )
    }
}

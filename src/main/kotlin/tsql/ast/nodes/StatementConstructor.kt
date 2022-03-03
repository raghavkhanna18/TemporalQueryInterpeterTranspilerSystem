package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class StatementConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<StatementAST>() {

    override fun visitSelect_from_table(ctx: TSQLParser.Select_from_tableContext): StatementAST {
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
            ctx.where_operation().accept(WhereOperationConstructor(syntaxErrorListener)),
            ctx.modal_operation().accept(ModalOperationConstructor(syntaxErrorListener)),
            ctx.at_operation().accept(AtOperationConstructor(syntaxErrorListener))
        )
    }

    override fun visitSelect_from_bin_opn(ctx: TSQLParser.Select_from_bin_opnContext): StatementAST {
        return StatementAST(
            Pair(
                Pair(
                    ctx.select_operator().SELECT_().symbol.line,
                    ctx.select_operator().SELECT_().symbol.charPositionInLine
                ),
                Pair(ctx.SCOL().symbol.line, ctx.SCOL().symbol.charPositionInLine + ctx.SCOL().text.length)
            ),
            ctx.select_operator().accept(SelectConstructor(syntaxErrorListener)),
            ctx.binary_operation().accept(BinaryOperationConstructor(syntaxErrorListener)),
            ctx.where_operation().accept(WhereOperationConstructor(syntaxErrorListener)),
            ctx.modal_operation().accept(ModalOperationConstructor(syntaxErrorListener)),
            ctx.at_operation().accept(AtOperationConstructor(syntaxErrorListener))
        )
    }
}

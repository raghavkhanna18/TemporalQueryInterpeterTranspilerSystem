package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class StatementConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<StatementAST>() {

    override fun visitSelect_from_table(ctx: TSQLParser.Select_from_tableContext): StatementAST {
        println("select from table")
        val whereOperationAST = if (ctx.where_operation() != null) ctx.where_operation()
            .accept(WhereOperationConstructor(syntaxErrorListener)) else null
        val modalOperationAST = if (ctx.modal_operation() != null) ctx.modal_operation()
            .accept(ModalOperationConstructor(syntaxErrorListener)) else null
        val atOperationAST = if (ctx.at_operation() != null) ctx.at_operation()
            .accept(AtOperationConstructor(syntaxErrorListener)) else null
        return StatementAST(
            // Pair(
            //     Pair(
            //         ctx.select_operator().SELECT_().symbol.line,
            //         ctx.select_operator().SELECT_().symbol.charPositionInLine
            //     ),
            //     Pair(ctx.SCOL().symbol.line, ctx.SCOL().symbol.charPositionInLine + ctx.SCOL().text.length)
            // ),
            ctx.select_operator().accept(SelectConstructor(syntaxErrorListener)),
            ctx.table().accept(TableConstructor(syntaxErrorListener)),
            whereOperationAST = whereOperationAST,
            modalOperationAST = modalOperationAST,
            atOperationAST = atOperationAST
        )
    }

    // override fun visitSelect_from_bin_opn(ctx: TSQLParser.Select_from_bin_opnContext): StatementAST {
    //     return StatementAST(
    //         // Pair(
    //         //     Pair(
    //         //         ctx.select_operator().SELECT_().symbol.line,
    //         //         ctx.select_operator().SELECT_().symbol.charPositionInLine
    //         //     ),
    //         //     Pair(ctx.SCOL().symbol.line, ctx.SCOL().symbol.charPositionInLine + ctx.SCOL().text.length)
    //         // ),
    //         ctx.select_operator().accept(SelectConstructor(syntaxErrorListener)),
    //         ctx.binary_operation().accept(BinaryOperationConstructor(syntaxErrorListener)),
    //         ctx.where_operation().accept(WhereOperationConstructor(syntaxErrorListener)),
    //         ctx.modal_operation().accept(ModalOperationConstructor(syntaxErrorListener)),
    //         ctx.at_operation().accept(AtOperationConstructor(syntaxErrorListener))
    //     )
    // }
}

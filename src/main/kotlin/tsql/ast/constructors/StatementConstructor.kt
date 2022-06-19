package tsql.ast.constructors

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.ast.nodes.StatementAST
import tsql.error.SyntaxErrorListener

class StatementConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<StatementAST>() {

    override fun visitSelect_from_table(ctx: TSQLParser.Select_from_tableContext): StatementAST {
        val whereOperationAST = if (ctx.where_operation() != null) ctx.where_operation()
            .accept(WhereOperationConstructor(syntaxErrorListener)) else null
        val modalOperationAST = if (ctx.modal_operation() != null) ctx.modal_operation()
            .accept(ModalOperationConstructor(syntaxErrorListener)) else null
        val atOperationAST = if (ctx.at_operation() != null) ctx.at_operation()
            .accept(AtOperationConstructor(syntaxErrorListener)) else null
        return StatementAST(
            ctx.select_operator().accept(SelectConstructor(syntaxErrorListener)),
            ctx.table().accept(TableConstructor(syntaxErrorListener)),
            whereOperationAST = whereOperationAST,
            modalOperationAST = modalOperationAST,
            atOperationAST = atOperationAST
        )
    }

    override fun visitSelect_from_bin_opn(ctx: TSQLParser.Select_from_bin_opnContext): StatementAST {
        val whereOperationAST = if (ctx.where_operation() != null) ctx.where_operation()
            .accept(WhereOperationConstructor(syntaxErrorListener)) else null
        val modalOperationAST = if (ctx.modal_operation() != null) ctx.modal_operation()
            .accept(ModalOperationConstructor(syntaxErrorListener)) else null
        val atOperationAST = if (ctx.at_operation() != null) ctx.at_operation()
            .accept(AtOperationConstructor(syntaxErrorListener)) else null
        return StatementAST(
            ctx.select_operator().accept(SelectConstructor(syntaxErrorListener)),
            ctx.binary_operation().accept(BinaryOperationConstructor(syntaxErrorListener)),
            whereOperationAST = whereOperationAST,
            modalOperationAST = modalOperationAST,
            atOperationAST = atOperationAST
        )
    }

    override fun visitSelect_from_join(ctx: TSQLParser.Select_from_joinContext): StatementAST {
        val whereOperationAST = if (ctx.where_operation() != null) ctx.where_operation()
            .accept(WhereOperationConstructor(syntaxErrorListener)) else null
        val modalOperationAST = if (ctx.modal_operation() != null) ctx.modal_operation()
            .accept(ModalOperationConstructor(syntaxErrorListener)) else null
        val atOperationAST = if (ctx.at_operation() != null) ctx.at_operation()
            .accept(AtOperationConstructor(syntaxErrorListener)) else null
        return StatementAST(
            ctx.select_operator().accept(SelectConstructor(syntaxErrorListener)),
            ctx.join_operation().accept(JoinConstructor(syntaxErrorListener)),
            whereOperationAST = whereOperationAST,
            modalOperationAST = modalOperationAST,
            atOperationAST = atOperationAST
        )
    }

    override fun visitSelect_nested(ctx: TSQLParser.Select_nestedContext): StatementAST {
        // TODO: implement nested selects
        return super.visitSelect_nested(ctx)
    }
}

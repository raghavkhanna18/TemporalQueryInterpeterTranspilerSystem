package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class BinaryOperationConstructor(val syntaxErrorListener: SyntaxErrorListener) :
    TSQLParserBaseVisitor<BinaryOperationAST>() {
    override fun visitBinopn_table_with_table(ctx: TSQLParser.Binopn_table_with_tableContext): BinaryOperationAST {
        return BinaryOperationAST(
            // Pair(
            //     Pair(ctx.start.line, ctx.start.charPositionInLine),
            //     Pair(ctx.stop.line, ctx.stop.charPositionInLine)
            // ),
            operator = ctx.binary_operator().accept(BinaryOperatorConstructor(syntaxErrorListener)),
            lhs = ctx.table().first().accept(TableConstructor(syntaxErrorListener)),
            rhs = ctx.table().last().accept(TableConstructor(syntaxErrorListener))
        )
    }

    override fun visitBinopn_table_with_binopn(ctx: TSQLParser.Binopn_table_with_binopnContext): BinaryOperationAST {
        return BinaryOperationAST(
            // Pair(
            //     Pair(ctx.start.line, ctx.start.charPositionInLine),
            //     Pair(ctx.stop.line, ctx.stop.charPositionInLine)
            // ),
            operator = ctx.binary_operator().accept(BinaryOperatorConstructor(syntaxErrorListener)),
            lhs = ctx.table().accept(TableConstructor(syntaxErrorListener)),
            rhs = ctx.binary_operation().accept(TableConstructor(syntaxErrorListener))
        )
    }
}

package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class BinaryOperatorConstructor(val syntaxErrorListener: SyntaxErrorListener) :
    TSQLParserBaseVisitor<BinaryOperatorAST>() {
    // override fun visitBinop_join(ctx: TSQLParser.Binop_joinContext): BinaryOperatorAST {
    //     return BinaryOperatorAST(
    //         // Pair(
    //         //     Pair(ctx.start.line, ctx.start.charPositionInLine),
    //         //     Pair(ctx.stop.line, ctx.stop.charPositionInLine)
    //         // ),
    //         ctx.accept(JoinConstructor(syntaxErrorListener))
    //     )
    // }

    override fun visitBinop_times(ctx: TSQLParser.Binop_timesContext?): BinaryOperatorAST {
        return super.visitBinop_times(ctx)
    }

    override fun visitBinop_until(ctx: TSQLParser.Binop_untilContext?): BinaryOperatorAST {
        return super.visitBinop_until(ctx)
    }
}

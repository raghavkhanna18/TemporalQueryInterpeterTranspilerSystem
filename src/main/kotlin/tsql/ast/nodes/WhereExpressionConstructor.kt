package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class WhereExpressionConstructor(val syntaxErrorListener: SyntaxErrorListener) :
    TSQLParserBaseVisitor<WhereExpressionAST>() {

    override fun visitWhere_aca(ctx: TSQLParser.Where_acaContext?): WhereExpressionAST {
        val lhs = ctx!!.attribute().first().accept(AttributeConstructor(syntaxErrorListener))
        val rhs = ctx.attribute().last().accept(AttributeConstructor(syntaxErrorListener))
        val comparator = ctx.comparator().accept(ComparatorConstructor(syntaxErrorListener))
        return WhereExpressionAST(
            // Pair(
            //     Pair(ctx.start.line, ctx.start.charPositionInLine),
            //     Pair(ctx.stop.line, ctx.stop.charPositionInLine + ctx.stop.text.length)
            // ),
            lhs = lhs,
            rhs = rhs,
            comparator = comparator
        )
    }

    override fun visitWhere_acl(ctx: TSQLParser.Where_aclContext?): WhereExpressionAST {
        val lhs = ctx!!.attribute().accept(AttributeConstructor(syntaxErrorListener))
        val rhs = ctx.literal_value().accept(LiteralValueConstructor(syntaxErrorListener))
        val comparator = ctx.comparator().accept(ComparatorConstructor(syntaxErrorListener))
        return WhereExpressionAST(
            // Pair(
            //     Pair(ctx.start.line, ctx.start.charPositionInLine),
            //     Pair(ctx.stop.line, ctx.stop.charPositionInLine + ctx.stop.text.length)
            // ),
            lhs = lhs,
            rhs = rhs,
            comparator = comparator
        )    }

    override fun visitWhere_lca(ctx: TSQLParser.Where_lcaContext?): WhereExpressionAST {
        return super.visitWhere_lca(ctx)
    }

    override fun visitWhere_lcl(ctx: TSQLParser.Where_lclContext?): WhereExpressionAST {
        return super.visitWhere_lcl(ctx)
    }
}

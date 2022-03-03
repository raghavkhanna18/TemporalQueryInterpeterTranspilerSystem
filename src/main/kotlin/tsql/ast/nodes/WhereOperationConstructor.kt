package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class WhereOperationConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<WhereOperationAST>() {

    override fun visitWhere_op_and(ctx: TSQLParser.Where_op_andContext): WhereOperationAST {
        val whereExpressions : MutableList<WhereExpressionAST> = mutableListOf()
        for (expr in ctx.where_expresion()){
            whereExpressions.add(expr.accept(WhereExpressionConstructor(syntaxErrorListener)))
        }
        return WhereOperationAST(
            Pair(
                Pair(ctx.WHERE_().symbol.line, ctx.WHERE_().symbol.charPositionInLine),
                Pair(ctx.stop.line, ctx.stop.charPositionInLine + ctx.stop.text.length)
            ),
            whereExpressions
        )
    }
}

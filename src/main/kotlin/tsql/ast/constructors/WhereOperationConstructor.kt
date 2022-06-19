package tsql.ast.constructors

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.ast.nodes.WhereOperationAST
import tsql.ast.types.EBinOp
import tsql.error.SyntaxErrorListener

class WhereOperationConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<WhereOperationAST>() {

    override fun visitWhere_op_and(ctx: TSQLParser.Where_op_andContext): WhereOperationAST {

        return WhereOperationAST(
            lhs = ctx.where_expresion().accept(WhereExpressionConstructor(syntaxErrorListener)),
            rhs = ctx.where_operation().accept(WhereOperationConstructor(syntaxErrorListener)),
            conjuction = EBinOp.AND
        )
    }

    override fun visitWhere_op_or(ctx: TSQLParser.Where_op_orContext): WhereOperationAST {
        return WhereOperationAST(
            lhs = ctx.where_expresion().accept(WhereExpressionConstructor(syntaxErrorListener)),
            rhs = ctx.where_operation().accept(WhereOperationConstructor(syntaxErrorListener)),
            conjuction = EBinOp.OR
        )
    }

    override fun visitWhere_single(ctx: TSQLParser.Where_singleContext): WhereOperationAST {
        return WhereOperationAST(
            lhs = ctx.accept(WhereExpressionConstructor(syntaxErrorListener))
        )
    }

    override fun visitWhere_nested(ctx: TSQLParser.Where_nestedContext): WhereOperationAST {
        return WhereOperationAST(
            lhs = null,
            rhs = ctx.where_operation().accept(WhereOperationConstructor(syntaxErrorListener)),
            rhsNested = true
        )
    }
}

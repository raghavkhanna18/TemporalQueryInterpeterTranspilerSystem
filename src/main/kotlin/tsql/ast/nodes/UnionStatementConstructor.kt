package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class UnionStatementConstructor(val syntaxErrorListener: SyntaxErrorListener) :
    TSQLParserBaseVisitor<UnionStatementAST>() {
    override fun visitSingle_statement(ctx: TSQLParser.Single_statementContext): UnionStatementAST {
        val statt = ctx.statement()
        val statt_a = statt.accept(StatementConstructor(syntaxErrorListener))
        return UnionStatementAST(
            statt_a
        )
    }

    override fun visitStatement_to_union(ctx: TSQLParser.Statement_to_unionContext?): UnionStatementAST {
        // TODO: Implement Unions
        return super.visitStatement_to_union(ctx)
    }
}

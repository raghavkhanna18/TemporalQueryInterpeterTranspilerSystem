package tsql.ast.constructors

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.ast.nodes.JoinAST
import tsql.error.SyntaxErrorListener

class JoinConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<JoinAST>() {
    override fun visitJoin_operation(ctx: TSQLParser.Join_operationContext): JoinAST {
        return JoinAST(

            ctx.join_operator().accept(JoinOperatorConstructor(syntaxErrorListener)),
            ctx.table().first().accept(TableConstructor(syntaxErrorListener)),
            ctx.table().last().accept(TableConstructor(syntaxErrorListener)),
            ctx.attribute().first().accept(AttributeConstructor(syntaxErrorListener)),
            ctx.attribute().last().accept(AttributeConstructor(syntaxErrorListener))
        )
    }
}

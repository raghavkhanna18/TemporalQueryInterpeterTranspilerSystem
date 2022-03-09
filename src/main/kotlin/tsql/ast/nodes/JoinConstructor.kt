package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class JoinConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<JoinAST>() {
    override fun visitJoin_operation(ctx: TSQLParser.Join_operationContext): JoinAST {
        return JoinAST(
            // Pair(
            //     Pair(ctx.start.line, ctx.start.charPositionInLine), Pair(ctx.stop.line, ctx.stop.charPositionInLine)
            // ),
            ctx.join_operator().accept(JoinOperatorConstructor(syntaxErrorListener)),
            ctx.attribute().first().accept(AttributeConstructor(syntaxErrorListener)),
            ctx.attribute().last().accept(AttributeConstructor(syntaxErrorListener))
        )
    }
}

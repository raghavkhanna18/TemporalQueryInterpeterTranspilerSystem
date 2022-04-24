package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class AtOperationConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<AtOperationAST>() {
    override fun visitAt_operation(ctx: TSQLParser.At_operationContext): AtOperationAST {
        return AtOperationAST(ctx.literal_value().accept(LiteralValueConstructor(syntaxErrorListener)))
    }
}

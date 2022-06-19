package tsql.ast.constructors

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.ast.nodes.AtOperationAST
import tsql.error.SyntaxErrorListener

class AtOperationConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<AtOperationAST>() {
    override fun visitAt_operation(ctx: TSQLParser.At_operationContext): AtOperationAST {
        return AtOperationAST(ctx.literal_value().accept(LiteralValueConstructor(syntaxErrorListener)))
    }
}

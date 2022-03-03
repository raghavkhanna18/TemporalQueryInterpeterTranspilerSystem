package tsql.ast.nodes

import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class BinaryOperatorConstructor(val syntaxErrorListener: SyntaxErrorListener) :
    TSQLParserBaseVisitor<BinaryOperatorAST>() {
}

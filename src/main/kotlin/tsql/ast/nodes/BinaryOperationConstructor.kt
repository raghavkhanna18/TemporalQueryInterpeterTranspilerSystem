package tsql.ast.nodes

import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class BinaryOperationConstructor(syntaxErrorListener: SyntaxErrorListener) :
    TSQLParserBaseVisitor<BinaryOperationAST>() {

}

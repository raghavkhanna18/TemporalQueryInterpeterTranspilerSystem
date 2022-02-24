package tsql.ast.nodes

import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class AtOperationConstructor(syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<AtOperationAST>() {

}

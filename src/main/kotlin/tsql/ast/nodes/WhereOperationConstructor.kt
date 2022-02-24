package tsql.ast.nodes

import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class WhereOperationConstructor(syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<WhereOperationAST>() {

}

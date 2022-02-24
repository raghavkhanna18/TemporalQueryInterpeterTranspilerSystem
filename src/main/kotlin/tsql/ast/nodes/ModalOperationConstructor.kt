package tsql.ast.nodes

import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class ModalOperationConstructor(syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<ModalOperationAST>() {

}

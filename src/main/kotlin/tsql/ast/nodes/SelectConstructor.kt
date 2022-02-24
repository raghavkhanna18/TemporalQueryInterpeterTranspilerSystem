package tsql.ast.nodes

import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class SelectConstructor(syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<SelectAST>() {

}

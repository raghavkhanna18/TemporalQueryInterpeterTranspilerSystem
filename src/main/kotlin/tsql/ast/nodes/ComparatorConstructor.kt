package tsql.ast.nodes

import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class ComparatorConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<ComparatorAST>() {

}

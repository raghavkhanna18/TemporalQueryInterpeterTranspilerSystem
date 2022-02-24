package tsql.ast.nodes

import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class TableConstructor(syntaxErrorListener: SyntaxErrorListener): TSQLParserBaseVisitor<TableAST>() {

}

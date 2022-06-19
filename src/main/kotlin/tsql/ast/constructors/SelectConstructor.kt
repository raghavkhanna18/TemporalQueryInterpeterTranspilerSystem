package tsql.ast.constructors

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.ast.nodes.SelectAST
import tsql.error.SyntaxErrorListener

class SelectConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<SelectAST>() {
    override fun visitSelect_operator(ctx: TSQLParser.Select_operatorContext): SelectAST {

            return SelectAST(
                    ctx.attribute_list().accept(AttributesConstructor(syntaxErrorListener))
            )
    }
}

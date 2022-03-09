package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class SelectConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<SelectAST>() {
    override fun visitSelect_operator(ctx: TSQLParser.Select_operatorContext): SelectAST {

            return SelectAST(
                // Pair(
                //     Pair(ctx.SELECT_().symbol.line, ctx.SELECT_().symbol.charPositionInLine),
                //     Pair(
                //         ctx.attribute_list().stop.line,
                //         ctx.attribute_list().stop.charPositionInLine + ctx.attribute_list().stop.text.length
                //     )
                // ),
                    ctx.attribute_list().accept(AttributesConstructor(syntaxErrorListener))
            )
    }
}

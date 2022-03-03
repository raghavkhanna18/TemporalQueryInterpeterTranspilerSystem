package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class AttributesConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<AttributesAST>() {
    override fun visitAttribute_list(ctx: TSQLParser.Attribute_listContext): AttributesAST {
        val attributes = mutableListOf<AttributeAST>()

        for (attribute in ctx.attribute()) {
            attributes.add(
                attribute.accept(AttributeConstructor(syntaxErrorListener))
            )
        }
        return AttributesAST(
            Pair(
                Pair(ctx.start.line, ctx.start.charPositionInLine),
                Pair(ctx.stop.line, ctx.stop.charPositionInLine + ctx.stop.text.length)
            ),
            attributes
        )
    }
}

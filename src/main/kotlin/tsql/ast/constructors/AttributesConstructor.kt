package tsql.ast.constructors

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.ast.nodes.AttributeAST
import tsql.ast.nodes.AttributesAST
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
            attributes
        )
    }
}

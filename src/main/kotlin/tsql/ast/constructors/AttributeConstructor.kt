package tsql.ast.constructors

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.ast.nodes.AttributeAST
import tsql.error.SyntaxErrorListener

class AttributeConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<AttributeAST>() {
    override fun visitAttri_table_dot_ident(ctx: TSQLParser.Attri_table_dot_identContext): AttributeAST {
        return AttributeAST(
            value = ctx.IDENTIFIER().symbol.text,
            isLiteral = false,
            tableName = ctx.table_name().IDENTIFIER().symbol.text
        )
    }

    override fun visitAttri_table_dot_ident_as(ctx: TSQLParser.Attri_table_dot_ident_asContext): AttributeAST {
        return AttributeAST(
            value = ctx.IDENTIFIER().symbol.text,
            isLiteral = false,
            tableName = ctx.table_name().IDENTIFIER().symbol.text,
            rename = ctx.as_operation().STRING_LITERAL().symbol.text
        )
    }

    override fun visitAttri_table_dot_star(ctx: TSQLParser.Attri_table_dot_starContext): AttributeAST {
        return AttributeAST(
            value = ctx.STAR().symbol.text,
            isLiteral = false,
            tableName = ctx.table_name().IDENTIFIER().symbol.text
        )
    }

    override fun visitAttri_identifier(ctx: TSQLParser.Attri_identifierContext): AttributeAST {
        return AttributeAST(
            value = ctx.IDENTIFIER().symbol.text,
            isLiteral = false
        )
    }

    override fun visitAttri_star(ctx: TSQLParser.Attri_starContext): AttributeAST {
            return AttributeAST(
                value = ctx.STAR().symbol.text,
                isLiteral = false
            )
    }

    override fun visitAttrie_as(ctx: TSQLParser.Attrie_asContext): AttributeAST {
        return AttributeAST(
            value = ctx.IDENTIFIER().symbol.text,
            isLiteral = false,
            rename = ctx.as_operation().STRING_LITERAL().symbol.text
        )
    }
}

package tsql.ast.constructors

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.ast.nodes.TableAST
import tsql.error.SyntaxErrorListener

class TableConstructor(syntaxErrorListener: SyntaxErrorListener): TSQLParserBaseVisitor<TableAST>() {


    override fun visitTable_as(ctx: TSQLParser.Table_asContext): TableAST {

        return TableAST(
            ctx.table_name().IDENTIFIER().symbol.text,
            ctx.as_operation().STRING_LITERAL().symbol.text
        )
    }

    override fun visitTable_not_renamed(ctx: TSQLParser.Table_not_renamedContext): TableAST {

        return TableAST(
            ctx.table_name().IDENTIFIER().symbol.text,
            ctx.table_name().IDENTIFIER().symbol.text
        )
    }
}

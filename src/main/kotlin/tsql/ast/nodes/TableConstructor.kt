package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class TableConstructor(syntaxErrorListener: SyntaxErrorListener): TSQLParserBaseVisitor<TableAST>() {


    override fun visitTable(ctx: TSQLParser.TableContext): TableAST {

        return TableAST(
            Pair(
                Pair(
                    ctx.table_name().start.line,
                    ctx.table_name().start.charPositionInLine
                ),
                Pair(
                    ctx.table_name().stop.line,
                    ctx.table_name().stop.charPositionInLine + ctx.table_name().stop.text.length
                )
            ),
            ctx.as_operation().STRING_LITERAL().symbol.text
        )
    }
}

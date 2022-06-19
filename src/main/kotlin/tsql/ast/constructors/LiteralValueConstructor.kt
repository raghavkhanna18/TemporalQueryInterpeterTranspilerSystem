package tsql.ast.constructors

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.ast.nodes.LiteralValueAST
import tsql.ast.types.EType
import tsql.error.SyntaxErrorListener

class LiteralValueConstructor(syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<LiteralValueAST>() {

    override fun visitLit_string(ctx: TSQLParser.Lit_stringContext): LiteralValueAST {
        return LiteralValueAST(
            // Pair(
            //     Pair(ctx.STRING_LITERAL().symbol.line, ctx.STRING_LITERAL().symbol.charPositionInLine),
            //     Pair(ctx.stop.line, ctx.stop.charPositionInLine)
            // ),
            value = ctx.STRING_LITERAL().symbol.text.removeSurrounding("'"),
            type = EType.STRING
        )
    }

    override fun visitLit_num(ctx: TSQLParser.Lit_numContext): LiteralValueAST {
        return LiteralValueAST(
            // Pair(
            //     Pair(ctx.NUMERIC_LITERAL().symbol.line, ctx.NUMERIC_LITERAL().symbol.charPositionInLine),
            //     Pair(ctx.stop.line, ctx.stop.charPositionInLine)
            // ),
            value = ctx.NUMERIC_LITERAL().symbol.text,
            type = EType.NUM
        )
    }

    override fun visitLit_true(ctx: TSQLParser.Lit_trueContext): LiteralValueAST {
        return LiteralValueAST(
            // Pair(
            //     Pair(ctx.TRUE_().symbol.line, ctx.TRUE_().symbol.charPositionInLine),
            //     Pair(ctx.stop.line, ctx.stop.charPositionInLine)
            // ),
            value = ctx.TRUE_().symbol.text,
            type = EType.BOOL
        )
    }

    override fun visitLit_false(ctx: TSQLParser.Lit_falseContext): LiteralValueAST {
        return LiteralValueAST(
            // Pair(
            //     Pair(ctx.FALSE_().symbol.line, ctx.FALSE_().symbol.charPositionInLine),
            //     Pair(ctx.stop.line, ctx.stop.charPositionInLine)
            // ),
            value = ctx.FALSE_().symbol.text,
            type = EType.BOOL
        )
    }

    override fun visitLit_null(ctx: TSQLParser.Lit_nullContext): LiteralValueAST {
        return LiteralValueAST(
            // Pair(
            //     Pair(ctx.NULL_().symbol.line, ctx.NULL_().symbol.charPositionInLine),
            //     Pair(ctx.stop.line, ctx.stop.charPositionInLine)
            // ),
            value = ctx.NULL_().symbol.text,
            type = EType.NULL
        )
    }

    override fun visitLit_current_date(ctx: TSQLParser.Lit_current_dateContext): LiteralValueAST {
        return LiteralValueAST(
            // Pair(
            //     Pair(ctx.CURRENT_DATE_().symbol.line, ctx.CURRENT_DATE_().symbol.charPositionInLine),
            //     Pair(ctx.stop.line, ctx.stop.charPositionInLine)
            // ),
            value = ctx.CURRENT_DATE_().symbol.text,
            type = EType.DATE
        )
    }

    override fun visitLit_current_time(ctx: TSQLParser.Lit_current_timeContext): LiteralValueAST {
        return LiteralValueAST(
            // Pair(
            //     Pair(ctx.CURRENT_TIME_().symbol.line, ctx.CURRENT_TIME_().symbol.charPositionInLine),
            //     Pair(ctx.stop.line, ctx.stop.charPositionInLine)
            // ),
            value = ctx.CURRENT_TIME_().symbol.text,
            type = EType.DATETIME
        )
    }

    override fun visitLit_current_timestamp(ctx: TSQLParser.Lit_current_timestampContext): LiteralValueAST {
        return LiteralValueAST(
            // Pair(
            //     Pair(ctx.CURRENT_TIMESTAMP_().symbol.line, ctx.CURRENT_TIMESTAMP_().symbol.charPositionInLine),
            //     Pair(ctx.stop.line, ctx.stop.charPositionInLine)
            // ),
            value = ctx.CURRENT_TIMESTAMP_().symbol.text,
            type = EType.TIMESTAMP
        )
    }

    override fun visitLit_blob(ctx: TSQLParser.Lit_blobContext): LiteralValueAST {
        return LiteralValueAST(
            // Pair(
            //     Pair(ctx.BLOB_LITERAL().symbol.line, ctx.BLOB_LITERAL().symbol.charPositionInLine),
            //     Pair(ctx.stop.line, ctx.stop.charPositionInLine)
            // ),
            value = ctx.BLOB_LITERAL().symbol.text,
            type = EType.BLOB
        )
    }
}


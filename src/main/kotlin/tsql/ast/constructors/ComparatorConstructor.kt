package tsql.ast.constructors

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.ast.nodes.ComparatorAST
import tsql.ast.types.EBinOp
import tsql.error.SyntaxErrorListener

class ComparatorConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<ComparatorAST>() {
    override fun visitEq(ctx: TSQLParser.EqContext?): ComparatorAST {
        return ComparatorAST(EBinOp.EQUAL)
    }

    override fun visitEq_2(ctx: TSQLParser.Eq_2Context?): ComparatorAST {
        return ComparatorAST(EBinOp.EQUAL)
    }

    override fun visitGt_eq(ctx: TSQLParser.Gt_eqContext?): ComparatorAST {
        return ComparatorAST(EBinOp.GREATER_EQUAL)
    }

    override fun visitNot_eq_1(ctx: TSQLParser.Not_eq_1Context?): ComparatorAST {
        return ComparatorAST(EBinOp.NOT_EQUAL)
    }

    override fun visitNot_eq_2(ctx: TSQLParser.Not_eq_2Context?): ComparatorAST {
        return ComparatorAST(EBinOp.NOT_EQUAL)
    }

    override fun visitGt(ctx: TSQLParser.GtContext?): ComparatorAST {
        return ComparatorAST(EBinOp.GREATER)
    }

    override fun visitLt(ctx: TSQLParser.LtContext?): ComparatorAST {
        return ComparatorAST(EBinOp.LESS)
    }

    override fun visitLt_eq(ctx: TSQLParser.Lt_eqContext?): ComparatorAST {
        return ComparatorAST(EBinOp.LESS_EQUAL)
    }
}

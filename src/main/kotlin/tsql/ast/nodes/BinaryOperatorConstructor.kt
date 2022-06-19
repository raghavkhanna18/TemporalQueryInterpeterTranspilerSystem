package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class BinaryOperatorConstructor(val syntaxErrorListener: SyntaxErrorListener) :
    TSQLParserBaseVisitor<BinaryOperatorAST>() {

    override fun visitBinop_times(ctx: TSQLParser.Binop_timesContext): BinaryOperatorAST {
        return BinaryOperatorAST(operator = BinaryOperatorEnum.TIMES)
    }

    override fun visitBinop_until(ctx: TSQLParser.Binop_untilContext): BinaryOperatorAST {
        return BinaryOperatorAST(operator = BinaryOperatorEnum.UNTIL)
    }

    override fun visitBinop_since(ctx: TSQLParser.Binop_sinceContext?): BinaryOperatorAST {
        return BinaryOperatorAST(operator = BinaryOperatorEnum.SINCE)
    }
}

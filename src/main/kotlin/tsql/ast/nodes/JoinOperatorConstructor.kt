package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.ast.types.JoinType
import tsql.error.SyntaxErrorListener

class JoinOperatorConstructor(val syntaxErrorListener: SyntaxErrorListener): TSQLParserBaseVisitor<JoinType>() {
    override fun visitInner_join(ctx: TSQLParser.Inner_joinContext?): JoinType {
        return JoinType.INNER
    }

    override fun visitLeft_join(ctx: TSQLParser.Left_joinContext?): JoinType {
        return JoinType.LEFT
    }

    override fun visitCross_join(ctx: TSQLParser.Cross_joinContext?): JoinType {
        return JoinType.CROSS
    }

    override fun visitSince_join(ctx: TSQLParser.Since_joinContext?): JoinType {
        return JoinType.SINCE
    }

    override fun visitUntil_join(ctx: TSQLParser.Until_joinContext?): JoinType {
        return JoinType.UNTIL
    }

    override fun visitRight_join(ctx: TSQLParser.Right_joinContext?): JoinType {
        return JoinType.RIGHT
    }
}

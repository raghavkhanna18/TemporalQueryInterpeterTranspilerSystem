package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor

class CoalesceAST : TSQLParserBaseVisitor<CoalesceAST>() {
    // override fun visitCoalesce(ctx: TSQLParser.CoalesceContext?): CoalesceAST {
    //     return super.visitCoalesce(ctx)
    // }
}

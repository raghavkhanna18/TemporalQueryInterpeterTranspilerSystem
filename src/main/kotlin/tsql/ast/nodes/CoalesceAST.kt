package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class CoalesceAST : AstNode, Visitable() {
    override val id: NodeId = AstNode.getId()

    // override fun visitCoalesce(ctx: TSQLParser.CoalesceContext?): CoalesceAST {
    //     return super.visitCoalesce(ctx)
    // }
    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        scope: SymbolTableInterface
    ) {
        TODO("Not yet implemented")
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI {
        TODO("Not yet implemented")
    }
}

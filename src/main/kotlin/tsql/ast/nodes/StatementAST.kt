package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class StatementAST(
    // override val position: Pair<Pair<Int, Int>, Pair<Int, Int>>,
    selectAST: SelectAST,
    DataSourceAST: DataSourceI,
    whereOperationAST: WhereOperationAST?,
    modalOperationAST: ModalOperationAST?,
    atOperationAST: AtOperationAST?
) : AstNode,
    Visitable() {
    override val id: NodeId = AstNode.getId()

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        scope: SymbolTableInterface
    ) {
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "statement $id"
    }
}

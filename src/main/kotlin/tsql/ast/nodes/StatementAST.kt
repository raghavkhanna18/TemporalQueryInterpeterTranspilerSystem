package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class StatementAST(
    // override val position: Pair<Pair<Int, Int>, Pair<Int, Int>>,
    val selectAST: SelectAST,
    val dataSourceAST: AstNode,
    val whereOperationAST: WhereOperationAST?,
    val modalOperationAST: ModalOperationAST?,
    val atOperationAST: AtOperationAST?
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
        atOperationAST?.execute(dataSourceI)
        val baseData = dataSourceAST.execute()
        val modalData = if (modalOperationAST != null) modalOperationAST.execute(baseData) else baseData
        val filteredData = if (whereOperationAST != null) whereOperationAST.execute(modalData) else modalData
        return selectAST.execute(filteredData)
    }

    override fun toString(): String {
        return "statement $id"
    }
}

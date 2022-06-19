package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.ast.types.EBinOp
import tsql.database.Condition
import tsql.database.Table
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class WhereOperationAST(
    val lhs: WhereExpressionAST?,
    val rhs: WhereOperationAST? = null,
    val rhsNested: Boolean = false,
    val conjuction: EBinOp? = null
) : AstNodeI, Visitable() {
    override val id: NodeId = AstNodeI.getId()

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        queryInfo: SymbolTableInterface
    ) {
        TODO("Not yet implemented")
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {

        var dataSource = dataSourceI

        if (rhs != null && (rhsNested || rhs.rhsNested)) {
            dataSource = rhs.execute(dataSource)
        } else if (lhs == null && rhs != null) {
            dataSource = rhs.execute(dataSource)
            return dataSource
        }
        if (dataSource == null) {
            return dataSource
        }
        val conditions = this.flatten()
        conditions.second.map { updateConditionType(it, dataSource) }
        when (dataSource) {
            is Table -> {
                dataSource.filter(conditions)
            }
        }
        return dataSource
    }

    fun flatten(): Pair<MutableList<EBinOp>, MutableList<Condition>> {
        if (rhs != null && conjuction != null && !rhsNested && lhs != null) {
            val flat_rhs = rhs.flatten()
            flat_rhs.first.add(conjuction)
            flat_rhs.second.add(lhs.toCondition())
            return Pair(flat_rhs.first, flat_rhs.second)
        }
        return Pair(mutableListOf(), if (lhs != null) mutableListOf(lhs.toCondition()) else mutableListOf())
    }

    fun updateConditionType(condition: Condition, dataSource: DataSourceI) {
        if (!condition.lhsIsLiteral) {
            val data = dataSource.getData()
            val index = data.columnNames.indexOf(condition.lhs)
            val type = data.columnTypes[index]
            condition.lhsType = type
        }

        if (!condition.rhsIsLiteral) {
            val data = dataSource.getData()
            val index = data.columnNames.indexOf(condition.rhs)
            val type = data.columnTypes[index]
            condition.rhsType = type
        }
    }
}

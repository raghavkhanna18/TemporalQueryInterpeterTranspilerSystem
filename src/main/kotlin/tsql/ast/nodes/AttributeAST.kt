package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.ast.types.EType
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

open class AttributeAST(
    // override val position: Pair<Pair<Int, Int>, Pair<Int, Int>>,
    val value: String,
    val isLiteral: Boolean = false,
    val tableName: String = "",
    val rename: String = value,
    var type: EType = EType.STRING
) : AstNodeI,
    Visitable() {
    override val id: NodeId = AstNodeI.getId()
    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        queryInfo: SymbolTableInterface
    ) {
        TODO("Not yet implemented")
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        TODO("Not yet implemented")
    }

    fun getColumnName(): String {
        if (tableName == "") return value else return "$tableName.$value"
    }
}

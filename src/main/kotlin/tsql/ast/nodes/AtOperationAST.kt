package tsql.ast.nodes

import tsql.Utils
import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.ast.types.EType
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class AtOperationAST(
    val literalValueAST: LiteralValueAST
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
        val time = literalValueAST.value
        val type = literalValueAST.type
        Utils.CURRENT_TIME = convertLiteralValueToLong(time, type)
        return dataSourceI
    }

    fun convertLiteralValueToLong(literalValue : String, type: EType) : Long {
        return when(type){
            EType.DATE, EType.DATETIME -> { 0}
            EType.LONG, EType.INT, EType.BIGINT ->  {literalValue.toLong()}
            EType.DECIMAL, EType.FLOAT, EType.DOUBLE, EType.NUM -> {literalValue.toDouble().toLong()}
            else -> {
                0
            }
        }
    }
}

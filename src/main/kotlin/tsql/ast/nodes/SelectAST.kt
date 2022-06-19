package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTable
import tsql.error.SyntaxErrorListener
import java.lang.IllegalArgumentException

class SelectAST(
    val attributesAST: AttributesAST
) : AstNodeI, Visitable() {
    override val id: NodeId = AstNodeI.getId()

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        queryInfo: SymbolTable
    ) {
        attributesAST.checkNode(syntaxErrorListener, queryInfo)
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        if (dataSourceI != null) {
            dataSourceI.project(columns = getColumnNames())
            return dataSourceI
        } else {
            throw IllegalArgumentException("dataSource must not be null")
        }
    }

    fun getColumnNames() : List<String>{
        return attributesAST.getColumnNames()
    }

    override fun toSQL(symbolTable: SymbolTable?): Pair<String, Pair<String, String>> {
        val attributes  = attributesAST.toSQL(symbolTable)
        return Pair("SELECT ${attributes.first}", attributes.second)
    }
}

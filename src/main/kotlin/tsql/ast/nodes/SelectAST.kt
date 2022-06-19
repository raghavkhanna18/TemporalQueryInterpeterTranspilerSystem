package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTable
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener
import java.lang.IllegalArgumentException

class SelectAST(
    // override val position: Pair<Pair<Int, Int>, Pair<Int, Int>> = Pair(Pair(0, 0), Pair(0, 0)),
    val attributesAST: AttributesAST
) : AstNodeI, Visitable() {
    override val id: NodeId = AstNodeI.getId()

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        queryInfo: SymbolTable
    ) {
        attributesAST.checkNode(syntaxErrorListener, semanticErrorListener, queryInfo)
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

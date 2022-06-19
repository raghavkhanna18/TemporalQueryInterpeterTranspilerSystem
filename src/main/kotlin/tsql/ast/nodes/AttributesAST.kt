package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTable
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class AttributesAST(
    val attributes: MutableCollection<AttributeAST>
) : AstNodeI,
    Visitable() {
    override val id: NodeId = AstNodeI.getId()
    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        queryInfo: SymbolTable
    ) {
        for (atrr in attributes){
            atrr.checkNode(syntaxErrorListener, semanticErrorListener, queryInfo)
        }
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        for (atrr in attributes){
            atrr.execute(dataSourceI)
        }
        return dataSourceI
    }

    fun getColumnNames(): List<String> {
        return attributes.map { it.getColumnName()}
    }

    override fun toSQL(symbolTable: SymbolTable?): Pair<String, Pair<String, String>> {
        var attString = ""
        for (atribute in attributes) {
            val data = atribute.toSQL(symbolTable)
            val sql = "${data.first}, "
            attString += sql
        }
        attString.removeSuffix(", ")
        return Pair(attString, Pair("", ""))
    }
}

package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTable
import tsql.database.Condition
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class WhereExpressionAST(
    val lhs: AttributeAST,
    val rhs: AttributeAST,
    val comparator: ComparatorAST
) : AstNodeI, Visitable() {
    override val id: NodeId = AstNodeI.getId()

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        queryInfo: SymbolTable
    ) {
        return
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        return dataSourceI
    }

    fun toCondition(): Condition {
        return Condition(
            lhs.getColumnName(),
            lhs.type,
            lhs.isLiteral,
            comparator.comparator,
            rhs.getColumnName(),
            rhs.type,
            rhs.isLiteral
        )
    }

    override fun toSQL(symbolTable: SymbolTable?): Pair<String, Pair<String, String>> {
        var lhsString = lhs.getColumnName()
        var rhsString = rhs.getColumnName()
        if (lhs.isLiteral && !lhs.type.isNumeric()){
            lhsString = "'$lhsString'"
        }
        if (rhs.isLiteral && !rhs.type.isNumeric()){
            rhsString = "'$rhsString'"

        }
        return Pair("$lhsString  ${comparator.toSQL(symbolTable).first} $rhsString", Pair("", ""))
    }
}

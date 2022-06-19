package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTable
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class ProgramAST(
    override val id: NodeId = AstNodeI.getId()
) : AstNodeI, Visitable() {
    // Top level symbol table - holds functions
    val statementList = ArrayList<UnionStatementAST>()
    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        queryInfo: SymbolTable
    ) {
        queryInfo
        // Validate all functions are correct
        for (statement in statementList) {
            statement.checkNode(syntaxErrorListener, semanticErrorListener, queryInfo)
        }
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        for (statement in statementList) {
            return statement.execute()
        }
        return null
    }

    override fun toString(): String {
        var str = ""
        for (statement in statementList) {
            str += statement.toString() + "\n"
        }
        return str
    }

    override fun toSQL(symbolTable: SymbolTable?): Pair<String, Pair<String, String>> {
        for (statement in statementList) {
            return statement.toSQL(symbolTable)
        }
        return Pair("",Pair("",""))
    }
}

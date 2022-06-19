package tsql.ast.nodes

import tsql.ast.symbol_table.SymbolTable
import tsql.error.SyntaxErrorListener

class ProgramAST(
    override val id: NodeId = AstNodeI.getId()
) : AstNodeI {
    // Top level symbol table - holds functions
    val statementList = ArrayList<UnionStatementAST>()
    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        queryInfo: SymbolTable
    ) {
        queryInfo
        // Validate all functions are correct
        for (statement in statementList) {
            statement.checkNode(syntaxErrorListener, queryInfo)
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

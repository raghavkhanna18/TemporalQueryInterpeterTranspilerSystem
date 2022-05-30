package tsql.ast.nodes

import tsql.ast.nodes.visitor.Visitable
import tsql.ast.symbol_table.SymbolTableInterface
import tsql.ast.symbol_table.TopLevelSymbolTable
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

class ProgramAST(
    override val id: NodeId = AstNode.getId()
    // override val position: Pair<Pair<Int, Int>, Pair<Int, Int>> = Pair(Pair(0, 0), Pair(0, 0))
) : AstNode, Visitable() {
    // Top level symbol table - holds functions
    val statementList = ArrayList<CoalesceStatementAST>()
    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        semanticErrorListener: SemanticErrorListener,
        scope: SymbolTableInterface
    ) {
        scope as TopLevelSymbolTable
        // Validate all functions are correct
        for (statement in statementList) {
            statement.execute()
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
}

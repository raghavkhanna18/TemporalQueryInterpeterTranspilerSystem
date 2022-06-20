package tsql.ast.nodes

import tsql.ast.symbol_table.SymbolTable
import tsql.database.Table
import tsql.error.CompileError
import tsql.error.SyntaxErrorListener

class UnionStatementAST(
    val statementA: StatementAST,
    val statementB: StatementAST? = null
) : AstNodeI {
    override val id: NodeId = AstNodeI.getId()

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        queryInfo: SymbolTable
    ) {
        statementA.checkNode(syntaxErrorListener, queryInfo)
        statementB?.checkNode(syntaxErrorListener, queryInfo)
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        val tableA = statementA.execute()
        if (statementB != null){
            val tableB = statementB.execute()
            val combinedTable = Table()
            if (tableA is  Table && tableB is Table){
                if (tableA.columnNames == tableB.columnNames){
                    combinedTable.columnNames = tableA.columnNames
                    combinedTable.numberOfColumns = tableA.numberOfColumns
                    combinedTable.columnTypes = tableA.columnTypes
                    combinedTable.name = "${tableA.name}_union_${tableB.name}"
                    combinedTable.rows = tableA.rows
                    combinedTable.rows.addAll(tableB.rows)
                    combinedTable.coalesce()
                    return combinedTable
                }
                throw CompileError("Semantic","Incompatible columns in Union")
            }
            throw CompileError("Semantic","Incompatible columns in Union")
        }
        return tableA
    }

    override fun toString(): String {
        return "union statement $id"
    }


    override fun toSQL(symbolTable: SymbolTable?): Pair<String, Pair<String, String>> {
        val statementAData = statementA.toSQL(symbolTable)
        val statementAString = statementAData.first
        if (statementB != null ){
            val statementBData = statementB.toSQL(symbolTable)
            val statementBString = statementBData.first
            Pair("($statementAString) UNION ($statementBString)", Pair("",""))
        }
        return Pair("$statementAString;", Pair("",""))

    }
}

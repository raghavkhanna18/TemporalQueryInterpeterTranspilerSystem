package tsql.ast.nodes

import tsql.ast.symbol_table.SymbolTable
import tsql.error.SyntaxErrorListener

class StatementAST(
    val selectAST: SelectAST,
    val dataSourceAST: AstNodeI,
    val whereOperationAST: WhereOperationAST?,
    val modalOperationAST: ModalOperationAST?,
    val atOperationAST: AtOperationAST?
) : AstNodeI {
    override val id: NodeId = AstNodeI.getId()

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        queryInfo: SymbolTable
    ) {
        dataSourceAST.checkNode(syntaxErrorListener, queryInfo)
        modalOperationAST?.checkNode(syntaxErrorListener, queryInfo)
        whereOperationAST?.checkNode(syntaxErrorListener, queryInfo)
        selectAST.checkNode(syntaxErrorListener, queryInfo)
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        atOperationAST?.execute(dataSourceI)
        val baseData = dataSourceAST.execute()
        val modalData = if (modalOperationAST != null) modalOperationAST.execute(baseData) else baseData
        val filteredData = if (whereOperationAST != null) whereOperationAST.execute(modalData) else modalData
        val projectedData =  selectAST.execute(filteredData)
            projectedData!!.coalesce()
        return projectedData
    }

    override fun toString(): String {
        return "statement $id"
    }

    override fun toSQL(symbolTable: SymbolTable?): Pair<String, Pair<String, String>> {
        var select = selectAST.toSQL(symbolTable)
        var data = dataSourceAST.toSQL(symbolTable)
        var where = whereOperationAST?.toSQL(symbolTable)
        var modal = modalOperationAST?.toSQL(symbolTable)
        var atOp = atOperationAST?.toSQL(symbolTable)
        var whereClause = where?.first ?: ""
        whereClause += data.second.first
        whereClause += modal?.second?.first ?: ""
        whereClause += atOp?.second?.first ?: ""
        whereClause = whereClause.removePrefix("AND")
        whereClause = whereClause.removePrefix("OR")
        whereClause = "WHERE $whereClause"
        var selectClause = select.first
        selectClause += data.second.second
        selectClause += modal?.second?.second ?: ""
        selectClause += atOp?.second?.second ?: ""
        selectClause = selectClause.trim()
        selectClause = selectClause.removeSuffix(",")
        selectClause += " "
        var combined = "$selectClause FROM ${data.first} $whereClause ${modal?.first ?: ""} ${atOp?.first ?: ""}"
        combined = combined.trim()
        return Pair(combined, Pair("",""))
    }
}

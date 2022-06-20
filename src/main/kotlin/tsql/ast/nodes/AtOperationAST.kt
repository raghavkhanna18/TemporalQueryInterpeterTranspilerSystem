package tsql.ast.nodes

import tsql.Utils
import tsql.ast.symbol_table.SymbolTable
import tsql.ast.types.EType
import tsql.error.SyntaxErrorListener
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeParseException

class AtOperationAST(
    val literalValueAST: LiteralValueAST
) : AstNodeI {
    override val id: NodeId = AstNodeI.getId()
    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        queryInfo: SymbolTable
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
        return when (type) {
            EType.DATE, EType.DATETIME, EType.STRING -> {
                try {
                    val parsedDate = LocalDate.parse(literalValue)
                    return parsedDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
                } catch (e: DateTimeParseException) {
                    try {
                        val parsedDateTime = LocalDateTime.parse(literalValue)
                        return parsedDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
                    } catch (e: DateTimeParseException) {

                    }
                }
                return Instant.now().toEpochMilli()
            }
            EType.LONG, EType.INT, EType.BIGINT -> {
                literalValue.toLong()
            }
            EType.DECIMAL, EType.FLOAT, EType.DOUBLE, EType.NUM -> {
                literalValue.toDouble().toLong()
            }
            else -> {
                0
            }
        }
    }

    override fun toSQL(symbolTable: SymbolTable?): Pair<String, Pair<String, String>> {
        val tableNames = symbolTable?.getTableNames() ?: mutableListOf()
        val combinedString = ""
        for ((name, alias) in tableNames){
            var tableName = name
            if (alias != ""){
                tableName = alias
            }
            combinedString.plus("AND ${tableName}.start_time <= ${literalValueAST.toSQL()} ${tableName}.end_time > ${literalValueAST.toSQL()}")
        }
        return Pair(combinedString, Pair(combinedString, ""))
    }
}

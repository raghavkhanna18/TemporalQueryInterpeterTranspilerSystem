package tsql.ast.nodes

import tsql.Utils.MIN_TIME
import tsql.Utils.MAX_TIME
import tsql.Utils.TIME_UNITS
import tsql.Utils.CURRENT_TIME
import tsql.ast.symbol_table.SymbolTable
import tsql.ast.types.EModalOperation
import tsql.database.Row
import tsql.database.Table
import tsql.decrementTime
import tsql.error.SyntaxErrorListener
import tsql.incrementTime
import kotlin.math.min

class ModalOperationAST(
    val operation: EModalOperation
) : AstNodeI {
    override val id: NodeId = AstNodeI.getId()

    override fun checkNode(
        syntaxErrorListener: SyntaxErrorListener,
        queryInfo: SymbolTable
    ) {
        TODO("Not yet implemented")
    }

    override fun execute(dataSourceI: DataSourceI?): DataSourceI? {
        if (dataSourceI != null && dataSourceI is Table) {
            when (operation) {
                EModalOperation.ALWAYS_PAST -> filterForAlwaysPast(dataSourceI.getData().rows)
                EModalOperation.ALWAYS_FUTURE -> filterForAlwaysFuture(dataSourceI.getData().rows)
                EModalOperation.PAST -> filterForPast(dataSourceI.getData().rows)
                EModalOperation.FUTURE -> filterForFuture(dataSourceI.getData().rows)
                EModalOperation.NEXT -> filterForNext(dataSourceI.getData().rows)
                EModalOperation.PREVIOUS -> filterForPrevious(dataSourceI.getData().rows)
            }
        }
        return dataSourceI
    }

    fun filterForAlwaysPast(rows: MutableList<Row>) {
        rows.filter { row: Row -> row.startTime == MIN_TIME && row.endTime >= CURRENT_TIME }
            .map { row: Row ->
                {
                    row.startTime = min(incrementTime(row.startTime, TIME_UNITS), MAX_TIME)
                    row.endTime = min(incrementTime(row.endTime, TIME_UNITS), MAX_TIME)
                }
            }
    }

    fun filterForAlwaysFuture(rows: MutableList<Row>) {
        rows.filter { row: Row -> row.endTime == MAX_TIME && row.startTime <= CURRENT_TIME }
            .map { row: Row ->
                {
                    row.startTime = min(decrementTime(row.startTime, TIME_UNITS), MIN_TIME)
                    row.endTime = min(decrementTime(row.endTime, TIME_UNITS), MIN_TIME)
                }
            }
    }

    fun filterForPast(rows: MutableList<Row>) {
        rows.filter { row: Row -> row.startTime <= CURRENT_TIME }
            .map { row: Row ->
                {
                    row.startTime = min(incrementTime(row.startTime, TIME_UNITS), MIN_TIME)
                    row.endTime = min(incrementTime(row.endTime, TIME_UNITS), MIN_TIME)
                }
            }
    }

    fun filterForFuture(rows: MutableList<Row>) {
        rows.filter { row: Row -> row.endTime >= CURRENT_TIME }
            .map { row: Row ->
                {
                    row.startTime = min(decrementTime(row.startTime, TIME_UNITS), MIN_TIME)
                    row.endTime = min(decrementTime(row.endTime, TIME_UNITS), MIN_TIME)
                }
            }
    }

    fun filterForNext(rows: MutableList<Row>) {
        // all time points where row held in next time point
        rows.filter { row: Row -> row.startTime != MAX_TIME && row.endTime != MAX_TIME }
            .map { row: Row ->
                {
                    row.startTime = min(decrementTime(row.startTime, TIME_UNITS), MIN_TIME)
                    row.endTime = min(decrementTime(row.endTime, TIME_UNITS), MIN_TIME)
                }
            }
    }

    fun filterForPrevious(rows: MutableList<Row>) {
        // all time points where row held in next time point
        rows.filter { row: Row -> row.startTime != MIN_TIME && row.endTime != MIN_TIME }
            .map { row: Row ->
                {
                    row.startTime = min(incrementTime(row.startTime, TIME_UNITS), MIN_TIME)
                    row.endTime = min(incrementTime(row.endTime, TIME_UNITS), MIN_TIME)
                }
            }
    }
}

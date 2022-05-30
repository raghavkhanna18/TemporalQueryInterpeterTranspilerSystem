package tsql.database

import tsql.ast.types.EType
import tsql.ast.types.JDBCTypes
import java.sql.Connection
import java.sql.DriverManager
import java.sql.JDBCType
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException

object Query {
    val driverClass: String = "postgres"
    var url = "jdbc:postgresql://localhost/test"
    var username = "tsql_test"
    var password = "test"
    var startTimePos = 99
    var endTimePos = 99
    var connection: Connection

    init {
        connection = DriverManager.getConnection(url, username, password)
    }

    fun execQuery(qry: String): Table {
        val table = Table()
        try {
            /* execute the query */
            val stmt = connection.createStatement()
            val rs = stmt.executeQuery(qry)
            val meta = rs.metaData

            extractColumns(table, meta)
            extractRows(table, rs)
        } catch (e: SQLException) {
            println("SQL error ${e.message}")
        }
        return table
    }

    fun close() {
        connection.close()
    }

    private fun extractColumns(table: Table, meta: ResultSetMetaData) {
        val colNumber = getNumberOfColumns(meta)
        val colNames = extractNamesOfColumns(meta, colNumber)
        val colTypes = extractTypesOfColumns(meta, colNumber)

        table.putCollumns(colNumber, colNames, colTypes)
    }

    private fun extractRows(table: Table, rs: ResultSet) {
        val colNumber: Int = table.numberOfColumns
        var rowVals: Array<Any>
        try {
            while (rs.next()) {
                rowVals = extractValuesFromRow(rs, colNumber)
                val (startTime, endTime) = getTimeBoundsOfRow(rs)
                table.putRow(rowVals, startTime, endTime)
            }
        } catch (e: SQLException) {
            println("SQL error " + e.message)
        }
    }

    private fun getNumberOfColumns(meta: ResultSetMetaData): Int {
        var number = 0
        try {
            number = meta.columnCount
        } catch (e: SQLException) {
            println("SQL error " + e.message)
        }
        return number
    }

    private fun extractNamesOfColumns(meta: ResultSetMetaData, count: Int): MutableList<String> {
        var s: String
        val colNames = Array(count) { i -> "" }
        try {
            for (i in 0 until count) {
                s = meta.getColumnLabel(i + 1)
                if (s == "start_time") {
                    startTimePos = i
                } else if (s == "end_time") {
                    endTimePos = i
                }

                colNames[i] = s
            }
        } catch (e: SQLException) {
            println("SQL error " + e.message)
        }
        return colNames.toMutableList()
    }

    fun extractTypesOfColumns(meta: ResultSetMetaData, count: Int): MutableList<EType> {
        val colTypes = Array<EType>(count) { EType.UNKNOWN }
        try {
            for (i in 0 until count) {
                // if (i != startTimePos && i != endTimePos) {
                val nameOfType = JDBCType.valueOf(meta.getColumnType(i + 1)).getName()
                colTypes[i] = JDBCTypes.valueOf(nameOfType).toEType()
                // }
            }
        } catch (e: SQLException) {
            println("SQL error " + e.message)
        }
        return colTypes.toMutableList()
    }

    private fun extractValuesFromRow(rs: ResultSet, count: Int): Array<Any> {
        val rowVals = Array<Any>(count) { 0 }
        try {
            for (i in 0 until count)  /* go through each column of the row */ {
                /* ignore the start and end time row values */
                // if (i != startTimePos && i != endTimePos) {
                val resultSetVal = rs.getObject(i + 1)
                rowVals[i] = resultSetVal
                if (resultSetVal == null || rowVals[i] == "null") {
                    rowVals[i] = "null"
                }
                // }
            }
        } catch (e: SQLException) {
            println("SQL error " + e.message)
        }
        return rowVals
    }

    private fun getTimeBoundsOfRow(rs: ResultSet): Pair<Long, Long> {
        var startTime = Long.MIN_VALUE
        var endTime = Long.MAX_VALUE
        try {
            // +1 as index from 1
            startTime = rs.getLong(startTimePos + 1)
            endTime = rs.getLong(endTimePos + 1)
        } catch (e: SQLException) {
            println("SQL error " + e.message)
        }
        return Pair(startTime, endTime)
    }
}

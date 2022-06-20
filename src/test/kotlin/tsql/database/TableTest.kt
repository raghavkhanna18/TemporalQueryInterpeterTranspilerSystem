package tsql.database

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tsql.ast.types.EBinOp
import tsql.ast.types.EType

internal class TableTest {

    @Test
    fun testClone() {
        val t1 = Table()
        t1.putCollumns(2, mutableListOf("test1", "test2"), mutableListOf(EType.STRING, EType.STRING))
        t1.putRow(arrayOf("ta, tb"), 2000L, 3000L)
        t1.putRow(arrayOf("za, zb"), 2000L, 3000L)
        val t2 = t1.clone()
        assertEquals(t1.columnTypes, t2.columnTypes)
        assertEquals(t1.columnNames, t2.columnNames)
        assertEquals(t1.rows, t2.rows)
    }

    @Test
    fun project() {
        val t1 = Table()
        t1.putCollumns(2, mutableListOf("test1", "test2"), mutableListOf(EType.STRING, EType.STRING))
        t1.putRow(arrayOf("ta, tb"), 2000L, 3000L)
        t1.putRow(arrayOf("za, zb"), 2000L, 3000L)
        t1.project(listOf("test1"))
        assertEquals(1, t1.columnTypes.size)
        assertEquals(1, t1.columnNames.size)
        assertEquals(2, t1.rows.size)
    }

    @Test
    fun coalesce() {
        val t1 = Table()
        t1.putCollumns(2, mutableListOf("test1", "test2"), mutableListOf(EType.STRING, EType.STRING))
        t1.putRow(arrayOf("ta, tb"), 2000L, 3000L)
        t1.putRow(arrayOf("ta, tb"), 2500L, 3000L)
        t1.coalesce()
        assertEquals(1, t1.rows.size)
    }

    @Test
    fun convertToFunction() {
        assertTrue(Table().convertToFunction(1, 1, EBinOp.EQUAL)(Row()))
        assertTrue(Table().convertToFunction(0, 1, EBinOp.LESS_EQUAL)(Row()))
        assertTrue(Table().convertToFunction(2, 1, EBinOp.GREATER_EQUAL)(Row()))
        assertTrue(Table().convertToFunction(2, 1, EBinOp.GREATER)(Row()))
        assertTrue(Table().convertToFunction(0, 1, EBinOp.LESS)(Row()))
        assertTrue(Table().convertToFunction(0, 1, EBinOp.NOT_EQUAL)(Row()))
    }

    @Test
    fun convertToFunctionIndexNumeric() {
        val r = Row(data = mutableListOf(0, 1, 2, 3, 4, 5, 6), startTime = 1000L, endTime = 2000L)
        assertTrue(Table().convertToFunctionIndexNumeric(1, 1, EBinOp.EQUAL)(r))
        assertTrue(Table().convertToFunctionIndexNumeric(0, 1, EBinOp.LESS_EQUAL)(r))
        assertTrue(Table().convertToFunctionIndexNumeric(2, 1, EBinOp.GREATER_EQUAL)(r))
        assertTrue(Table().convertToFunctionIndexNumeric(2, 1, EBinOp.GREATER)(r))
        assertTrue(Table().convertToFunctionIndexNumeric(0, 1, EBinOp.LESS)(r))
        assertTrue(Table().convertToFunctionIndexNumeric(0, 1, EBinOp.NOT_EQUAL)(r))
    }

    @Test
    fun convertToFunctionIndex() {
        val r = Row(data = mutableListOf(0, 1, 2, 3, 4, 5, 6), startTime = 1000L, endTime = 2000L)
        assertTrue(Table().convertToFunctionIndex(1, 1, EBinOp.EQUAL)(r))
        assertTrue(Table().convertToFunctionIndex(0, 1, EBinOp.LESS_EQUAL)(r))
        assertTrue(Table().convertToFunctionIndex(2, 1, EBinOp.GREATER_EQUAL)(r))
        assertTrue(Table().convertToFunctionIndex(2, 1, EBinOp.GREATER)(r))
        assertTrue(Table().convertToFunctionIndex(0, 1, EBinOp.LESS)(r))
        assertTrue(Table().convertToFunctionIndex(0, 1, EBinOp.NOT_EQUAL)(r))
    }

    @Test
    fun convertToFunctionIndexIndex() {
        val r = Row(data = mutableListOf(1, 2, 3, 4, 5, 6), startTime = 1000L, endTime = 2000L)
        assertTrue(Table().convertToFunctionIndexIndex(1, 1, EBinOp.EQUAL, EType.INT)(r))
        assertTrue(Table().convertToFunctionIndexIndex(0, 1, EBinOp.LESS_EQUAL, EType.INT)(r))
        assertTrue(Table().convertToFunctionIndexIndex(2, 1, EBinOp.GREATER_EQUAL, EType.INT)(r))
        assertTrue(Table().convertToFunctionIndexIndex(2, 1, EBinOp.GREATER, EType.INT)(r))
        assertTrue(Table().convertToFunctionIndexIndex(0, 1, EBinOp.LESS, EType.INT)(r))
        assertTrue(Table().convertToFunctionIndexIndex(0, 1, EBinOp.NOT_EQUAL, EType.INT)(r))
    }

    @Test
    fun convertToFunctionIndexIndexNumeric() {
        val r = Row(data = mutableListOf(1, 2, 3, 4, 5, 6), startTime = 1000L, endTime = 2000L)
        assertTrue(Table().convertToFunctionIndexIndexNumeric(1, 1, EBinOp.EQUAL, EType.INT)(r))
        assertTrue(Table().convertToFunctionIndexIndexNumeric(0, 1, EBinOp.LESS_EQUAL, EType.INT)(r))
        assertTrue(Table().convertToFunctionIndexIndexNumeric(2, 1, EBinOp.GREATER_EQUAL, EType.INT)(r))
        assertTrue(Table().convertToFunctionIndexIndexNumeric(2, 1, EBinOp.GREATER, EType.INT)(r))
        assertTrue(Table().convertToFunctionIndexIndexNumeric(0, 1, EBinOp.LESS, EType.INT)(r))
        assertTrue(Table().convertToFunctionIndexIndexNumeric(0, 1, EBinOp.NOT_EQUAL, EType.INT)(r))
    }
}

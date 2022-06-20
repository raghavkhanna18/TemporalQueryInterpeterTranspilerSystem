package tsql

import org.junit.jupiter.api.Test
import tsql.ast.constructAndCreateAST
import tsql.database.Query
import tsql.database.Table
import kotlin.test.assertEquals

class TestUtil {
    @Test
    fun testUntilProduct(){
        val input = "SELECT a.artist, a.rank, b.title, b.rank FROM  top_100_artists as 'a' UNTIL top_100_songs as 'b'  WHERE a.artist = 'BTS' OR (a.rank = 1 AND b.rank = 2);"

        val (absSynTree, symbolTable) = constructAndCreateAST(
            input
        )
        var table = absSynTree.execute()

        assert(table is Table)
        table = table as Table
        assertEquals(4, table.rows.size)
    }

    @Test
    fun testSinceProduct(){
        val input = "SELECT a.artist, a.rank, b.title, b.rank FROM  top_100_artists as 'a' SINCE top_100_songs as 'b'  WHERE a.artist = 'BTS' OR (a.rank = 1 AND b.rank = 2);"

        val (absSynTree, symbolTable) = constructAndCreateAST(
            input
        )
        var table = absSynTree.execute()

        assert(table is Table)
        table = table as Table
        assertEquals(4, table.rows.size)
    }

    @Test
    fun testTimesProduct(){
        val input = "SELECT a.artist, a.rank, b.title, b.rank FROM  top_100_artists as 'a' SINCE top_100_songs as 'b'  WHERE a.artist = 'BTS' OR (a.rank = 1 AND b.rank = 2);"

        val (absSynTree, symbolTable) = constructAndCreateAST(
            input
        )
        var table = absSynTree.execute()

        assert(table is Table)
        table = table as Table
        assertEquals(4, table.rows.size)
    }

    @Test
    fun testSinceJoin() {
        val input =
            "SELECT a.artist, a.rank, b.title, b.rank FROM  top_100_artists as 'a' SINCE JOIN top_100_songs as 'b' ON a.artist = b.artist  WHERE a.artist = 'BTS';"

        val (absSynTree, symbolTable) = constructAndCreateAST(
            input
        )
        var table = absSynTree.execute()

        assert(table is Table)
        table = table as Table
        assertEquals(42, table.rows.size)
    }

    @Test
    fun testUntilJoin() {
        val input =
            "SELECT a.artist, a.rank, b.title, b.rank FROM  top_100_artists as 'a' SINCE JOIN top_100_songs as 'b' ON a.artist = b.artist  WHERE a.artist = 'BTS';"


        val (absSynTree, symbolTable) = constructAndCreateAST(
            input
        )
        var table = absSynTree.execute()

        assert(table is Table)
        table = table as Table
        assertEquals(42, table.rows.size)
    }

    @Test
    fun testSQLTranspileExecuteSameAsInterpter(){
        val input =
            "SELECT a.artist, a.rank, b.title, b.rank FROM  top_100_artists as 'a' SINCE JOIN top_100_songs as 'b' ON a.artist = b.artist  WHERE a.artist = 'BTS';"

        val (absSynTree, symbolTable) = constructAndCreateAST(
            input
        )
        val sqlString = absSynTree.toSQL(symbolTable).first

        val resultTableSQL  = Query.exec(sqlString)

        val resultTableInt = absSynTree.execute() as Table
        assertEquals(resultTableInt.rows.size, resultTableSQL.rows.size)

    }



    @Test
    fun testSQLTranspileExecuteSameAsInterpter2(){
        val input =
            "SELECT a.artist, a.rank, b.title, b.rank FROM  top_100_artists as 'a' UNTIL  top_100_songs as 'b' WHERE a.artist = 'BTS';"
        val (absSynTree, symbolTable) = constructAndCreateAST(
            input
        )
        val sqlString = absSynTree.toSQL(symbolTable).first

        val resultTableSQL  = Query.exec(sqlString)

        val resultTableInt = absSynTree.execute() as Table
        assertEquals(resultTableInt.rows.size, resultTableSQL.rows.size)

    }

    @Test
    fun testSQLTranspileExecuteSameAsInterpter3(){
        val input =
            "SELECT a.artist, a.rank, b.title, b.rank FROM  top_100_artists as 'a' UNTIL JOIN top_100_songs as 'b' ON a.artist = b.artist  WHERE a.artist = 'BTS';"

        val (absSynTree, symbolTable) = constructAndCreateAST(
            input
        )
        val sqlString = absSynTree.toSQL(symbolTable).first

        val resultTableSQL  = Query.exec(sqlString)

        val resultTableInt = absSynTree.execute() as Table
        assertEquals(resultTableInt.rows.size, resultTableSQL.rows.size)

    }

    @Test
    fun testSQLTranspileExecuteSameAsInterpter5(){
        val input =
            "SELECT a.artist, a.rank FROM top_100_artists as 'a' ALWAYS PAST;"
        val (absSynTree, symbolTable) = constructAndCreateAST(
            input
        )
        val sqlString = absSynTree.toSQL(symbolTable).first
        println(sqlString)

        val resultTableInt = absSynTree.execute() as Table
         assertEquals(resultTableInt.rows.size, 1)

    }

    @Test
    fun testSQLTranspileExecuteSameAsInterpter6(){
        val input =
            "SELECT a.artist, a.rank, b.title, b.rank FROM  top_100_artists as 'a' SINCE  top_100_songs as 'b' WHERE a.artist = 'BTS';"
        val (absSynTree, symbolTable) = constructAndCreateAST(
            input
        )
        val sqlString = absSynTree.toSQL(symbolTable).first

        val resultTableSQL  = Query.exec(sqlString)

        val resultTableInt = absSynTree.execute() as Table
        assertEquals(resultTableInt.rows.size, resultTableSQL.rows.size)

    }
}

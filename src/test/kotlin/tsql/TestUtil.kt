package tsql

import org.junit.jupiter.api.Test
import tsql.ast.constructAndCreateAST
import tsql.database.Table
import tsql.error.CommonErrorPrinter
import tsql.error.ErrorAccumulator
import kotlin.test.assertEquals

class TestUtil {
    @Test
    fun testUntilProduct(){
        val input = "SELECT a.artist, a.rank, b.title, b.rank FROM  top_100_artists as 'a' UNTIL top_100_songs as 'b'  WHERE a.artist = 'BTS' OR (a.rank = 1 AND b.rank = 2);"
        val syntaxErrorAccumulator = ErrorAccumulator(
            Constants.SYNTAX_EXIT_CODE,
            CommonErrorPrinter(arrayOf(input))
        )

        val absSynTree = constructAndCreateAST(
            syntaxErrorAccumulator,
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
        val syntaxErrorAccumulator = ErrorAccumulator(
            Constants.SYNTAX_EXIT_CODE,
            CommonErrorPrinter(arrayOf(input))
        )

        val absSynTree = constructAndCreateAST(
            syntaxErrorAccumulator,
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
        val syntaxErrorAccumulator = ErrorAccumulator(
            Constants.SYNTAX_EXIT_CODE,
            CommonErrorPrinter(arrayOf(input))
        )

        val absSynTree = constructAndCreateAST(
            syntaxErrorAccumulator,
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
            "SELECT a.artist, a.rank, b.title, b.rank FROM  top_100_artists as 'a' SINCE JOIN top_100_songs as 'b' ON a.artist = b.artist  WHERE a.artist = 'BTS');"
        val syntaxErrorAccumulator = ErrorAccumulator(
            Constants.SYNTAX_EXIT_CODE,
            CommonErrorPrinter(arrayOf(input))
        )

        val absSynTree = constructAndCreateAST(
            syntaxErrorAccumulator,
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
            "SELECT a.artist, a.rank, b.title, b.rank FROM  top_100_artists as 'a' SINCE JOIN top_100_songs as 'b' ON a.artist = b.artist  WHERE a.artist = 'BTS');"
        val syntaxErrorAccumulator = ErrorAccumulator(
            Constants.SYNTAX_EXIT_CODE,
            CommonErrorPrinter(arrayOf(input))
        )

        val absSynTree = constructAndCreateAST(
            syntaxErrorAccumulator,
            input
        )
        var table = absSynTree.execute()

        assert(table is Table)
        table = table as Table
        assertEquals(42, table.rows.size)
    }
}

package tsql

import tsql.Utils.TIME_UNITS
import tsql.ast.symbol_table.SymbolTable
import tsql.database.Row
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.time.temporal.ChronoUnit.CENTURIES
import java.time.temporal.ChronoUnit.DAYS
import java.time.temporal.ChronoUnit.DECADES
import java.time.temporal.ChronoUnit.ERAS
import java.time.temporal.ChronoUnit.FOREVER
import java.time.temporal.ChronoUnit.HALF_DAYS
import java.time.temporal.ChronoUnit.HOURS
import java.time.temporal.ChronoUnit.MICROS
import java.time.temporal.ChronoUnit.MILLENNIA
import java.time.temporal.ChronoUnit.MILLIS
import java.time.temporal.ChronoUnit.MINUTES
import java.time.temporal.ChronoUnit.MONTHS
import java.time.temporal.ChronoUnit.NANOS
import java.time.temporal.ChronoUnit.SECONDS
import java.time.temporal.ChronoUnit.WEEKS
import java.time.temporal.ChronoUnit.YEARS

object Utils {
    const val MIN_TIME = 0L
    var MAX_TIME = Instant.now().toEpochMilli()
    var CURRENT_TIME = 3L
    var TIME_UNITS = DAYS
}

fun incrementTime(time: Long, unit: ChronoUnit, simpleLong: Boolean = false): Long = when (simpleLong) {
    true -> time + 1
    false -> when (unit) {
        NANOS -> throw NotImplementedError("Time unit of $NANOS has not been implemented")
        MICROS -> throw NotImplementedError("Time unit of $MICROS has not been implemented")
        MILLIS -> Instant.ofEpochMilli(time).plusMillis(1).toEpochMilli()
        SECONDS -> Instant.ofEpochMilli(time).plusSeconds(1).toEpochMilli()
        MINUTES -> Instant.ofEpochMilli(time).plus(1, MINUTES).toEpochMilli()
        HOURS -> Instant.ofEpochMilli(time).plus(1, HOURS).toEpochMilli()
        HALF_DAYS -> Instant.ofEpochMilli(time).plus(1, HALF_DAYS).toEpochMilli()
        DAYS -> Instant.ofEpochMilli(time).plus(1, DAYS).toEpochMilli()
        WEEKS -> Instant.ofEpochMilli(time).plus(7, DAYS).toEpochMilli()
        MONTHS -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .plusMonths(1)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        YEARS -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .plusYears(1)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        DECADES -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .plusYears(10)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        CENTURIES -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .plusYears(100)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        MILLENNIA -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .plusYears(1000)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        ERAS -> throw NotImplementedError("Time unit of $ERAS has not been implemented")
        // Forever used to indicate Long
        FOREVER -> time + 1
    }
}

fun decrementTime(time: Long, unit: ChronoUnit, simpleLong: Boolean = false): Long = when (simpleLong) {
    true -> time - 1
    false -> when (unit) {
        NANOS -> throw NotImplementedError("Time unit of $NANOS has not been implemented")
        MICROS -> throw NotImplementedError("Time unit of $MICROS has not been implemented")
        MILLIS -> Instant.ofEpochMilli(time).minusMillis(1).toEpochMilli()
        SECONDS -> Instant.ofEpochMilli(time).minusSeconds(1).toEpochMilli()
        MINUTES -> Instant.ofEpochMilli(time).minus(1, MINUTES).toEpochMilli()
        HOURS -> Instant.ofEpochMilli(time).minus(1, HOURS).toEpochMilli()
        HALF_DAYS -> Instant.ofEpochMilli(time).minus(1, HALF_DAYS).toEpochMilli()
        DAYS -> Instant.ofEpochMilli(time).minus(1, DAYS).toEpochMilli()
        WEEKS -> Instant.ofEpochMilli(time).minus(7, DAYS).toEpochMilli()
        MONTHS -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .minusMonths(1)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        YEARS -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .minusYears(1)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        DECADES -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .minusYears(10)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        CENTURIES -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .minusYears(100)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        MILLENNIA -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .minusYears(1000)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        ERAS -> throw NotImplementedError("Time unit of $ERAS has not been implemented")
        // Forever used to indicate Long
        FOREVER -> time + 1
    }
}

fun getOverlapContion(symbolTable: SymbolTable): String {
    val tableNames = symbolTable.getTableNames()
    val firstTable = tableNames.first()
    val firstTableName = if (firstTable.second != "") firstTable.second else firstTable.first
    val secondTable = tableNames.get(1)
    val secondTableName = if (secondTable.second != "") secondTable.second else secondTable.first
    return "AND $firstTableName.start_time < $secondTableName.end_time AND  $secondTableName.start_time < $firstTableName.end_time"
}

fun getTimeUnitString(): String {
    return when (TIME_UNITS) {
        NANOS -> "'0.001 MICROSECOND'"
        MICROS -> "'1 MICROSECOND'"
        MILLIS -> "'1000 MICROSECOND'"
        SECONDS -> "'1 SECOND'"
        MINUTES -> "'1 MINUTE'"
        HOURS -> "'1 HOUR'"
        HALF_DAYS -> "'0.5 DAY'"
        DAYS -> "'1 DAY'"
        WEEKS -> "'1 WEEK'"
        MONTHS -> "'1 MONTH'"
        YEARS -> "'1 YEAR'"
        DECADES -> "'10 YEAR'"
        CENTURIES -> "'100 YEAR'"
        MILLENNIA -> "'1000 YEAR'"
        ERAS -> throw NotImplementedError("Time unit of $ERAS has not been implemented")
        FOREVER -> ""
    }
}

fun overlap(left: Row, right: Row): Boolean {
    return left.startTime < right.endTime && right.startTime < left.endTime
}

fun alterAttributes(basSQL: String): Pair<String, List<String>> {
    val regex = "(?<=SELECT)(.*?)(?=FROM)".toRegex()
    val matches = regex.find(basSQL)
    val matchesList = matches?.groupValues
    var match = ""
    if (matchesList != null) {
        match = matchesList.first()
    }
    val ats = match.split(",")
    val renamedAts = mutableListOf<String>()
    val combinedNames = mutableListOf<String>()
    for (at in ats) {
        if (!at.contains("AS") && !at.contains("GREATEST") &&  !at.contains("LEAST") ) {
            val firstPart = ".*(?=\\.)".toRegex().find(at)?.groupValues?.first() ?: ""
            val secondPart = "(?<=\\.).*".toRegex().find(at)?.groupValues?.first() ?: ""
            val combinedName = "$firstPart$secondPart".trim()
            combinedNames.add(combinedName)
            renamedAts.add("$at AS $combinedName")
        } else {
            renamedAts.add(at)
        }
    }
    val combinedString = renamedAts.joinToString(", ")
    val newString = basSQL.replaceFirst(regex, combinedString)
    return Pair(newString, combinedNames)
}

fun createALLSQl(baseSqlAndRenames: Pair<String, List<String>>): String {
    var fullString = ""
    val baseSQL = baseSqlAndRenames.first

    val combinedNames = baseSqlAndRenames.second
    val modCombinedNames = mutableListOf<String>()
    for (combinedName in combinedNames) {
        modCombinedNames.add("c.$combinedName")
    }
    val modCombinedNamesString = modCombinedNames.joinToString(", ")
    val combinedNameString = combinedNames.joinToString(", ")
    val attributeEquality = mutableListOf<String>()
    for (combinedName in combinedNames) {
        attributeEquality.add("c.$combinedName = d.$combinedName")
    }
    val attributeEqualityString = attributeEquality.joinToString(" AND ")
    fullString +=
        """
        DROP TABLE IF EXISTS i_t;
        CREATE TEMP TABLE i_t AS
        $baseSQL
        DROP TABLE IF EXISTS  base_example_rec;
        CREATE TEMP TABLE base_example_rec AS
        WITH RECURSIVE all_rel AS (SELECT *
                           FROM i_t
                           UNION
                           SELECT *
                           FROM (WITH inner_rel AS (SELECT * FROM all_rel)
                           SELECT $modCombinedNamesString, d.s as s, c.e as e
                           FROM i_t c
                           CROSS join inner_rel d
                           WHERE c.s <= d.e
                           AND c.e >= d.e
                           AND d.s != c.s
                           AND c.e != d.e
                           AND $attributeEqualityString )
                           t )
        SELECT * FROM all_rel;
        SELECT $combinedNameString, s, e FROM (SELECT *,
                row_number() OVER
                    (PARTITION BY $combinedNameString, s ORDER BY e desc) AS srown,
                row_number() OVER
                    (PARTITION BY $combinedNameString, e ORDER BY s asc) AS erown
                FROM base_example_rec) r
        WHERE  r.srown = 1 AND r.erown = 1 ORDER BY s, e desc;
        """.trimIndent()
    return fullString
}



package tsql

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
object Utils {
    const val MIN_TIME = 0L
    var MAX_TIME = Instant.now().toEpochMilli()
    var CURRENT_TIME = 3L
    var TIME_UNITS = ChronoUnit.DAYS
}
fun incrementTime(time: Long, unit: ChronoUnit, simpleLong: Boolean = false): Long = when (simpleLong) {
    true -> time + 1
    false -> when(unit){
        ChronoUnit.NANOS -> throw NotImplementedError("Time unit of ${ChronoUnit.NANOS} has not been implemented")
        ChronoUnit.MICROS -> throw NotImplementedError("Time unit of ${ChronoUnit.MICROS} has not been implemented")
        ChronoUnit.MILLIS -> Instant.ofEpochMilli(time).plusMillis(1).toEpochMilli()
        ChronoUnit.SECONDS -> Instant.ofEpochMilli(time).plusSeconds(1).toEpochMilli()
        ChronoUnit.MINUTES -> Instant.ofEpochMilli(time).plus(1, ChronoUnit.MINUTES).toEpochMilli()
        ChronoUnit.HOURS -> Instant.ofEpochMilli(time).plus(1, ChronoUnit.HOURS).toEpochMilli()
        ChronoUnit.HALF_DAYS -> Instant.ofEpochMilli(time).plus(1, ChronoUnit.HALF_DAYS).toEpochMilli()
        ChronoUnit.DAYS -> Instant.ofEpochMilli(time).plus(1, ChronoUnit.DAYS).toEpochMilli()
        ChronoUnit.WEEKS -> Instant.ofEpochMilli(time).plus(7, ChronoUnit.DAYS).toEpochMilli()
        ChronoUnit.MONTHS -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .plusMonths(1)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        ChronoUnit.YEARS -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .plusYears(1)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        ChronoUnit.DECADES -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .plusYears(10)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        ChronoUnit.CENTURIES -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .plusYears(100)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        ChronoUnit.MILLENNIA -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .plusYears(1000)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        ChronoUnit.ERAS -> throw NotImplementedError("Time unit of ${ChronoUnit.ERAS} has not been implemented")
        ChronoUnit.FOREVER -> throw NotImplementedError("Time unit of ${ChronoUnit.FOREVER} has not been implemented")
    }
}

fun decrementTime(time: Long, unit: ChronoUnit, simpleLong: Boolean = false): Long = when (simpleLong) {
    true -> time - 1
    false -> when (unit) {
        ChronoUnit.NANOS -> throw NotImplementedError("Time unit of ${ChronoUnit.NANOS} has not been implemented")
        ChronoUnit.MICROS -> throw NotImplementedError("Time unit of ${ChronoUnit.MICROS} has not been implemented")
        ChronoUnit.MILLIS -> Instant.ofEpochMilli(time).minusMillis(1).toEpochMilli()
        ChronoUnit.SECONDS -> Instant.ofEpochMilli(time).minusSeconds(1).toEpochMilli()
        ChronoUnit.MINUTES -> Instant.ofEpochMilli(time).minus(1, ChronoUnit.MINUTES).toEpochMilli()
        ChronoUnit.HOURS -> Instant.ofEpochMilli(time).minus(1, ChronoUnit.HOURS).toEpochMilli()
        ChronoUnit.HALF_DAYS -> Instant.ofEpochMilli(time).minus(1, ChronoUnit.HALF_DAYS).toEpochMilli()
        ChronoUnit.DAYS -> Instant.ofEpochMilli(time).minus(1, ChronoUnit.DAYS).toEpochMilli()
        ChronoUnit.WEEKS -> Instant.ofEpochMilli(time).minus(7, ChronoUnit.DAYS).toEpochMilli()
        ChronoUnit.MONTHS -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .minusMonths(1)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        ChronoUnit.YEARS -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .minusYears(1)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        ChronoUnit.DECADES -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .minusYears(10)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        ChronoUnit.CENTURIES -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .minusYears(100)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        ChronoUnit.MILLENNIA -> LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"))
            .minusYears(1000)
            .atStartOfDay()
            .toInstant(
                ZoneOffset.UTC
            ).toEpochMilli()
        ChronoUnit.ERAS -> throw NotImplementedError("Time unit of ${ChronoUnit.ERAS} has not been implemented")
        ChronoUnit.FOREVER -> throw NotImplementedError("Time unit of ${ChronoUnit.FOREVER} has not been implemented")
    }
}


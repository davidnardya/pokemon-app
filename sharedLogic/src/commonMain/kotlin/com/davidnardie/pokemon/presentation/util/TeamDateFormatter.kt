package com.davidnardie.pokemon.presentation.util

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal object TeamDateFormatter {
    private val MONTH_NAMES = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
    )

    @OptIn(ExperimentalTime::class)
    fun currentMonthDay(): Pair<Int, Int> =
        monthDayFromEpochMillis(Clock.System.now().toEpochMilliseconds())

    fun monthDayFromEpochMillis(epochMillis: Long): Pair<Int, Int> {
        val days = epochMillis.floorDiv(86_400_000L)
        val z = days + 719468
        val era = if (z >= 0) z / 146097 else (z - 146096) / 146097
        val doe = z - era * 146097
        val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
        val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
        val mp = (5 * doy + 2) / 153
        val d = doy - (153 * mp + 2) / 5 + 1
        val m = mp + if (mp < 10) 3 else -9
        return m.toInt() to d.toInt()
    }

    fun formatAddedDate(month: Int, day: Int): String =
        "Added ${MONTH_NAMES[month - 1]} $day"
}

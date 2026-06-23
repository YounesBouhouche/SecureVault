package com.younesb.securevault.features.main.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.younesb.securevault.R

const val millisecondsInADay = 86_400_000L
const val millisecondsInAYear = 31_536_000_000L

@Composable
fun Long.toReadableDateString(): String {
    val currentTime = System.currentTimeMillis()
    val difference = currentTime - this

    return when {
        difference < millisecondsInADay -> stringResource(R.string.today)
        difference < 2 * millisecondsInADay -> stringResource(R.string.yesterday)
        difference < 7 * millisecondsInADay -> stringResource(
            R.string.days_ago,
            difference / millisecondsInADay
        )
        difference < millisecondsInAYear -> stringResource(
            R.string.weeks_ago,
            difference / (millisecondsInADay * 7)
        )
        else -> stringResource(R.string.years_ago, difference / millisecondsInAYear)
    }
}

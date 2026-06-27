package com.davidnardie.pokemon.presentation.state

data class AppSettings(
    val useMetricUnits: Boolean = true,
    val use24HourTime: Boolean = false,
    val useDarkTheme: Boolean = false,
)

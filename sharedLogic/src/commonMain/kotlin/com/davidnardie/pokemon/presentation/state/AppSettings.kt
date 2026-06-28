package com.davidnardie.pokemon.presentation.state

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val useMetricUnits: Boolean = true,
    val use24HourTime: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.System,
)

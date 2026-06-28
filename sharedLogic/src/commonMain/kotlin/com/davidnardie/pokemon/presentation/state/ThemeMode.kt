package com.davidnardie.pokemon.presentation.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ThemeMode {
    @SerialName("system")
    System,

    @SerialName("light")
    Light,

    @SerialName("dark")
    Dark,
    ;

    fun displayLabel(): String = when (this) {
        System -> "System"
        Light -> "Light"
        Dark -> "Dark"
    }

    fun resolveIsDark(systemIsDark: Boolean): Boolean = when (this) {
        System -> systemIsDark
        Light -> false
        Dark -> true
    }
}

package com.davidnardie.pokemon.presentation.util

import kotlin.math.round

object PokemonDetailFormatter {
    fun formatWeight(weightHectograms: Int, useMetricUnits: Boolean): String {
        if (useMetricUnits) {
            val kg = weightHectograms / 10.0
            return "${formatDecimal(kg)} kg"
        }
        val lbs = weightHectograms / 10.0 * 2.20462
        return "${formatDecimal(lbs)} lbs"
    }

    fun formatHeight(heightDecimeters: Int, useMetricUnits: Boolean): String {
        if (useMetricUnits) {
            val meters = heightDecimeters / 10.0
            return "${formatDecimal(meters)} m"
        }
        val totalInches = round(heightDecimeters / 10.0 * 39.3701).toInt()
        val feet = totalInches / 12
        val inches = totalInches % 12
        return "$feet'$inches\""
    }

    fun formatAbilityName(apiName: String): String =
        apiName.split('-', ' ')
            .joinToString(" ") { word ->
                word.replaceFirstChar { char ->
                    if (char.isLowerCase()) char.titlecase() else char.toString()
                }
            }

    private fun formatDecimal(value: Double): String {
        val rounded = round(value * 10.0) / 10.0
        return if (rounded % 1.0 == 0.0) {
            rounded.toInt().toString()
        } else {
            rounded.toString()
        }
    }
}

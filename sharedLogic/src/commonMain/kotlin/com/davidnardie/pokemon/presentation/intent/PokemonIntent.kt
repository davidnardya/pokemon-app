package com.davidnardie.pokemon.presentation.intent

import com.davidnardie.pokemon.presentation.state.NavigationTab
import com.davidnardie.pokemon.presentation.state.ThemeMode

sealed interface PokemonIntent {
    data class LoadPokemonList(
        val limit: Int = 20,
        val offset: Int = 0,
    ) : PokemonIntent

    data object LoadNextPage : PokemonIntent

    data object RefreshPokemonList : PokemonIntent

    data object OpenSettings : PokemonIntent

    data object CloseSettings : PokemonIntent

    data class SelectTab(
        val tab: NavigationTab,
    ) : PokemonIntent

    data class ToggleTeamMembership(
        val pokemonId: Int,
    ) : PokemonIntent

    data class OpenPokemonDetail(
        val pokemonId: Int,
    ) : PokemonIntent

    data object ClosePokemonDetail : PokemonIntent

    data object ToggleMetricUnits : PokemonIntent

    data class SetMetricUnits(
        val useMetric: Boolean,
    ) : PokemonIntent

    data object ToggleTimeFormat : PokemonIntent

    data class SetUse24HourTime(
        val use24HourTime: Boolean,
    ) : PokemonIntent

    data object OpenModePicker : PokemonIntent

    data object CloseModePicker : PokemonIntent

    data class SelectThemeMode(
        val themeMode: ThemeMode,
    ) : PokemonIntent

    data object OpenAbout : PokemonIntent

    data object CloseAbout : PokemonIntent

    data object CloseTeamFullDialog : PokemonIntent
}

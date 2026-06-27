package com.davidnardie.pokemon.presentation.intent

import com.davidnardie.pokemon.domain.model.Pokemon
import com.davidnardie.pokemon.presentation.state.NavigationTab

sealed interface PokemonIntent {
    data class LoadPokemonList(
        val limit: Int = 20,
        val offset: Int = 0,
    ) : PokemonIntent

    data object LoadNextPage : PokemonIntent

    data object OpenSettings : PokemonIntent

    data object CloseSettings : PokemonIntent

    data class SelectTab(
        val tab: NavigationTab,
    ) : PokemonIntent

    data class ToggleTeamMembership(
        val pokemonId: Int,
    ) : PokemonIntent

    data class OpenPokemonDetail(
        val pokemon: Pokemon,
    ) : PokemonIntent

    data object ClosePokemonDetail : PokemonIntent

    data object ToggleMetricUnits : PokemonIntent

    data object ToggleTimeFormat : PokemonIntent

    data object ToggleTheme : PokemonIntent

    data object CloseTeamFullDialog : PokemonIntent
}

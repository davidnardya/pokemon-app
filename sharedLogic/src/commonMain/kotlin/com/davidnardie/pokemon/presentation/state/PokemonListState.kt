package com.davidnardie.pokemon.presentation.state

import com.davidnardie.pokemon.domain.model.Pokemon

data class PokemonListState(
    val isLoading: Boolean = false,
    val pokemon: List<Pokemon> = emptyList(),
    val offset: Int = 0,
    val hasMore: Boolean = true,
    val error: String? = null,
    val networkError: Boolean = false,
    val selectedTab: NavigationTab = NavigationTab.Pokemon,
    val isSettingsVisible: Boolean = false,
    val teamPokemonIds: List<Int> = emptyList(),
    val selectedPokemon: Pokemon? = null,
    val settings: AppSettings = AppSettings(),
    val showTeamFullDialog: Boolean = false,
) {
    fun isInTeam(pokemonId: Int): Boolean = pokemonId in teamPokemonIds

    fun displayedPokemon(): List<Pokemon> = when (selectedTab) {
        NavigationTab.Pokemon -> pokemon
        NavigationTab.Team -> teamPokemonIds.mapNotNull { id -> pokemon.find { it.id == id } }
    }

    val isCacheEmpty: Boolean get() = pokemon.isEmpty()

    fun shouldShowLoadingSpinner(): Boolean = isLoading && isCacheEmpty

    fun shouldShowNetworkErrorEmpty(): Boolean = networkError && isCacheEmpty

    fun shouldShowFullScreenLoadingState(): Boolean =
        shouldShowLoadingSpinner() || shouldShowNetworkErrorEmpty()
}

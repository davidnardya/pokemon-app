package com.davidnardie.pokemon.presentation.state

import com.davidnardie.pokemon.domain.model.Pokemon
import com.davidnardie.pokemon.domain.model.PokemonType
import com.davidnardie.pokemon.domain.model.TeamMember
import com.davidnardie.pokemon.presentation.model.TeamPokemonItem
import com.davidnardie.pokemon.presentation.util.TeamDateFormatter

data class PokemonListState(
    val isLoading: Boolean = false,
    val pokemon: List<Pokemon> = emptyList(),
    val offset: Int = 0,
    val hasMore: Boolean = true,
    val error: String? = null,
    val networkError: Boolean = false,
    val selectedTab: NavigationTab = NavigationTab.Pokemon,
    val isSettingsVisible: Boolean = false,
    val isModePickerVisible: Boolean = false,
    val isAboutVisible: Boolean = false,
    val teamMembers: List<TeamMember> = emptyList(),
    val pokemonTypes: Map<Int, List<PokemonType>> = emptyMap(),
    val selectedPokemon: Pokemon? = null,
    val settings: AppSettings = AppSettings(),
    val showTeamFullDialog: Boolean = false,
) {
    val teamPokemonIds: List<Int> get() = teamMembers.map { it.pokemonId }

    fun isInTeam(pokemonId: Int): Boolean = teamMembers.any { it.pokemonId == pokemonId }

    fun displayedPokemon(): List<Pokemon> = when (selectedTab) {
        NavigationTab.Pokemon -> pokemon
        NavigationTab.Team -> teamMembers.mapNotNull { member -> pokemon.find { it.id == member.pokemonId } }
    }

    fun teamPokemonItems(): List<TeamPokemonItem> =
        teamMembers.mapNotNull { member ->
            pokemon.find { it.id == member.pokemonId }?.let { pokemonItem ->
                TeamPokemonItem(
                    pokemon = pokemonItem,
                    types = pokemonTypes[member.pokemonId].orEmpty(),
                    addedDateLabel = TeamDateFormatter.formatAddedDate(
                        month = member.addedMonth,
                        day = member.addedDay,
                    ),
                )
            }
        }

    val teamCount: Int get() = teamMembers.size

    val isCacheEmpty: Boolean get() = pokemon.isEmpty()

    fun shouldShowLoadingSpinner(): Boolean = isLoading && isCacheEmpty

    fun shouldShowNetworkErrorEmpty(): Boolean = networkError && isCacheEmpty

    fun shouldShowFullScreenLoadingState(): Boolean =
        shouldShowLoadingSpinner() || shouldShowNetworkErrorEmpty()

    companion object {
        const val MAX_TEAM_SIZE = 6
    }
}

package com.davidnardie.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidnardie.pokemon.domain.model.TeamMember
import com.davidnardie.pokemon.domain.repository.PokemonRepository
import com.davidnardie.pokemon.presentation.state.NavigationTab
import com.davidnardie.pokemon.presentation.state.PokemonListUiState
import com.davidnardie.pokemon.presentation.util.TeamDateFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokemonListViewModel(
    private val repository: PokemonRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PokemonListUiState())
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    fun loadInitialList() {
        loadPokemonList(limit = PAGE_SIZE, offset = 0)
    }

    fun loadNextPage() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.isLoading || state.isRefreshing || !state.hasMore) {
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null, networkError = false) }

            try {
                val page = repository.refresh(limit = PAGE_SIZE, offset = state.offset)
                val nextOffset = state.offset + page.pokemon.size
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        pokemon = it.pokemon + page.pokemon,
                        offset = nextOffset,
                        hasMore = page.hasMore,
                        networkError = page.isOffline,
                        error = if (page.isOffline && page.pokemon.isEmpty()) OFFLINE_ERROR_MESSAGE else null,
                    )
                }
                ensureTeamMemberTypes()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        networkError = false,
                        error = e.message ?: "Failed to load Pokémon",
                    )
                }
            }
        }
    }

    fun refreshPokemonList() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.isRefreshing || (state.isLoading && !state.isCacheEmpty)) {
                return@launch
            }

            _uiState.update {
                it.copy(isRefreshing = true, error = null, networkError = false)
            }

            try {
                val page = repository.hardRefresh(limit = PAGE_SIZE)
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        isLoading = false,
                        pokemon = page.pokemon,
                        offset = page.pokemon.size,
                        hasMore = page.hasMore,
                        networkError = page.isOffline,
                        error = if (page.isOffline && page.pokemon.isEmpty()) OFFLINE_ERROR_MESSAGE else null,
                        pokemonTypes = emptyMap(),
                    )
                }
                ensureTeamMemberTypes()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        networkError = false,
                        error = e.message ?: "Failed to refresh Pokémon",
                    )
                }
            }
        }
    }

    fun selectTab(tab: NavigationTab) {
        _uiState.update { it.copy(selectedTab = tab) }
        if (tab == NavigationTab.Team) {
            ensureTeamMemberTypes()
        }
    }

    fun openPokemonDetail(pokemonId: Int) {
        val pokemon = _uiState.value.findPokemon(pokemonId) ?: return
        _uiState.update {
            it.copy(
                selectedPokemonId = pokemonId,
                selectedPokemon = pokemon,
            )
        }
    }

    fun closePokemonDetail() {
        _uiState.update {
            it.copy(
                selectedPokemonId = null,
                selectedPokemon = null,
            )
        }
    }

    fun toggleTeamMembership(pokemonId: Int) {
        val state = _uiState.value
        if (state.isInTeam(pokemonId)) {
            _uiState.update { current ->
                current.copy(
                    teamMembers = current.teamMembers.filterNot { it.pokemonId == pokemonId },
                    pokemonTypes = current.pokemonTypes - pokemonId,
                )
            }
            return
        }

        if (state.teamMembers.size >= PokemonListUiState.MAX_TEAM_SIZE) {
            _uiState.update { it.copy(showTeamFullDialog = true) }
            return
        }

        val (month, day) = TeamDateFormatter.currentMonthDay()
        _uiState.update { current ->
            current.copy(
                teamMembers = current.teamMembers + TeamMember(
                    pokemonId = pokemonId,
                    addedMonth = month,
                    addedDay = day,
                ),
            )
        }
        fetchPokemonTypes(pokemonId)
    }

    fun closeTeamFullDialog() {
        _uiState.update { it.copy(showTeamFullDialog = false) }
    }

    private fun loadPokemonList(limit: Int, offset: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, error = null, networkError = false)
            }

            try {
                val page = repository.refresh(limit = limit, offset = offset)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        pokemon = page.pokemon,
                        offset = offset + page.pokemon.size,
                        hasMore = page.hasMore,
                        networkError = page.isOffline,
                        error = if (page.isOffline && page.pokemon.isEmpty()) OFFLINE_ERROR_MESSAGE else null,
                    )
                }
                ensureTeamMemberTypes()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        networkError = false,
                        error = e.message ?: "Failed to load Pokémon",
                    )
                }
            }
        }
    }

    private fun ensureTeamMemberTypes() {
        val state = _uiState.value
        state.teamMembers
            .map { it.pokemonId }
            .filter { it !in state.pokemonTypes }
            .forEach(::fetchPokemonTypes)
    }

    private fun fetchPokemonTypes(pokemonId: Int) {
        viewModelScope.launch {
            val detail = repository.fetchPokemonDetail(pokemonId) ?: return@launch
            _uiState.update { current ->
                current.copy(
                    pokemonTypes = current.pokemonTypes + (pokemonId to detail.types),
                )
            }
        }
    }

    private companion object {
        const val PAGE_SIZE = 20
        const val OFFLINE_ERROR_MESSAGE = "Check your connection"
    }
}

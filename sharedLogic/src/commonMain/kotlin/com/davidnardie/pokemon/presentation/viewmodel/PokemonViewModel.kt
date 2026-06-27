package com.davidnardie.pokemon.presentation.viewmodel

import com.davidnardie.pokemon.domain.repository.PokemonRepository
import com.davidnardie.pokemon.presentation.intent.PokemonIntent
import com.davidnardie.pokemon.presentation.state.PokemonListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val repository: PokemonRepository,
) {
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _uiState = MutableStateFlow(PokemonListState())
    val uiState: StateFlow<PokemonListState> = _uiState.asStateFlow()

    fun processIntent(intent: PokemonIntent) {
        when (intent) {
            is PokemonIntent.LoadPokemonList -> loadPokemonList(
                limit = intent.limit,
                offset = intent.offset,
            )
            PokemonIntent.LoadNextPage -> loadNextPage()
            PokemonIntent.OpenSettings -> _uiState.update { it.copy(isSettingsVisible = true) }
            PokemonIntent.CloseSettings -> _uiState.update { it.copy(isSettingsVisible = false) }
            is PokemonIntent.SelectTab -> _uiState.update { it.copy(selectedTab = intent.tab) }
            is PokemonIntent.ToggleTeamMembership -> toggleTeamMembership(intent.pokemonId)
            is PokemonIntent.OpenPokemonDetail -> _uiState.update { it.copy(selectedPokemon = intent.pokemon) }
            PokemonIntent.ClosePokemonDetail -> _uiState.update { it.copy(selectedPokemon = null) }
            PokemonIntent.ToggleMetricUnits -> _uiState.update {
                it.copy(settings = it.settings.copy(useMetricUnits = !it.settings.useMetricUnits))
            }
            PokemonIntent.ToggleTimeFormat -> _uiState.update {
                it.copy(settings = it.settings.copy(use24HourTime = !it.settings.use24HourTime))
            }
            PokemonIntent.ToggleTheme -> _uiState.update {
                it.copy(settings = it.settings.copy(useDarkTheme = !it.settings.useDarkTheme))
            }
            PokemonIntent.CloseTeamFullDialog -> _uiState.update { it.copy(showTeamFullDialog = false) }
        }
    }

    fun onCleared() {
        viewModelScope.cancel()
    }

    private fun toggleTeamMembership(pokemonId: Int) {
        val state = _uiState.value
        if (state.isInTeam(pokemonId)) {
            _uiState.update { current ->
                current.copy(teamPokemonIds = current.teamPokemonIds.filterNot { it == pokemonId })
            }
            return
        }

        if (state.teamPokemonIds.size >= MAX_TEAM_SIZE) {
            _uiState.update { it.copy(showTeamFullDialog = true) }
            return
        }

        _uiState.update { current ->
            current.copy(teamPokemonIds = current.teamPokemonIds + pokemonId)
        }
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

    private fun loadNextPage() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.isLoading || !state.hasMore) {
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

    private companion object {
        const val PAGE_SIZE = 20
        const val MAX_TEAM_SIZE = 6
        const val OFFLINE_ERROR_MESSAGE = "Check your connection"
    }
}

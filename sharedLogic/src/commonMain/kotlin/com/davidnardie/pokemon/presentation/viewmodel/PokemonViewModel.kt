package com.davidnardie.pokemon.presentation.viewmodel

import com.davidnardie.pokemon.domain.model.TeamMember
import com.davidnardie.pokemon.domain.repository.PokemonRepository
import com.davidnardie.pokemon.domain.repository.SettingsRepository
import com.davidnardie.pokemon.presentation.intent.PokemonIntent
import com.davidnardie.pokemon.presentation.state.AppSettings
import com.davidnardie.pokemon.presentation.state.NavigationTab
import com.davidnardie.pokemon.presentation.state.PokemonListState
import com.davidnardie.pokemon.presentation.util.TeamDateFormatter
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
    private val settingsRepository: SettingsRepository,
) {
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _uiState = MutableStateFlow(
        PokemonListState(settings = settingsRepository.loadSettings()),
    )
    val uiState: StateFlow<PokemonListState> = _uiState.asStateFlow()

    fun processIntent(intent: PokemonIntent) {
        when (intent) {
            is PokemonIntent.LoadPokemonList -> loadPokemonList(
                limit = intent.limit,
                offset = intent.offset,
            )
            PokemonIntent.LoadNextPage -> loadNextPage()
            PokemonIntent.RefreshPokemonList -> refreshPokemonList()
            PokemonIntent.OpenSettings -> _uiState.update { it.copy(isSettingsVisible = true) }
            PokemonIntent.CloseSettings -> _uiState.update {
                it.copy(
                    isSettingsVisible = false,
                    isModePickerVisible = false,
                    isAboutVisible = false,
                )
            }
            is PokemonIntent.SelectTab -> {
                _uiState.update { it.copy(selectedTab = intent.tab) }
                if (intent.tab == NavigationTab.Team) {
                    ensureTeamMemberTypes()
                }
            }
            is PokemonIntent.ToggleTeamMembership -> toggleTeamMembership(intent.pokemonId)
            is PokemonIntent.OpenPokemonDetail -> openPokemonDetail(intent.pokemonId)
            PokemonIntent.ClosePokemonDetail -> _uiState.update {
                it.copy(
                    selectedPokemon = null,
                    selectedPokemonId = null,
                    isDetailLoading = false,
                    detailLoadFailed = false,
                )
            }
            PokemonIntent.ToggleMetricUnits -> updateSettings { settings ->
                settings.copy(useMetricUnits = !settings.useMetricUnits)
            }
            is PokemonIntent.SetMetricUnits -> updateSettings { settings ->
                settings.copy(useMetricUnits = intent.useMetric)
            }
            PokemonIntent.ToggleTimeFormat -> updateSettings { settings ->
                settings.copy(use24HourTime = !settings.use24HourTime)
            }
            is PokemonIntent.SetUse24HourTime -> updateSettings { settings ->
                settings.copy(use24HourTime = intent.use24HourTime)
            }
            PokemonIntent.OpenModePicker -> _uiState.update { it.copy(isModePickerVisible = true) }
            PokemonIntent.CloseModePicker -> _uiState.update { it.copy(isModePickerVisible = false) }
            is PokemonIntent.SelectThemeMode -> {
                updateSettings { settings ->
                    settings.copy(themeMode = intent.themeMode)
                }
                _uiState.update { it.copy(isModePickerVisible = false) }
            }
            PokemonIntent.OpenAbout -> _uiState.update { it.copy(isAboutVisible = true) }
            PokemonIntent.CloseAbout -> _uiState.update { it.copy(isAboutVisible = false) }
            PokemonIntent.CloseTeamFullDialog -> _uiState.update { it.copy(showTeamFullDialog = false) }
        }
    }

    private fun updateSettings(transform: (AppSettings) -> AppSettings) {
        val updatedSettings = transform(_uiState.value.settings)
        settingsRepository.saveSettings(updatedSettings)
        _uiState.update { it.copy(settings = updatedSettings) }
    }

    fun onCleared() {
        viewModelScope.cancel()
    }

    private fun toggleTeamMembership(pokemonId: Int) {
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

        if (state.teamMembers.size >= PokemonListState.MAX_TEAM_SIZE) {
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
                    pokemonDetails = current.pokemonDetails + (pokemonId to detail),
                )
            }
        }
    }

    private fun openPokemonDetail(pokemonId: Int) {
        val pokemon = _uiState.value.findPokemon(pokemonId)
        if (pokemon == null) {
            _uiState.update {
                it.copy(
                    selectedPokemon = null,
                    selectedPokemonId = null,
                    isDetailLoading = false,
                    detailLoadFailed = false,
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                selectedPokemon = pokemon,
                selectedPokemonId = pokemonId,
                detailLoadFailed = false,
            )
        }
        fetchPokemonDetail(pokemonId)
    }

    private fun fetchPokemonDetail(pokemonId: Int) {
        if (_uiState.value.pokemonDetails.containsKey(pokemonId)) {
            _uiState.update { it.copy(isDetailLoading = false, detailLoadFailed = false) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isDetailLoading = true, detailLoadFailed = false) }
            val detail = repository.fetchPokemonDetail(pokemonId)
            _uiState.update { current ->
                current.copy(
                    isDetailLoading = false,
                    detailLoadFailed = detail == null,
                    pokemonDetails = detail?.let { current.pokemonDetails + (pokemonId to it) }
                        ?: current.pokemonDetails,
                    pokemonTypes = detail?.let { current.pokemonTypes + (pokemonId to it.types) }
                        ?: current.pokemonTypes,
                )
            }
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

    private fun refreshPokemonList() {
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
                        pokemonDetails = emptyMap(),
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

    private fun loadNextPage() {
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

    private companion object {
        const val PAGE_SIZE = 20
        const val OFFLINE_ERROR_MESSAGE = "Check your connection"
    }
}

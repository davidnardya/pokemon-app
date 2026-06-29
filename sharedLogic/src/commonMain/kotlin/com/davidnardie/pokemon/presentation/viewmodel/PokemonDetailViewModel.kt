package com.davidnardie.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidnardie.pokemon.domain.repository.PokemonRepository
import com.davidnardie.pokemon.domain.repository.SettingsRepository
import com.davidnardie.pokemon.presentation.state.PokemonDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val pokemonId: Int,
    private val repository: PokemonRepository,
    settingsRepository: SettingsRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        PokemonDetailUiState(
            pokemonId = pokemonId,
            useMetricUnits = settingsRepository.loadSettings().useMetricUnits,
        ),
    )
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _uiState.update { it.copy(useMetricUnits = settings.useMetricUnits) }
            }
        }
        fetchDetail()
    }

    private fun fetchDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loadFailed = false) }
            val detail = repository.fetchPokemonDetail(pokemonId)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    detail = detail,
                    loadFailed = detail == null,
                )
            }
        }
    }
}

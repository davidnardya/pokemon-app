package com.davidnardie.pokemon.presentation.state

import com.davidnardie.pokemon.domain.model.PokemonDetail

data class PokemonDetailUiState(
    val pokemonId: Int,
    val detail: PokemonDetail? = null,
    val isLoading: Boolean = false,
    val loadFailed: Boolean = false,
    val useMetricUnits: Boolean = true,
)

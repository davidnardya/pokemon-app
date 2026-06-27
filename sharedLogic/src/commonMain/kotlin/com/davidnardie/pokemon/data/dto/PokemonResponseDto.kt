package com.davidnardie.pokemon.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PokemonResponseDto(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<PokemonDto>,
)

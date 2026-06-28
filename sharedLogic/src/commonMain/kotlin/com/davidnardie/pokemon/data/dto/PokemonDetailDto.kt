package com.davidnardie.pokemon.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PokemonDetailDto(
    val types: List<PokemonTypeSlotDto> = emptyList(),
)

@Serializable
data class PokemonTypeSlotDto(
    val type: PokemonNamedResourceDto,
)

@Serializable
data class PokemonNamedResourceDto(
    val name: String,
)

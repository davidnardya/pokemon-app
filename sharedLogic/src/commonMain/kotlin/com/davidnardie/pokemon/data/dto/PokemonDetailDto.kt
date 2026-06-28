package com.davidnardie.pokemon.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDetailDto(
    val types: List<PokemonTypeSlotDto> = emptyList(),
    val weight: Int = 0,
    val height: Int = 0,
    val abilities: List<PokemonAbilitySlotDto> = emptyList(),
    val stats: List<PokemonStatSlotDto> = emptyList(),
)

@Serializable
data class PokemonTypeSlotDto(
    val slot: Int = 0,
    val type: PokemonNamedResourceDto,
)

@Serializable
data class PokemonAbilitySlotDto(
    val ability: PokemonNamedResourceDto,
    @SerialName("is_hidden")
    val isHidden: Boolean = false,
)

@Serializable
data class PokemonStatSlotDto(
    @SerialName("base_stat")
    val baseStat: Int = 0,
    val stat: PokemonNamedResourceDto,
)

@Serializable
data class PokemonNamedResourceDto(
    val name: String,
)

package com.davidnardie.pokemon.data.mapper

import com.davidnardie.pokemon.data.dto.PokemonDetailDto
import com.davidnardie.pokemon.domain.model.PokemonDetail
import com.davidnardie.pokemon.domain.model.PokemonStatEntry
import com.davidnardie.pokemon.domain.model.PokemonStatType
import com.davidnardie.pokemon.domain.model.PokemonType
import com.davidnardie.pokemon.presentation.util.PokemonDetailFormatter

fun PokemonDetailDto.toDomain(pokemonId: Int): PokemonDetail {
    val mappedTypes = types.mapNotNull { slot ->
        PokemonType.fromApiName(slot.type.name)
    }
    val mappedStats = stats.mapNotNull { slot ->
        PokemonStatType.fromApiName(slot.stat.name)?.let { statType ->
            PokemonStatEntry(stat = statType, baseValue = slot.baseStat)
        }
    }
    val mappedAbilities = abilities.map { slot ->
        PokemonDetailFormatter.formatAbilityName(slot.ability.name)
    }

    return PokemonDetail(
        id = pokemonId,
        types = mappedTypes,
        weightHectograms = weight,
        heightDecimeters = height,
        abilities = mappedAbilities,
        stats = mappedStats,
    )
}

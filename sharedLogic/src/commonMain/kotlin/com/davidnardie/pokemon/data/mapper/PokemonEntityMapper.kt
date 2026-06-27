package com.davidnardie.pokemon.data.mapper

import com.davidnardie.pokemon.data.dto.PokemonDto
import com.davidnardie.pokemon.data.local.entity.PokemonEntity
import com.davidnardie.pokemon.domain.model.Pokemon

fun PokemonEntity.toDomain(): Pokemon =
    Pokemon(
        id = id,
        name = name,
    )

fun PokemonDto.toEntity(): PokemonEntity {
    val id = url.trimEnd('/').substringAfterLast('/').toInt()
    return PokemonEntity(
        id = id,
        name = name,
        url = url,
    )
}

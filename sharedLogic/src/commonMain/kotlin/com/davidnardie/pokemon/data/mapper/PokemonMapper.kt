package com.davidnardie.pokemon.data.mapper

import com.davidnardie.pokemon.data.dto.PokemonDto
import com.davidnardie.pokemon.domain.model.Pokemon

fun PokemonDto.toDomain(): Pokemon {
    val id = url.trimEnd('/').substringAfterLast('/').toInt()
    return Pokemon(
        id = id,
        name = name,
    )
}

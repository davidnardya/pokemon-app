package com.davidnardie.pokemon.data

import com.davidnardie.pokemon.data.dto.PokemonDto
import com.davidnardie.pokemon.data.mapper.toDomain
import kotlin.test.Test
import kotlin.test.assertEquals

class PokemonMapperTest {

    @Test
    fun toDomain_mapsPokemonDtoToDomainModel() {
        val dto = PokemonDto(
            name = "bulbasaur",
            url = "https://pokeapi.co/api/v2/pokemon/1/",
        )

        val pokemon = dto.toDomain()

        assertEquals(1, pokemon.id)
        assertEquals("bulbasaur", pokemon.name)
    }
}

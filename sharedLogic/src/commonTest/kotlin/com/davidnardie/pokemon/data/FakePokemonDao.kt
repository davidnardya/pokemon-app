package com.davidnardie.pokemon.data

import com.davidnardie.pokemon.data.local.dao.PokemonDao
import com.davidnardie.pokemon.data.local.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakePokemonDao : PokemonDao {
    private val pokemon = MutableStateFlow<List<PokemonEntity>>(emptyList())

    override suspend fun upsertAll(pokemon: List<PokemonEntity>) {
        this.pokemon.value = pokemon
    }

    override fun getAllPokemon(): Flow<List<PokemonEntity>> = pokemon.asStateFlow()

    override suspend fun getAllPokemonSync(): List<PokemonEntity> = pokemon.value

    override suspend fun deleteAll() {
        pokemon.value = emptyList()
    }
}

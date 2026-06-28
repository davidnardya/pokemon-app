package com.davidnardie.pokemon.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.davidnardie.pokemon.data.local.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Upsert
    suspend fun upsertAll(pokemon: List<PokemonEntity>)

    @Query("SELECT * FROM pokemon ORDER BY id ASC")
    fun getAllPokemon(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon ORDER BY id ASC")
    suspend fun getAllPokemonSync(): List<PokemonEntity>

    @Query("DELETE FROM pokemon")
    suspend fun deleteAll()
}

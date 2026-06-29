package com.davidnardie.pokemon.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.davidnardie.pokemon.data.local.dao.PokemonDao
import com.davidnardie.pokemon.data.local.entity.PokemonEntity

@Database(
    entities = [PokemonEntity::class],
    version = 1,
)
@ConstructedBy(PokemonDatabaseConstructor::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}

expect object PokemonDatabaseConstructor : RoomDatabaseConstructor<PokemonDatabase> {
    override fun initialize(): PokemonDatabase
}

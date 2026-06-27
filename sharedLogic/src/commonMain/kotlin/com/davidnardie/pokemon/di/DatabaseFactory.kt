package com.davidnardie.pokemon.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.davidnardie.pokemon.data.local.database.PokemonDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

expect fun getPokemonDatabaseBuilder(): RoomDatabase.Builder<PokemonDatabase>

fun buildPokemonDatabase(
    builder: RoomDatabase.Builder<PokemonDatabase>,
): PokemonDatabase =
    builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()

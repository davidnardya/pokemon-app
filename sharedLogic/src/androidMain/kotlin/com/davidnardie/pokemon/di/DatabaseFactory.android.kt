package com.davidnardie.pokemon.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.davidnardie.pokemon.data.local.database.PokemonDatabase

private lateinit var applicationContext: Context

fun initDatabaseContext(context: Context) {
    applicationContext = context.applicationContext
}

actual fun getPokemonDatabaseBuilder(): RoomDatabase.Builder<PokemonDatabase> {
    val dbFile = applicationContext.getDatabasePath(POKEMON_DATABASE_NAME)
    return Room.databaseBuilder<PokemonDatabase>(
        context = applicationContext,
        name = dbFile.absolutePath,
    )
}

private const val POKEMON_DATABASE_NAME = "pokemon.db"

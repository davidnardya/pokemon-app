@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.davidnardie.pokemon.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.davidnardie.pokemon.data.local.database.PokemonDatabase
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual fun getPokemonDatabaseBuilder(): RoomDatabase.Builder<PokemonDatabase> {
    val dbFilePath = documentDirectory() + "/$POKEMON_DATABASE_NAME"
    return Room.databaseBuilder<PokemonDatabase>(
        name = dbFilePath,
    )
}

private fun documentDirectory(): String {
    val directory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(directory?.path)
}

private const val POKEMON_DATABASE_NAME = "pokemon.db"

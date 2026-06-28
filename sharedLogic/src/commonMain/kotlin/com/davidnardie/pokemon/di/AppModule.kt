package com.davidnardie.pokemon.di

import com.davidnardie.pokemon.data.local.database.PokemonDatabase
import com.davidnardie.pokemon.data.local.createSettingsStorage
import com.davidnardie.pokemon.data.repository.PokemonRepositoryImpl
import com.davidnardie.pokemon.data.repository.SettingsRepositoryImpl
import com.davidnardie.pokemon.domain.repository.PokemonRepository
import com.davidnardie.pokemon.domain.repository.SettingsRepository
import com.davidnardie.pokemon.presentation.viewmodel.PokemonViewModel
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

val appModule = module {
    single {
        createHttpClient {
            install(ContentNegotiation) {
                json(json)
            }
        }
    }
    single { buildPokemonDatabase(getPokemonDatabaseBuilder()) }
    single { get<PokemonDatabase>().pokemonDao() }
    single { createSettingsStorage() }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    single<PokemonRepository> { PokemonRepositoryImpl(get(), get()) }
    single { PokemonViewModel(get(), get()) }
}

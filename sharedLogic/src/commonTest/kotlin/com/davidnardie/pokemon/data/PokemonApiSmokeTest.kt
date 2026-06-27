package com.davidnardie.pokemon.data

import com.davidnardie.pokemon.data.repository.PokemonRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PokemonApiSmokeTest {

    @Test
    fun refresh_fetchesPokemonFromPokeApi() = runTest {
        val httpClient = createTestHttpClient()
        val database = FakePokemonDao()
        try {
            val repository = PokemonRepositoryImpl(httpClient, database)

            val page = repository.refresh(limit = 10, offset = 0)

            assertFalse(page.isOffline, "Expected a successful refresh from PokeAPI")
            assertTrue(page.pokemon.isNotEmpty(), "Expected a non-empty Pokémon page from PokeAPI")
            val pokemonList = repository.getPokemonList().first()
            assertTrue(pokemonList.isNotEmpty(), "Expected a non-empty Pokémon list from PokeAPI")
            assertTrue(
                pokemonList.first().name.isNotBlank(),
                "Expected the first Pokémon to have a non-blank name",
            )
        } finally {
            httpClient.close()
        }
    }

    private fun createTestHttpClient(): HttpClient =
        createSmokeTestHttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    },
                )
            }
        }
}

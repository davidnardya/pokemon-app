package com.davidnardie.pokemon.data.repository

import com.davidnardie.pokemon.data.dto.PokemonDetailDto
import com.davidnardie.pokemon.data.dto.PokemonResponseDto
import com.davidnardie.pokemon.data.local.dao.PokemonDao
import com.davidnardie.pokemon.data.mapper.toDomain
import com.davidnardie.pokemon.data.mapper.toEntity
import com.davidnardie.pokemon.domain.model.Pokemon
import com.davidnardie.pokemon.domain.model.PokemonDetail
import com.davidnardie.pokemon.domain.model.PokemonPage
import com.davidnardie.pokemon.domain.model.PokemonType
import com.davidnardie.pokemon.domain.repository.PokemonRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.io.IOException

class PokemonRepositoryImpl(
    private val httpClient: HttpClient,
    private val dao: PokemonDao,
) : PokemonRepository {

    private val detailCache = mutableMapOf<Int, PokemonDetail>()

    override fun getPokemonList(): Flow<List<Pokemon>> =
        dao.getAllPokemon().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun hardRefresh(limit: Int): PokemonPage {
        dao.deleteAll()
        detailCache.clear()
        return refresh(limit = limit, offset = 0)
    }

    override suspend fun refresh(limit: Int, offset: Int): PokemonPage {
        return try {
            val response = httpClient.get(POKEMON_LIST_URL) {
                parameter("limit", limit)
                parameter("offset", offset)
            }.body<PokemonResponseDto>()

            val entities = response.results.map { it.toEntity() }
            dao.upsertAll(entities)
            PokemonPage(
                pokemon = entities.map { it.toDomain() },
                isOffline = false,
                hasMore = response.next != null,
            )
        } catch (e: Exception) {
            if (e.isConnectivityError()) {
                val cachedPokemon = dao.getAllPokemonSync().map { it.toDomain() }
                PokemonPage(
                    pokemon = if (offset == 0) cachedPokemon else emptyList(),
                    isOffline = true,
                    hasMore = false,
                )
            } else {
                throw e
            }
        }
    }

    private fun Throwable.isConnectivityError(): Boolean {
        var current: Throwable? = this
        while (current != null) {
            when (current) {
                is IOException,
                is HttpRequestTimeoutException,
                -> return true
            }
            current = current.cause
        }
        return false
    }

    override suspend fun fetchPokemonTypes(pokemonId: Int): List<PokemonType> {
        return fetchPokemonDetail(pokemonId)?.types.orEmpty()
    }

    override suspend fun fetchPokemonDetail(pokemonId: Int): PokemonDetail? {
        detailCache[pokemonId]?.let { return it }
        return try {
            val response = httpClient.get("$POKEMON_LIST_URL/$pokemonId")
                .body<PokemonDetailDto>()
            response.toDomain(pokemonId).also { detail ->
                detailCache[pokemonId] = detail
            }
        } catch (_: Exception) {
            null
        }
    }

    private companion object {
        const val POKEMON_LIST_URL = "https://pokeapi.co/api/v2/pokemon"
    }
}

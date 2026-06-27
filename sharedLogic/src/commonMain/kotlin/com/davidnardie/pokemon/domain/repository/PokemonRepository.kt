package com.davidnardie.pokemon.domain.repository

import com.davidnardie.pokemon.domain.model.Pokemon
import com.davidnardie.pokemon.domain.model.PokemonPage
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(): Flow<List<Pokemon>>

    /**
     * Fetches a page from the API, updates local storage, and returns the page result.
     * @return Page data; [PokemonPage.isOffline] is `true` when the fetch failed due to connectivity.
     */
    suspend fun refresh(limit: Int, offset: Int): PokemonPage
}

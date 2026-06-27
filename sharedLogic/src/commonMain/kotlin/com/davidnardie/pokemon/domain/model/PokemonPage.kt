package com.davidnardie.pokemon.domain.model

data class PokemonPage(
    val pokemon: List<Pokemon>,
    val isOffline: Boolean,
    val hasMore: Boolean,
)

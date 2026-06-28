package com.davidnardie.pokemon.domain.model

data class PokemonDetail(
    val id: Int,
    val types: List<PokemonType>,
    val weightHectograms: Int,
    val heightDecimeters: Int,
    val abilities: List<String>,
    val stats: List<PokemonStatEntry>,
) {
    val primaryType: PokemonType?
        get() = types.firstOrNull()
}

package com.davidnardie.pokemon.presentation.model

import com.davidnardie.pokemon.domain.model.Pokemon
import com.davidnardie.pokemon.domain.model.PokemonType

data class TeamPokemonItem(
    val pokemon: Pokemon,
    val types: List<PokemonType>,
    val addedDateLabel: String,
)

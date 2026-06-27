package com.davidnardie.pokemon.presentation.effect

sealed interface PokemonSideEffect {
    data object ShowTeamFullError : PokemonSideEffect
}

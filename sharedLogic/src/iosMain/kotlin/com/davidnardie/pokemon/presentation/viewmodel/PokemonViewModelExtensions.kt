package com.davidnardie.pokemon.presentation.viewmodel

import com.davidnardie.pokemon.domain.model.Pokemon
import com.davidnardie.pokemon.presentation.intent.PokemonIntent
import com.davidnardie.pokemon.presentation.state.NavigationTab
import com.davidnardie.pokemon.presentation.state.PokemonListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun PokemonListState.toPokemonArray(): List<Pokemon> = pokemon.toList()

fun PokemonListState.isPokemonInTeam(pokemonId: Int): Boolean = isInTeam(pokemonId)

fun PokemonViewModel.currentUiState(): PokemonListState = uiState.value

fun PokemonViewModel.watchUiState(onState: (PokemonListState) -> Unit): () -> Unit {
    val job: Job = CoroutineScope(Dispatchers.Main).launch {
        uiState.collect { state ->
            onState(state)
        }
    }
    return { job.cancel() }
}

fun PokemonViewModel.loadInitialList() {
    processIntent(PokemonIntent.LoadPokemonList())
}

fun PokemonViewModel.requestNextPage() {
    processIntent(PokemonIntent.LoadNextPage)
}

fun PokemonViewModel.openSettings() {
    processIntent(PokemonIntent.OpenSettings)
}

fun PokemonViewModel.closeSettings() {
    processIntent(PokemonIntent.CloseSettings)
}

fun PokemonViewModel.selectTab(tab: NavigationTab) {
    processIntent(PokemonIntent.SelectTab(tab))
}

fun PokemonViewModel.toggleTeamMembership(pokemonId: Int) {
    processIntent(PokemonIntent.ToggleTeamMembership(pokemonId))
}

fun PokemonViewModel.openPokemonDetail(pokemon: Pokemon) {
    processIntent(PokemonIntent.OpenPokemonDetail(pokemon))
}

fun PokemonViewModel.closePokemonDetail() {
    processIntent(PokemonIntent.ClosePokemonDetail)
}

fun PokemonViewModel.toggleMetricUnits() {
    processIntent(PokemonIntent.ToggleMetricUnits)
}

fun PokemonViewModel.toggleTimeFormat() {
    processIntent(PokemonIntent.ToggleTimeFormat)
}

fun PokemonViewModel.toggleTheme() {
    processIntent(PokemonIntent.ToggleTheme)
}

fun PokemonViewModel.closeTeamFullDialog() {
    processIntent(PokemonIntent.CloseTeamFullDialog)
}

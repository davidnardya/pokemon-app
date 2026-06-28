package com.davidnardie.pokemon.presentation.viewmodel

import com.davidnardie.pokemon.domain.model.Pokemon
import com.davidnardie.pokemon.domain.model.PokemonDetail
import com.davidnardie.pokemon.domain.model.PokemonStatEntry
import com.davidnardie.pokemon.domain.model.PokemonType
import com.davidnardie.pokemon.presentation.intent.PokemonIntent
import com.davidnardie.pokemon.presentation.model.TeamPokemonItem
import com.davidnardie.pokemon.presentation.state.NavigationTab
import com.davidnardie.pokemon.presentation.state.ThemeMode
import com.davidnardie.pokemon.presentation.state.PokemonListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun PokemonListState.toPokemonArray(): List<Pokemon> = pokemon.toList()

fun PokemonListState.isPokemonInTeam(pokemonId: Int): Boolean = isInTeam(pokemonId)

fun PokemonListState.toTeamPokemonItemsArray(): List<TeamPokemonItem> = teamPokemonItems()

fun PokemonListState.detailForPokemon(pokemonId: Int): PokemonDetail? = findDetail(pokemonId)

fun PokemonDetail.toTypesArray(): List<PokemonType> = types

fun PokemonDetail.toStatsArray(): List<PokemonStatEntry> = stats

fun PokemonDetail.toAbilitiesArray(): List<String> = abilities

fun PokemonStatEntry.statValue(): Int = baseValue

fun PokemonStatEntry.statLabel(): String = stat.label

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
    processIntent(PokemonIntent.OpenPokemonDetail(pokemonId = pokemon.id))
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

fun PokemonViewModel.setMetricUnits(useMetric: Boolean) {
    processIntent(PokemonIntent.SetMetricUnits(useMetric = useMetric))
}

fun PokemonViewModel.setUse24HourTime(use24HourTime: Boolean) {
    processIntent(PokemonIntent.SetUse24HourTime(use24HourTime = use24HourTime))
}

fun PokemonViewModel.openModePicker() {
    processIntent(PokemonIntent.OpenModePicker)
}

fun PokemonViewModel.closeModePicker() {
    processIntent(PokemonIntent.CloseModePicker)
}

fun PokemonViewModel.selectThemeMode(themeMode: ThemeMode) {
    processIntent(PokemonIntent.SelectThemeMode(themeMode = themeMode))
}

fun PokemonViewModel.openAbout() {
    processIntent(PokemonIntent.OpenAbout)
}

fun PokemonViewModel.closeAbout() {
    processIntent(PokemonIntent.CloseAbout)
}

fun PokemonViewModel.closeTeamFullDialog() {
    processIntent(PokemonIntent.CloseTeamFullDialog)
}

/**
 * SwiftUI bridge helpers for observing shared ViewModels and adapting state/models for iOS.
 *
 * This file lives in `iosMain` and is consumed from Swift (`iosApp/`), e.g.
 * `PokemonListViewModelWrapper`, `SettingsViewModelWrapper`, and `PokemonDetailViewModelWrapper`.
 * Swift cannot use Kotlin `StateFlow` directly in SwiftUI, so these extensions provide:
 * - `currentUiState()` for an initial snapshot when a wrapper is created
 * - `watchUiState()` to subscribe to updates and push them into `@Published` properties
 * - Small adapters (`toPokemonArray()`, `toTypesArray()`, etc.) used from Swift extensions
 *
 * Android Studio may report everything here as unused because it only indexes Kotlin references
 * inside the Gradle project. Call sites are in Swift/Xcode, which the IDE does not analyze.
 * On Android/Compose, screens use `collectAsState()` on the same ViewModels instead — no bridge needed.
 */
package com.davidnardie.pokemon.presentation.viewmodel

import com.davidnardie.pokemon.domain.model.Pokemon
import com.davidnardie.pokemon.domain.model.PokemonDetail
import com.davidnardie.pokemon.domain.model.PokemonStatEntry
import com.davidnardie.pokemon.domain.model.PokemonType
import com.davidnardie.pokemon.presentation.model.TeamPokemonItem
import com.davidnardie.pokemon.presentation.state.PokemonDetailUiState
import com.davidnardie.pokemon.presentation.state.PokemonListUiState
import com.davidnardie.pokemon.presentation.state.SettingsUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun PokemonListUiState.toPokemonArray(): List<Pokemon> = pokemon.toList()

fun PokemonListUiState.isPokemonInTeam(pokemonId: Int): Boolean = isInTeam(pokemonId)

fun PokemonListUiState.toTeamPokemonItemsArray(): List<TeamPokemonItem> = teamPokemonItems()

fun PokemonDetail.toTypesArray(): List<PokemonType> = types

fun PokemonDetail.toStatsArray(): List<PokemonStatEntry> = stats

fun PokemonDetail.toAbilitiesArray(): List<String> = abilities

fun PokemonStatEntry.statValue(): Int = baseValue

fun PokemonStatEntry.statLabel(): String = stat.label

fun PokemonListViewModel.currentUiState(): PokemonListUiState = uiState.value

fun SettingsViewModel.currentUiState(): SettingsUiState = uiState.value

fun PokemonDetailViewModel.currentUiState(): PokemonDetailUiState = uiState.value

fun <T> watchStateFlow(
    stateFlow: StateFlow<T>,
    onState: (T) -> Unit,
): () -> Unit {
    val job: Job = CoroutineScope(Dispatchers.Main).launch {
        stateFlow.collect { state ->
            onState(state)
        }
    }
    return { job.cancel() }
}

fun PokemonListViewModel.watchUiState(onState: (PokemonListUiState) -> Unit): () -> Unit =
    watchStateFlow(uiState, onState)

fun SettingsViewModel.watchUiState(onState: (SettingsUiState) -> Unit): () -> Unit =
    watchStateFlow(uiState, onState)

fun PokemonDetailViewModel.watchUiState(onState: (PokemonDetailUiState) -> Unit): () -> Unit =
    watchStateFlow(uiState, onState)

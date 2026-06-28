package com.davidnardie.pokemon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.davidnardie.pokemon.domain.model.Pokemon
import com.davidnardie.pokemon.presentation.state.AppSettings
import com.davidnardie.pokemon.presentation.state.NavigationTab
import com.davidnardie.pokemon.presentation.state.PokemonListState
import com.davidnardie.pokemon.presentation.viewmodel.PokemonViewModel
import org.koin.compose.koinInject
import ui.pokemonlist.PokemonMainListContent
import ui.pokemonlist.PokemonMainListScreen
import ui.theme.PokemonAppTheme
import ui.theme.SyncPlatformThemeMode

@Composable
fun App() {
    val viewModel: PokemonViewModel = koinInject()
    val uiState by viewModel.uiState.collectAsState()

    SyncPlatformThemeMode(uiState.settings.themeMode)

    PokemonAppTheme(themeMode = uiState.settings.themeMode) {
        PokemonMainListScreen(viewModel = viewModel)
    }
}

@Preview
@Composable
private fun PokemonMainListPreview() {
    PokemonAppTheme {
        PokemonMainListContent(
            uiState = PokemonListState(
                pokemon = listOf(
                    Pokemon(id = 1, name = "bulbasaur"),
                    Pokemon(id = 2, name = "ivysaur"),
                    Pokemon(id = 3, name = "venusaur"),
                    Pokemon(id = 4, name = "charmander"),
                ),
                selectedTab = NavigationTab.Team,
                teamMembers = listOf(
                    com.davidnardie.pokemon.domain.model.TeamMember(
                        pokemonId = 1,
                        addedMonth = 12,
                        addedDay = 5,
                    ),
                ),
                settings = AppSettings(),
            ),
            onSettingsClick = {},
            onTabSelected = {},
            onLoadNextPage = {},
            onRefresh = {},
            onPokemonClick = {},
            onToggleTeam = {},
        )
    }
}

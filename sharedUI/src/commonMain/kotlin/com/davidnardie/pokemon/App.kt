package com.davidnardie.pokemon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.davidnardie.pokemon.domain.model.Pokemon
import com.davidnardie.pokemon.presentation.state.AppSettings
import com.davidnardie.pokemon.presentation.state.NavigationTab
import com.davidnardie.pokemon.presentation.state.PokemonListUiState
import com.davidnardie.pokemon.presentation.viewmodel.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel
import ui.pokemonlist.PokemonMainListContent
import ui.pokemonlist.PokemonMainListScreen
import ui.theme.PokemonAppTheme
import ui.theme.SyncPlatformThemeMode

@Composable
fun App() {
    val settingsViewModel: SettingsViewModel = koinViewModel()
    val settingsState by settingsViewModel.uiState.collectAsState()

    SyncPlatformThemeMode(settingsState.settings.themeMode)

    PokemonAppTheme(themeMode = settingsState.settings.themeMode) {
        PokemonMainListScreen(settingsViewModel = settingsViewModel)
    }
}

@Preview
@Composable
private fun PokemonMainListPreview() {
    PokemonAppTheme {
        PokemonMainListContent(
            listState = PokemonListUiState(
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
            ),
            settings = AppSettings(),
            isSettingsVisible = false,
            isModePickerVisible = false,
            isAboutVisible = false,
            onSettingsClick = {},
            onTabSelected = {},
            onLoadNextPage = {},
            onRefresh = {},
            onPokemonClick = {},
            onToggleTeam = {},
        )
    }
}

package com.davidnardie.pokemon

import androidx.compose.runtime.Composable
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

@Composable
fun App() {
    PokemonAppTheme {
        val viewModel: PokemonViewModel = koinInject()
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
                selectedTab = NavigationTab.Pokemon,
                teamPokemonIds = listOf(1),
                settings = AppSettings(),
            ),
            onSettingsClick = {},
            onTabSelected = {},
            onLoadNextPage = {},
            onPokemonClick = {},
            onToggleTeam = {},
        )
    }
}

package ui.pokemonlist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.davidnardie.pokemon.domain.model.Pokemon
import com.davidnardie.pokemon.presentation.intent.PokemonIntent
import com.davidnardie.pokemon.presentation.state.NavigationTab
import com.davidnardie.pokemon.presentation.state.PokemonListState
import com.davidnardie.pokemon.presentation.viewmodel.PokemonViewModel
import org.jetbrains.compose.resources.painterResource
import ui.theme.PokemonCardTheme
import pokemonapp.sharedui.generated.resources.Res
import pokemonapp.sharedui.generated.resources.ic_darkmode_settings
import pokemonapp.sharedui.generated.resources.ic_darkmode_switch_pokemon_empty
import pokemonapp.sharedui.generated.resources.ic_darkmode_switch_pokemon_full
import pokemonapp.sharedui.generated.resources.ic_darkmode_switch_team_empty
import pokemonapp.sharedui.generated.resources.ic_darkmode_switch_team_full
import pokemonapp.sharedui.generated.resources.ic_lightmode_settings
import pokemonapp.sharedui.generated.resources.ic_lightmode_switch_pokemon_empty
import pokemonapp.sharedui.generated.resources.ic_lightmode_switch_pokemon_full
import pokemonapp.sharedui.generated.resources.ic_lightmode_switch_team_empty
import pokemonapp.sharedui.generated.resources.ic_lightmode_switch_team_full
import pokemonapp.sharedui.generated.resources.ic_loading_pokeball_orange
import pokemonapp.sharedui.generated.resources.ic_pokeball_dark_grey
import pokemonapp.sharedui.generated.resources.ic_pokeball_grey

@Composable
fun PokemonMainListScreen(
    viewModel: PokemonViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.processIntent(PokemonIntent.LoadPokemonList())
    }

    val selectedPokemon = uiState.selectedPokemon
    if (selectedPokemon != null) {
        BackHandler {
            viewModel.processIntent(PokemonIntent.ClosePokemonDetail)
        }
        PokemonDetailScreen(
            pokemon = selectedPokemon,
            isLoading = uiState.isLoading,
            networkError = uiState.networkError,
            isCacheEmpty = uiState.isCacheEmpty,
            onBackClick = { viewModel.processIntent(PokemonIntent.ClosePokemonDetail) },
            modifier = modifier,
        )
        return
    }

    PokemonMainListContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onSettingsClick = { viewModel.processIntent(PokemonIntent.OpenSettings) },
        onCloseSettings = { viewModel.processIntent(PokemonIntent.CloseSettings) },
        onTabSelected = { tab -> viewModel.processIntent(PokemonIntent.SelectTab(tab)) },
        onLoadNextPage = { viewModel.processIntent(PokemonIntent.LoadNextPage) },
        onPokemonClick = { pokemon -> viewModel.processIntent(PokemonIntent.OpenPokemonDetail(pokemon)) },
        onToggleTeam = { pokemonId -> viewModel.processIntent(PokemonIntent.ToggleTeamMembership(pokemonId)) },
        onToggleMetricUnits = { viewModel.processIntent(PokemonIntent.ToggleMetricUnits) },
        onToggleTimeFormat = { viewModel.processIntent(PokemonIntent.ToggleTimeFormat) },
        onToggleTheme = { viewModel.processIntent(PokemonIntent.ToggleTheme) },
        onCloseTeamFullDialog = { viewModel.processIntent(PokemonIntent.CloseTeamFullDialog) },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonMainListContent(
    uiState: PokemonListState,
    onSettingsClick: () -> Unit,
    onTabSelected: (NavigationTab) -> Unit,
    onLoadNextPage: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onCloseSettings: () -> Unit = {},
    onPokemonClick: (Pokemon) -> Unit = {},
    onToggleTeam: (Int) -> Unit = {},
    onToggleMetricUnits: () -> Unit = {},
    onToggleTimeFormat: () -> Unit = {},
    onToggleTheme: () -> Unit = {},
    onCloseTeamFullDialog: () -> Unit = {},
) {
    val isDarkTheme = isSystemInDarkTheme()
    val colors = rememberPokemonListColors(isDarkTheme)
    val displayedPokemon = uiState.displayedPokemon()
    val showPagination = uiState.selectedTab == NavigationTab.Pokemon
    val showFullScreenLoadingState = uiState.shouldShowFullScreenLoadingState()

    Box(modifier = modifier.fillMaxSize()) {
        if (showFullScreenLoadingState) {
            PokemonLoadingView(
                uiState = uiState,
                isDarkTheme = isDarkTheme,
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.screenBackground),
            ) {
                // Shell hidden during initial load and network-error empty state.
            }
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = colors.screenBackground,
                contentWindowInsets = WindowInsets.statusBars,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    PokemonListTopBar(
                        title = if (uiState.selectedTab == NavigationTab.Team) "Team" else "Pokemon",
                        isDarkTheme = isDarkTheme,
                        colors = colors,
                        onSettingsClick = onSettingsClick,
                    )
                },
                bottomBar = {
                    PokemonAndroidBottomNavigation(
                        selectedTab = uiState.selectedTab,
                        isDarkTheme = isDarkTheme,
                        colors = colors,
                        onTabSelected = onTabSelected,
                    )
                },
            ) { innerPadding ->
                val contentModifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)

                PokemonGrid(
                    pokemon = displayedPokemon,
                    teamPokemonIds = uiState.teamPokemonIds,
                    isDarkTheme = isDarkTheme,
                    colors = colors,
                    isLoading = uiState.isLoading,
                    hasMore = uiState.hasMore && showPagination,
                    onLoadNextPage = onLoadNextPage,
                    onPokemonClick = onPokemonClick,
                    onToggleTeam = onToggleTeam,
                    modifier = contentModifier,
                )
            }
        }

        if (uiState.isSettingsVisible && !showFullScreenLoadingState) {
            SettingsDrawer(
                settings = uiState.settings,
                onClose = onCloseSettings,
                onToggleMetricUnits = onToggleMetricUnits,
                onToggleTimeFormat = onToggleTimeFormat,
                onToggleTheme = onToggleTheme,
            )
        }

        if (uiState.showTeamFullDialog) {
            FullTeamDialog(onDismiss = onCloseTeamFullDialog)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonListTopBar(
    title: String,
    isDarkTheme: Boolean,
    colors: PokemonListColors,
    onSettingsClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = colors.primaryText,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
            )
        },
        actions = {
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(colors.settingsButtonBackground),
            ) {
                Icon(
                    painter = painterResource(
                        if (isDarkTheme) Res.drawable.ic_darkmode_settings
                        else Res.drawable.ic_lightmode_settings,
                    ),
                    contentDescription = "Settings",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp),
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colors.screenBackground,
            scrolledContainerColor = colors.screenBackground,
        ),
        windowInsets = WindowInsets.statusBars,
    )
}

@Composable
private fun PokemonAndroidBottomNavigation(
    selectedTab: NavigationTab,
    isDarkTheme: Boolean,
    colors: PokemonListColors,
    onTabSelected: (NavigationTab) -> Unit,
) {
    NavigationBar(
        containerColor = colors.navigationBarBackground,
        tonalElevation = 0.dp,
    ) {
        NavigationBarItem(
            selected = selectedTab == NavigationTab.Pokemon,
            onClick = { onTabSelected(NavigationTab.Pokemon) },
            icon = {
                Icon(
                    painter = painterResource(
                        pokemonTabIcon(
                            isDarkTheme = isDarkTheme,
                            isSelected = selectedTab == NavigationTab.Pokemon,
                        ),
                    ),
                    contentDescription = "Pokemon",
                    tint = Color.Unspecified,
                )
            },
            label = {
                Text(
                    text = "Pokemon",
                    fontWeight = if (selectedTab == NavigationTab.Pokemon) FontWeight.SemiBold else FontWeight.Normal,
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colors.navigationSelectedContent,
                unselectedIconColor = colors.navigationUnselectedContent,
                selectedTextColor = Color.Black,
                unselectedTextColor = Color.Black,
                indicatorColor = colors.navigationIndicator,
            ),
        )
        NavigationBarItem(
            selected = selectedTab == NavigationTab.Team,
            onClick = { onTabSelected(NavigationTab.Team) },
            icon = {
                Icon(
                    painter = painterResource(
                        teamTabIcon(
                            isDarkTheme = isDarkTheme,
                            isSelected = selectedTab == NavigationTab.Team,
                        ),
                    ),
                    contentDescription = "Team",
                    tint = Color.Unspecified,
                )
            },
            label = {
                Text(
                    text = "Team",
                    fontWeight = if (selectedTab == NavigationTab.Team) FontWeight.SemiBold else FontWeight.Normal,
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colors.navigationSelectedContent,
                unselectedIconColor = colors.navigationUnselectedContent,
                selectedTextColor = Color.Black,
                unselectedTextColor = Color.Black,
                indicatorColor = colors.navigationIndicator,
            ),
        )
    }
}

@Composable
private fun PokemonGrid(
    pokemon: List<Pokemon>,
    teamPokemonIds: List<Int>,
    isDarkTheme: Boolean,
    colors: PokemonListColors,
    isLoading: Boolean,
    hasMore: Boolean,
    onLoadNextPage: () -> Unit,
    onPokemonClick: (Pokemon) -> Unit,
    onToggleTeam: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val rows = pokemon.chunked(2)
    val listState = rememberLazyListState()
    val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

    LaunchedEffect(lastVisibleItemIndex, rows.size, isLoading, hasMore) {
        if (lastVisibleItemIndex >= rows.size - 1 && !isLoading && hasMore) {
            onLoadNextPage()
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            items = rows,
            key = { row -> row.first().id },
        ) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                row.forEach { pokemonItem ->
                    PokemonCard(
                        pokemon = pokemonItem,
                        isInTeam = pokemonItem.id in teamPokemonIds,
                        isDarkTheme = isDarkTheme,
                        onPokemonClick = { onPokemonClick(pokemonItem) },
                        onToggleTeam = { onToggleTeam(pokemonItem.id) },
                        modifier = Modifier.weight(1f),
                    )
                }
                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun PokemonCard(
    pokemon: Pokemon,
    isInTeam: Boolean,
    isDarkTheme: Boolean,
    onPokemonClick: () -> Unit,
    onToggleTeam: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val usesDistinctImageSurface = PokemonCardTheme.usesDistinctImageSurface
    val cardShape = RoundedCornerShape(24.dp)
    val imageAreaShape = RoundedCornerShape(20.dp)

    Column(
        modifier = modifier
            .clip(cardShape)
            .background(PokemonCardTheme.containerColor)
            .clickable(onClick = onPokemonClick)
            .padding(if (usesDistinctImageSurface) 8.dp else 12.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .then(
                    if (usesDistinctImageSurface) {
                        Modifier
                            .clip(imageAreaShape)
                            .background(PokemonCardTheme.imageSurfaceColor)
                    } else {
                        Modifier
                    },
                ),
        ) {
            Image(
                painter = painterResource(
                    if (isInTeam) {
                        Res.drawable.ic_loading_pokeball_orange
                    } else if (isDarkTheme) {
                        Res.drawable.ic_pokeball_dark_grey
                    } else {
                        Res.drawable.ic_pokeball_grey
                    },
                ),
                contentDescription = if (isInTeam) "Remove from team" else "Add to team",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .size(24.dp)
                    .clickable(onClick = onToggleTeam),
            )
            AsyncImage(
                model = pokemonImageUrl(pokemon.id),
                contentDescription = pokemon.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                contentScale = ContentScale.Fit,
            )
        }
        Column(
            modifier = Modifier.padding(
                start = if (usesDistinctImageSurface) 4.dp else 0.dp,
                top = 10.dp,
                bottom = if (usesDistinctImageSurface) 4.dp else 0.dp,
            ),
        ) {
            Text(
                text = pokemon.displayName(),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = pokemon.formattedId(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

private fun pokemonTabIcon(isDarkTheme: Boolean, isSelected: Boolean) = when {
    isSelected && isDarkTheme -> Res.drawable.ic_darkmode_switch_pokemon_full
    isSelected -> Res.drawable.ic_lightmode_switch_pokemon_full
    isDarkTheme -> Res.drawable.ic_darkmode_switch_pokemon_empty
    else -> Res.drawable.ic_lightmode_switch_pokemon_empty
}

private fun teamTabIcon(isDarkTheme: Boolean, isSelected: Boolean) = when {
    isSelected && isDarkTheme -> Res.drawable.ic_darkmode_switch_team_full
    isSelected -> Res.drawable.ic_lightmode_switch_team_full
    isDarkTheme -> Res.drawable.ic_darkmode_switch_team_empty
    else -> Res.drawable.ic_lightmode_switch_team_empty
}

internal fun pokemonImageUrl(id: Int): String =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

private fun Pokemon.displayName(): String =
    name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

private fun Pokemon.formattedId(): String = "#%03d".format(id)

internal data class PokemonListColors(
    val screenBackground: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val settingsButtonBackground: Color,
    val navigationBarBackground: Color,
    val navigationIndicator: Color,
    val navigationSelectedContent: Color,
    val navigationUnselectedContent: Color,
)

@Composable
internal fun rememberPokemonListColors(isDarkTheme: Boolean): PokemonListColors {
    return if (isDarkTheme) {
        PokemonListColors(
            screenBackground = Color(0xFF000000),
            primaryText = Color(0xFFFFFFFF),
            secondaryText = Color(0xFF8E8E93),
            settingsButtonBackground = Color(0xFF2C2C2E),
            navigationBarBackground = Color(0xFF1C1C1E),
            navigationIndicator = Color(0xFF3A3A3C),
            navigationSelectedContent = Color(0xFFFFFFFF),
            navigationUnselectedContent = Color(0xFF8E8E93),
        )
    } else {
        PokemonListColors(
            screenBackground = Color(0xFFEDEDED),
            primaryText = Color(0xFF1A1A1A),
            secondaryText = Color(0xFF8E8E93),
            settingsButtonBackground = Color(0xFFFFFFFF),
            navigationBarBackground = Color(0xFFF2F2F7),
            navigationIndicator = Color(0xFFE3E3E8),
            navigationSelectedContent = Color(0xFF1A1A1A),
            navigationUnselectedContent = Color(0xFF8E8E93),
        )
    }
}

package ui.pokemonlist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import ui.theme.isPokemonDarkTheme
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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.davidnardie.pokemon.domain.model.Pokemon
import com.davidnardie.pokemon.presentation.state.NavigationTab
import com.davidnardie.pokemon.presentation.state.PokemonListUiState
import com.davidnardie.pokemon.presentation.viewmodel.PokemonDetailViewModel
import com.davidnardie.pokemon.presentation.viewmodel.PokemonListViewModel
import com.davidnardie.pokemon.presentation.viewmodel.SettingsViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
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
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
    listViewModel: PokemonListViewModel = koinViewModel(),
) {
    val listState by listViewModel.uiState.collectAsState()
    val settingsState by settingsViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val pokemonListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        listViewModel.loadInitialList()
    }

    val selectedPokemon = listState.selectedPokemonOrNull()
    val selectedPokemonId = listState.selectedPokemonId

    Box(modifier = modifier.fillMaxSize()) {
        PokemonMainListContent(
            listState = listState,
            settings = settingsState.settings,
            isSettingsVisible = settingsState.isVisible,
            isModePickerVisible = settingsState.isModePickerVisible,
            isAboutVisible = settingsState.isAboutVisible,
            pokemonListState = pokemonListState,
            snackbarHostState = snackbarHostState,
            onSettingsClick = settingsViewModel::openSettings,
            onCloseSettings = settingsViewModel::closeSettings,
            onTabSelected = listViewModel::selectTab,
            onLoadNextPage = listViewModel::loadNextPage,
            onRefresh = listViewModel::refreshPokemonList,
            onPokemonClick = { pokemon -> listViewModel.openPokemonDetail(pokemon.id) },
            onToggleTeam = listViewModel::toggleTeamMembership,
            onSetMetricUnits = settingsViewModel::setMetricUnits,
            onSetUse24HourTime = settingsViewModel::setUse24HourTime,
            onOpenModePicker = settingsViewModel::openModePicker,
            onCloseModePicker = settingsViewModel::closeModePicker,
            onSelectThemeMode = settingsViewModel::selectThemeMode,
            onOpenAbout = settingsViewModel::openAbout,
            onCloseAbout = settingsViewModel::closeAbout,
            onCloseTeamFullDialog = listViewModel::closeTeamFullDialog,
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (selectedPokemonId == null) 1f else 0f),
            isListInteractive = selectedPokemonId == null,
        )

        if (selectedPokemonId != null) {
            BackHandler {
                listViewModel.closePokemonDetail()
            }
            if (selectedPokemon != null) {
                val detailViewModel: PokemonDetailViewModel = koinViewModel(
                    key = "detail-$selectedPokemonId",
                    parameters = { parametersOf(selectedPokemonId) },
                )
                val detailState by detailViewModel.uiState.collectAsState()

                PokemonDetailScreen(
                    pokemon = selectedPokemon,
                    detail = detailState.detail,
                    isDetailLoading = detailState.isLoading,
                    detailLoadFailed = detailState.loadFailed,
                    useMetricUnits = detailState.useMetricUnits,
                    onBackClick = listViewModel::closePokemonDetail,
                    onSettingsClick = settingsViewModel::openSettings,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                PokemonDetailErrorScreen(
                    message = "This Pokémon is no longer available in the list.",
                    onBackClick = listViewModel::closePokemonDetail,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            if (settingsState.isVisible) {
                SettingsDrawer(
                    settings = settingsState.settings,
                    isModePickerVisible = settingsState.isModePickerVisible,
                    isAboutVisible = settingsState.isAboutVisible,
                    onClose = settingsViewModel::closeSettings,
                    onSetMetricUnits = settingsViewModel::setMetricUnits,
                    onSetUse24HourTime = settingsViewModel::setUse24HourTime,
                    onOpenModePicker = settingsViewModel::openModePicker,
                    onCloseModePicker = settingsViewModel::closeModePicker,
                    onSelectThemeMode = settingsViewModel::selectThemeMode,
                    onOpenAbout = settingsViewModel::openAbout,
                    onCloseAbout = settingsViewModel::closeAbout,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonMainListContent(
    modifier: Modifier = Modifier,
    listState: PokemonListUiState,
    settings: com.davidnardie.pokemon.presentation.state.AppSettings,
    isSettingsVisible: Boolean,
    isModePickerVisible: Boolean,
    isAboutVisible: Boolean,
    onSettingsClick: () -> Unit,
    onTabSelected: (NavigationTab) -> Unit,
    onLoadNextPage: () -> Unit,
    onRefresh: () -> Unit = {},
    pokemonListState: LazyListState = rememberLazyListState(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onCloseSettings: () -> Unit = {},
    onPokemonClick: (Pokemon) -> Unit = {},
    onToggleTeam: (Int) -> Unit = {},
    onSetMetricUnits: (Boolean) -> Unit = {},
    onSetUse24HourTime: (Boolean) -> Unit = {},
    onOpenModePicker: () -> Unit = {},
    onCloseModePicker: () -> Unit = {},
    onSelectThemeMode: (com.davidnardie.pokemon.presentation.state.ThemeMode) -> Unit = {},
    onOpenAbout: () -> Unit = {},
    onCloseAbout: () -> Unit = {},
    onCloseTeamFullDialog: () -> Unit = {},
    isListInteractive: Boolean = true,
) {
    val isDarkTheme = isPokemonDarkTheme()
    val colors = rememberPokemonListColors(isDarkTheme)
    val displayedPokemon = listState.displayedPokemon()
    val showPagination = listState.selectedTab == NavigationTab.Pokemon
    val showInitialLoadingSpinner = listState.shouldShowLoadingSpinner()

    Box(modifier = modifier.fillMaxSize()) {
        if (showInitialLoadingSpinner) {
            PokemonLoadingView(
                listState = listState,
                isDarkTheme = isDarkTheme,
                isRefreshing = listState.isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .background(colors.screenBackground),
            ) {
                // Shell hidden during initial load only.
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.screenBackground),
                ) {
                    PokemonListHeader(
                        title = screenTitle(listState),
                        isDarkTheme = isDarkTheme,
                        colors = colors,
                        onSettingsClick = onSettingsClick,
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                    ) {
                        PokemonLoadingView(
                            listState = listState,
                            isDarkTheme = isDarkTheme,
                            isRefreshing = listState.isRefreshing,
                            onRefresh = if (listState.selectedTab == NavigationTab.Pokemon) onRefresh else null,
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                PullToRefreshBox(
                                    isRefreshing = listState.isRefreshing,
                                    onRefresh = onRefresh,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .alpha(if (listState.selectedTab == NavigationTab.Pokemon) 1f else 0f),
                                ) {
                                    PokemonGrid(
                                        pokemon = displayedPokemon,
                                        teamPokemonIds = listState.teamPokemonIds,
                                        isDarkTheme = isDarkTheme,
                                        isLoading = listState.isLoading,
                                        isRefreshing = listState.isRefreshing,
                                        hasMore = listState.hasMore && showPagination,
                                        isScrollEnabled = listState.selectedTab == NavigationTab.Pokemon && isListInteractive,
                                        listState = pokemonListState,
                                        onLoadNextPage = onLoadNextPage,
                                        onPokemonClick = onPokemonClick,
                                        onToggleTeam = onToggleTeam,
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                }

                                if (listState.selectedTab == NavigationTab.Team) {
                                    TeamListContent(
                                        teamItems = listState.teamPokemonItems(),
                                        teamCount = listState.teamCount,
                                        isDarkTheme = isDarkTheme,
                                        colors = colors,
                                        onRemoveFromTeam = onToggleTeam,
                                        onAddMoreClick = { onTabSelected(NavigationTab.Pokemon) },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .verticalScroll(rememberScrollState()),
                                    )
                                }
                            }
                        }
                    }

                    PokemonAndroidBottomNavigation(
                        selectedTab = listState.selectedTab,
                        isDarkTheme = isDarkTheme,
                        colors = colors,
                        onTabSelected = onTabSelected,
                    )
                }

                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 80.dp),
                )
            }
        }

        if (isSettingsVisible && !showInitialLoadingSpinner && isListInteractive) {
            SettingsDrawer(
                settings = settings,
                isModePickerVisible = isModePickerVisible,
                isAboutVisible = isAboutVisible,
                onClose = onCloseSettings,
                onSetMetricUnits = onSetMetricUnits,
                onSetUse24HourTime = onSetUse24HourTime,
                onOpenModePicker = onOpenModePicker,
                onCloseModePicker = onCloseModePicker,
                onSelectThemeMode = onSelectThemeMode,
                onOpenAbout = onOpenAbout,
                onCloseAbout = onCloseAbout,
            )
        }

        if (listState.showTeamFullDialog) {
            FullTeamDialog(onDismiss = onCloseTeamFullDialog)
        }
    }
}

private fun screenTitle(listState: PokemonListUiState): String = when (listState.selectedTab) {
    NavigationTab.Team -> "Team ${listState.teamCount}/${PokemonListUiState.MAX_TEAM_SIZE}"
    NavigationTab.Pokemon -> "Pokemon"
}

@Composable
private fun PokemonListHeader(
    title: String,
    isDarkTheme: Boolean,
    colors: PokemonListColors,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(colors.screenBackground)
            .padding(horizontal = 20.dp)
            .padding(top = 8.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            color = colors.primaryText,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
        )
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
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
    }
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
        windowInsets = WindowInsets.navigationBars,
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
                selectedTextColor = colors.navigationSelectedContent,
                unselectedTextColor = colors.navigationUnselectedContent,
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
                selectedTextColor = colors.navigationSelectedContent,
                unselectedTextColor = colors.navigationUnselectedContent,
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
    isLoading: Boolean,
    isRefreshing: Boolean,
    hasMore: Boolean,
    listState: LazyListState,
    onLoadNextPage: () -> Unit,
    onPokemonClick: (Pokemon) -> Unit,
    onToggleTeam: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isScrollEnabled: Boolean = true,
) {
    val rows = pokemon.chunked(2)
    val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

    LaunchedEffect(lastVisibleItemIndex, rows.size, isLoading, isRefreshing, hasMore, isScrollEnabled) {
        if (!isScrollEnabled) return@LaunchedEffect
        if (lastVisibleItemIndex >= rows.size - 1 && !isLoading && !isRefreshing && hasMore) {
            onLoadNextPage()
        }
    }
    LazyColumn(
        state = listState,
        modifier = modifier,
        userScrollEnabled = isScrollEnabled,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
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

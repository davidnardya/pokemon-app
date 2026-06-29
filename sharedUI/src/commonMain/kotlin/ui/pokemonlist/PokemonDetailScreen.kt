package ui.pokemonlist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.davidnardie.pokemon.domain.model.Pokemon
import com.davidnardie.pokemon.domain.model.PokemonDetail
import com.davidnardie.pokemon.domain.model.PokemonType
import com.davidnardie.pokemon.presentation.util.PokemonDetailFormatter
import com.davidnardie.pokemon.presentation.util.TypeColors
import org.jetbrains.compose.resources.painterResource
import pokemonapp.sharedui.generated.resources.Res
import pokemonapp.sharedui.generated.resources.ic_darkmode_back_arrow
import pokemonapp.sharedui.generated.resources.ic_darkmode_height
import pokemonapp.sharedui.generated.resources.ic_darkmode_moves
import pokemonapp.sharedui.generated.resources.ic_darkmode_settings
import pokemonapp.sharedui.generated.resources.ic_darkmode_weight
import pokemonapp.sharedui.generated.resources.ic_lightmode_back_arrow
import pokemonapp.sharedui.generated.resources.ic_lightmode_height
import pokemonapp.sharedui.generated.resources.ic_lightmode_moves
import pokemonapp.sharedui.generated.resources.ic_lightmode_settings
import pokemonapp.sharedui.generated.resources.ic_lightmode_weight
import ui.theme.isPokemonDarkTheme

private object DetailLayout {
    val imageHeight = 200.dp
    val cardTopCornerRadius = 32.dp
    val cardOverlap = 24.dp
}

@Composable
fun PokemonDetailScreen(
    pokemon: Pokemon,
    detail: PokemonDetail?,
    isDetailLoading: Boolean,
    detailLoadFailed: Boolean,
    useMetricUnits: Boolean,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = isPokemonDarkTheme()
    val primaryType = detail?.primaryType ?: PokemonType.Normal
    val typePalette = TypeColors.palette(primaryType)
    val headerColor = typePalette.headerBackground.toComposeColor()
    val accentColor = typePalette.accent.toComposeColor()
    val cardBackground = TypeColors.cardBackground(isDarkTheme).toComposeColor()
    val tileBackground = TypeColors.tileBackground(isDarkTheme).toComposeColor()
    val statTrack = TypeColors.statTrack(isDarkTheme).toComposeColor()
    val borderColor = if (isDarkTheme) Color(0xFF2A2A2A) else Color(0xFFE8E8E8)
    val labelColor = if (isDarkTheme) Color(0xFF8E8E93) else Color(0xFFA4A4A4)
    val primaryText = if (isDarkTheme) Color.White else Color(0xFF1D1D1D)
    val secondaryText = if (isDarkTheme) Color(0xFF8E8E93) else Color(0xFF8E8E93)

    val cardTopShape = RoundedCornerShape(
        topStart = DetailLayout.cardTopCornerRadius,
        topEnd = DetailLayout.cardTopCornerRadius,
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(cardBackground),
    ) {
        val headerHeight = maxHeight * 0.42f
        val cardTopOffset = (headerHeight - DetailLayout.cardOverlap).coerceAtLeast(0.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .align(Alignment.TopCenter)
                .background(headerColor),
        )

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = cardTopOffset),
            shape = cardTopShape,
            color = cardBackground,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
        ) {
            when {
                detail != null -> {
                    DetailInfoCard(
                        pokemon = pokemon,
                        detail = detail,
                        useMetricUnits = useMetricUnits,
                        isDarkTheme = isDarkTheme,
                        accentColor = accentColor,
                        tileBackground = tileBackground,
                        borderColor = borderColor,
                        labelColor = labelColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        statTrack = statTrack,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                isDetailLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Loading...",
                            color = secondaryText,
                            fontSize = 16.sp,
                        )
                    }
                }
                detailLoadFailed -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Unable to load Pokémon details.\nCheck your connection and try again.",
                            color = secondaryText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .align(Alignment.TopCenter)
                .zIndex(1f),
        ) {
            DetailHeaderControls(
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick,
                isDarkTheme = isDarkTheme,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            AsyncImage(
                model = pokemonImageUrl(pokemon.id),
                contentDescription = pokemon.name,
                modifier = Modifier
                    .height(DetailLayout.imageHeight)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = DetailLayout.cardOverlap),
                contentScale = ContentScale.Fit,
            )
        }
    }
}

@Composable
private fun DetailHeaderControls(
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
) {
    val buttonBackground = detailHeaderButtonBackground(isDarkTheme)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DetailCircleButton(
            onClick = onBackClick,
            backgroundColor = buttonBackground,
        ) {
            Icon(
                painter = painterResource(
                    if (isDarkTheme) Res.drawable.ic_darkmode_back_arrow
                    else Res.drawable.ic_lightmode_back_arrow,
                ),
                contentDescription = "Back",
                tint = Color.Unspecified,
                modifier = Modifier.size(width = 8.dp, height = 14.dp),
            )
        }

        DetailCircleButton(
            onClick = onSettingsClick,
            backgroundColor = buttonBackground,
        ) {
            Icon(
                painter = painterResource(
                    if (isDarkTheme) Res.drawable.ic_darkmode_settings else Res.drawable.ic_lightmode_settings,
                ),
                contentDescription = "Settings",
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

private fun detailHeaderButtonBackground(isDarkTheme: Boolean): Color =
    if (isDarkTheme) {
        Color(0xFF1C1C1E).copy(alpha = 0.75f)
    } else {
        Color.White.copy(alpha = 0.9f)
    }

@Composable
private fun DetailCircleButton(
    onClick: () -> Unit,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(backgroundColor),
    ) {
        content()
    }
}

@Composable
private fun DetailInfoCard(
    pokemon: Pokemon,
    detail: PokemonDetail,
    useMetricUnits: Boolean,
    isDarkTheme: Boolean,
    accentColor: Color,
    tileBackground: Color,
    borderColor: Color,
    labelColor: Color,
    primaryText: Color,
    secondaryText: Color,
    statTrack: Color,
    modifier: Modifier = Modifier,
) {
    val weightIcon = painterResource(
        if (isDarkTheme) Res.drawable.ic_darkmode_weight else Res.drawable.ic_lightmode_weight,
    )
    val heightIcon = painterResource(
        if (isDarkTheme) Res.drawable.ic_darkmode_height else Res.drawable.ic_lightmode_height,
    )
    val movesIcon = painterResource(
        if (isDarkTheme) Res.drawable.ic_darkmode_moves else Res.drawable.ic_lightmode_moves,
    )
    val iconBackground = if (isDarkTheme) accentColor.copy(alpha = 0.35f) else accentColor.copy(alpha = 0.18f)
    val iconTint = if (isDarkTheme) Color.White else accentColor

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 28.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                detail.types.forEach { type ->
                    DetailTypeBadge(type = type, isDarkTheme = isDarkTheme)
                }
            }

            Text(
                text = pokemon.formattedId(),
                color = secondaryText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
        }

        Text(
            text = pokemon.displayName(),
            color = primaryText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PokemonInfoTile(
                label = "Weight",
                value = PokemonDetailFormatter.formatWeight(detail.weightHectograms, useMetricUnits),
                icon = weightIcon,
                tileBackground = tileBackground,
                borderColor = borderColor,
                labelColor = labelColor,
                valueColor = primaryText,
                iconBackground = iconBackground,
                iconTint = iconTint,
                modifier = Modifier.weight(1f),
            )
            PokemonInfoTile(
                label = "Height",
                value = PokemonDetailFormatter.formatHeight(detail.heightDecimeters, useMetricUnits),
                icon = heightIcon,
                tileBackground = tileBackground,
                borderColor = borderColor,
                labelColor = labelColor,
                valueColor = primaryText,
                iconBackground = iconBackground,
                iconTint = iconTint,
                modifier = Modifier.weight(1f),
            )
        }

        if (detail.abilities.isNotEmpty()) {
            PokemonMovesTile(
                label = "Moves",
                moves = detail.abilities,
                icon = movesIcon,
                tileBackground = tileBackground,
                borderColor = borderColor,
                labelColor = labelColor,
                valueColor = primaryText,
                iconBackground = iconBackground,
                iconTint = iconTint,
            )
        }

        if (detail.stats.isNotEmpty()) {
            PokemonBaseStatsSection(
                stats = detail.stats,
                accentColor = accentColor,
                tileBackground = tileBackground,
                borderColor = borderColor,
                labelColor = labelColor,
                valueColor = primaryText,
                trackColor = statTrack,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun DetailTypeBadge(
    type: PokemonType,
    isDarkTheme: Boolean,
) {
    val background = TypeColors.badgeBackground(type, isDarkTheme).toComposeColor()
    val textColor = TypeColors.badgeText(type, isDarkTheme).toComposeColor()

    Text(
        text = type.displayName,
        color = textColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    )
}

private fun Pokemon.displayName(): String =
    name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

private fun Pokemon.formattedId(): String = "#%03d".format(id)

@Composable
internal fun PokemonDetailErrorScreen(
    message: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = isPokemonDarkTheme()
    val colors = rememberPokemonListColors(isDarkTheme)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.screenBackground)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        IconButton(onClick = onBackClick) {
            Text(
                text = "‹ Back",
                color = colors.primaryText,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = message,
                color = colors.secondaryText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

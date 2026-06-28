package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.davidnardie.pokemon.presentation.state.ThemeMode

private val PokemonLightCardGrey = Color(0xFFF1F1F1)
private val PokemonDarkCardGrey = Color(0xFF2A2A2A)

private val LightColorScheme = lightColorScheme(
    surface = Color(0xFFEDEDED),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceVariant = PokemonLightCardGrey,
    onSurface = Color(0xFF1A1A1A),
    onSurfaceVariant = Color(0xFF8E8E93),
)

private val DarkColorScheme = darkColorScheme(
    surface = Color(0xFF000000),
    surfaceContainerLowest = PokemonDarkCardGrey,
    surfaceVariant = PokemonDarkCardGrey,
    onSurface = Color(0xFFFFFFFF),
    onSurfaceVariant = Color(0xFF8E8E93),
)

private val LocalPokemonDarkTheme = staticCompositionLocalOf { false }

@Composable
fun isPokemonDarkTheme(): Boolean = LocalPokemonDarkTheme.current

@Composable
fun PokemonAppTheme(
    themeMode: ThemeMode = ThemeMode.System,
    content: @Composable () -> Unit,
) {
    val systemDark = isSystemInDarkTheme()
    val isDarkTheme = when (themeMode) {
        ThemeMode.System -> systemDark
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }
    CompositionLocalProvider(LocalPokemonDarkTheme provides isDarkTheme) {
        MaterialTheme(
            colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme,
            content = content,
        )
    }
}

object PokemonCardTheme {
    val containerColor: Color
        @Composable get() = if (isPokemonDarkTheme()) {
            MaterialTheme.colorScheme.surfaceVariant
        } else {
            MaterialTheme.colorScheme.surfaceContainerLowest
        }

    val imageSurfaceColor: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceVariant

    val usesDistinctImageSurface: Boolean
        @Composable get() = !isPokemonDarkTheme()
}

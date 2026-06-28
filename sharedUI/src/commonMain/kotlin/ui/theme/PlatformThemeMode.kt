package ui.theme

import androidx.compose.runtime.Composable
import com.davidnardie.pokemon.presentation.state.ThemeMode

@Composable
internal expect fun SyncPlatformThemeMode(themeMode: ThemeMode)

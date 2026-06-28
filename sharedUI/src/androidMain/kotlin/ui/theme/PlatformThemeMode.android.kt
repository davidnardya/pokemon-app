package ui.theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.davidnardie.pokemon.presentation.state.ThemeMode

@Composable
internal actual fun SyncPlatformThemeMode(themeMode: ThemeMode) {
    SideEffect {
        val nightMode = when (themeMode) {
            ThemeMode.System -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            ThemeMode.Light -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.Dark -> AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}

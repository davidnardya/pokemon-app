package ui.pokemonlist

import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.davidnardie.pokemon.presentation.state.AppSettings
import com.davidnardie.pokemon.presentation.state.ThemeMode
import ui.theme.isPokemonDarkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDrawer(
    settings: AppSettings,
    isModePickerVisible: Boolean,
    isAboutVisible: Boolean,
    onClose: () -> Unit,
    onSetMetricUnits: (Boolean) -> Unit,
    onSetUse24HourTime: (Boolean) -> Unit,
    onOpenModePicker: () -> Unit,
    onCloseModePicker: () -> Unit,
    onSelectThemeMode: (ThemeMode) -> Unit,
    onOpenAbout: () -> Unit,
    onCloseAbout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = isPokemonDarkTheme()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        modifier = modifier,
        containerColor = if (isDarkTheme) Color(0xFF1C1C1E) else Color(0xFFF5F5F5),
        dragHandle = null,
    ) {
        SettingsDrawerContent(
            settings = settings,
            isModePickerVisible = isModePickerVisible,
            isAboutVisible = isAboutVisible,
            onClose = onClose,
            onSetMetricUnits = onSetMetricUnits,
            onSetUse24HourTime = onSetUse24HourTime,
            onOpenModePicker = onOpenModePicker,
            onCloseModePicker = onCloseModePicker,
            onSelectThemeMode = onSelectThemeMode,
            onOpenAbout = onOpenAbout,
            onCloseAbout = onCloseAbout,
        )
    }
}

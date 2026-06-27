package ui.pokemonlist

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davidnardie.pokemon.presentation.state.AppSettings
import org.jetbrains.compose.resources.painterResource
import pokemonapp.sharedui.generated.resources.Res
import pokemonapp.sharedui.generated.resources.ic_darkmode_clock
import pokemonapp.sharedui.generated.resources.ic_darkmode_mode
import pokemonapp.sharedui.generated.resources.ic_darkmode_settings_close
import pokemonapp.sharedui.generated.resources.ic_darkmode_unit
import pokemonapp.sharedui.generated.resources.ic_lightmode_clock
import pokemonapp.sharedui.generated.resources.ic_lightmode_mode
import pokemonapp.sharedui.generated.resources.ic_lightmode_settings_close
import pokemonapp.sharedui.generated.resources.ic_lightmode_unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDrawer(
    settings: AppSettings,
    onClose: () -> Unit,
    onToggleMetricUnits: () -> Unit,
    onToggleTimeFormat: () -> Unit,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = isSystemInDarkTheme()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val colors = rememberPokemonListColors(isDarkTheme)

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        modifier = modifier,
        containerColor = if (isDarkTheme) Color(0xFF1C1C1E) else Color.White,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Settings",
                    color = colors.primaryText,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                )
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isDarkTheme) Color(0xFF2C2C2E) else Color(0xFFF2F2F7)),
                ) {
                    Icon(
                        painter = painterResource(
                            if (isDarkTheme) Res.drawable.ic_darkmode_settings_close
                            else Res.drawable.ic_lightmode_settings_close,
                        ),
                        contentDescription = "Close settings",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            SettingsToggleRow(
                icon = if (isDarkTheme) Res.drawable.ic_darkmode_unit else Res.drawable.ic_lightmode_unit,
                title = "Units",
                subtitle = if (settings.useMetricUnits) "Metric" else "Imperial",
                checked = settings.useMetricUnits,
                onCheckedChange = { onToggleMetricUnits() },
                colors = colors,
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingsToggleRow(
                icon = if (isDarkTheme) Res.drawable.ic_darkmode_clock else Res.drawable.ic_lightmode_clock,
                title = "Time Format",
                subtitle = if (settings.use24HourTime) "24-hour" else "12-hour",
                checked = settings.use24HourTime,
                onCheckedChange = { onToggleTimeFormat() },
                colors = colors,
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingsToggleRow(
                icon = if (isDarkTheme) Res.drawable.ic_darkmode_mode else Res.drawable.ic_lightmode_mode,
                title = "Theme",
                subtitle = if (settings.useDarkTheme) "Dark" else "Light",
                checked = settings.useDarkTheme,
                onCheckedChange = { onToggleTheme() },
                colors = colors,
            )
        }
    }
}

@Composable
private fun SettingsToggleRow(
    icon: org.jetbrains.compose.resources.DrawableResource,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    colors: PokemonListColors,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (colors.screenBackground == Color(0xFF000000)) Color(0xFF2C2C2E) else Color(0xFFF2F2F7))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
        ) {
            Text(
                text = title,
                color = colors.primaryText,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = subtitle,
                color = colors.secondaryText,
                fontSize = 14.sp,
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = Color(0xFFFF9500),
                checkedThumbColor = Color.White,
            ),
        )
    }
}

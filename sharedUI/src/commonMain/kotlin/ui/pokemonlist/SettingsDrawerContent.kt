package ui.pokemonlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.davidnardie.pokemon.presentation.state.AppSettings
import com.davidnardie.pokemon.presentation.state.ThemeMode
import org.jetbrains.compose.resources.painterResource
import pokemonapp.sharedui.generated.resources.Res
import pokemonapp.sharedui.generated.resources.ic_darkmode_about
import pokemonapp.sharedui.generated.resources.ic_darkmode_clock
import pokemonapp.sharedui.generated.resources.ic_darkmode_down_arrow
import pokemonapp.sharedui.generated.resources.ic_darkmode_mode
import pokemonapp.sharedui.generated.resources.ic_darkmode_right_arrow
import pokemonapp.sharedui.generated.resources.ic_darkmode_settings_close
import pokemonapp.sharedui.generated.resources.ic_darkmode_unit
import pokemonapp.sharedui.generated.resources.ic_lightmode_about
import pokemonapp.sharedui.generated.resources.ic_lightmode_clock
import pokemonapp.sharedui.generated.resources.ic_lightmode_down_arrow
import pokemonapp.sharedui.generated.resources.ic_lightmode_mode
import pokemonapp.sharedui.generated.resources.ic_lightmode_right_arrow
import pokemonapp.sharedui.generated.resources.ic_lightmode_settings_close
import pokemonapp.sharedui.generated.resources.ic_lightmode_unit
import ui.theme.isPokemonDarkTheme

private val PillShape = RoundedCornerShape(percent = 50)
private val SettingsContentHorizontalPadding = 20.dp
private val ModePickerEndMargin = 16.dp
private val SettingsRowMinHeight = 48.dp
// Handle + header + unit row + divider + time row + divider + mode row
private val ModePickerTopOffset = 170.dp

@Composable
private fun Modifier.settingsClickable(onClick: () -> Unit): Modifier =
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
    )

@Composable
internal fun SettingsDrawerContent(
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
    val colors = rememberSettingsColors(isDarkTheme)

    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp, bottom = 16.dp)
                    .width(36.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(2.5.dp))
                    .background(colors.dragHandle),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colors.closeButtonBackground),
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

                Text(
                    text = "Settings",
                    color = colors.primaryText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(colors.cardBackground),
            ) {
                SettingsUnitRow(
                    isDarkTheme = isDarkTheme,
                    useMetricUnits = settings.useMetricUnits,
                    colors = colors,
                    onSetMetricUnits = onSetMetricUnits,
                )
                SettingsDivider(colors)
                SettingsSwitchRow(
                    icon = if (isDarkTheme) Res.drawable.ic_darkmode_clock else Res.drawable.ic_lightmode_clock,
                    title = "24-Hour Time",
                    checked = settings.use24HourTime,
                    isDarkTheme = isDarkTheme,
                    colors = colors,
                    onCheckedChange = onSetUse24HourTime,
                )
                SettingsDivider(colors)
                SettingsNavigationRow(
                    icon = if (isDarkTheme) Res.drawable.ic_darkmode_mode else Res.drawable.ic_lightmode_mode,
                    title = "Mode",
                    value = settings.themeMode.displayLabel(),
                    isDarkTheme = isDarkTheme,
                    colors = colors,
                    showChevronDown = true,
                    onClick = onOpenModePicker,
                )
                SettingsDivider(colors)
                SettingsNavigationRow(
                    icon = if (isDarkTheme) Res.drawable.ic_darkmode_about else Res.drawable.ic_lightmode_about,
                    title = "About",
                    value = null,
                    isDarkTheme = isDarkTheme,
                    colors = colors,
                    showChevronDown = false,
                    onClick = onOpenAbout,
                )
            }
        }

        if (isModePickerVisible) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .settingsClickable(onCloseModePicker)
                    .zIndex(1f),
            )
            ModePickerView(
                selectedMode = settings.themeMode,
                isDarkTheme = isDarkTheme,
                colors = colors,
                onSelectMode = onSelectThemeMode,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(
                        x = -(SettingsContentHorizontalPadding + ModePickerEndMargin),
                        y = ModePickerTopOffset,
                    )
                    .zIndex(2f),
            )
        }
    }

    if (isAboutVisible) {
        SettingsAboutDialog(
            isDarkTheme = isDarkTheme,
            colors = colors,
            onClose = onCloseAbout,
        )
    }
}

@Composable
private fun SettingsUnitRow(
    isDarkTheme: Boolean,
    useMetricUnits: Boolean,
    colors: SettingsColors,
    onSetMetricUnits: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = SettingsRowMinHeight)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(
                if (isDarkTheme) Res.drawable.ic_darkmode_unit else Res.drawable.ic_lightmode_unit,
            ),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Unit",
            color = colors.primaryText,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.wrapContentWidth(unbounded = false),
        )
        Spacer(modifier = Modifier.weight(1f))
        UnitSegmentedControl(
            useMetricUnits = useMetricUnits,
            isDarkTheme = isDarkTheme,
            colors = colors,
            onSetMetricUnits = onSetMetricUnits,
            modifier = Modifier.widthIn(max = 188.dp),
        )
    }
}

@Composable
private fun UnitSegmentedControl(
    useMetricUnits: Boolean,
    isDarkTheme: Boolean,
    colors: SettingsColors,
    onSetMetricUnits: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(36.dp)
            .wrapContentWidth(unbounded = false)
            .clip(PillShape)
            .background(colors.segmentBackground)
            .padding(3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UnitSegmentOption(
            label = "Metric",
            selected = useMetricUnits,
            isDarkTheme = isDarkTheme,
            colors = colors,
            onClick = { onSetMetricUnits(true) },
            modifier = Modifier.weight(1f),
        )
        UnitSegmentOption(
            label = "Imperial",
            selected = !useMetricUnits,
            isDarkTheme = isDarkTheme,
            colors = colors,
            onClick = { onSetMetricUnits(false) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun UnitSegmentOption(
    label: String,
    selected: Boolean,
    isDarkTheme: Boolean,
    colors: SettingsColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedBackgroundModifier = if (selected) {
        Modifier
            .shadow(
                elevation = if (isDarkTheme) 0.dp else 2.dp,
                shape = PillShape,
                clip = false,
            )
            .clip(PillShape)
            .background(colors.segmentSelectedBackground)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .then(selectedBackgroundModifier)
            .settingsClickable(onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = if (selected) colors.primaryText else colors.secondaryText,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
        )
    }
}

@Composable
private fun SettingsSwitchRow(
    icon: org.jetbrains.compose.resources.DrawableResource,
    title: String,
    checked: Boolean,
    isDarkTheme: Boolean,
    colors: SettingsColors,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = SettingsRowMinHeight)
            .settingsClickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            color = colors.primaryText,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f),
        )
        Switch(
            checked = checked,
            onCheckedChange = null,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = if (isDarkTheme) Color(0xFF39393D) else Color(0xFF34C759),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = colors.switchTrack,
                uncheckedBorderColor = Color.Transparent,
                checkedBorderColor = Color.Transparent,
                disabledUncheckedThumbColor = Color.White,
                disabledUncheckedTrackColor = colors.switchTrack,
            ),
        )
    }
}

@Composable
private fun SettingsNavigationRow(
    icon: org.jetbrains.compose.resources.DrawableResource,
    title: String,
    value: String?,
    isDarkTheme: Boolean,
    colors: SettingsColors,
    showChevronDown: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = SettingsRowMinHeight)
            .settingsClickable(onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            color = colors.primaryText,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f),
        )
        if (value != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = value,
                    color = colors.secondaryText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                )
                Icon(
                    painter = painterResource(
                        if (isDarkTheme) Res.drawable.ic_darkmode_down_arrow
                        else Res.drawable.ic_lightmode_down_arrow,
                    ),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp),
                )
            }
        } else if (!showChevronDown) {
            Icon(
                painter = painterResource(
                    if (isDarkTheme) Res.drawable.ic_darkmode_right_arrow
                    else Res.drawable.ic_lightmode_right_arrow,
                ),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
private fun SettingsDivider(colors: SettingsColors) {
    HorizontalDivider(
        color = colors.divider,
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}

internal data class SettingsColors(
    val primaryText: Color,
    val secondaryText: Color,
    val cardBackground: Color,
    val closeButtonBackground: Color,
    val dragHandle: Color,
    val divider: Color,
    val segmentBackground: Color,
    val segmentSelectedBackground: Color,
    val switchTrack: Color,
    val pickerBackground: Color,
    val aboutBackground: Color,
)

@Composable
internal fun rememberSettingsColors(isDarkTheme: Boolean): SettingsColors {
    return if (isDarkTheme) {
        SettingsColors(
            primaryText = Color.White,
            secondaryText = Color(0xFF8E8E93),
            cardBackground = Color(0xFF2C2C2E),
            closeButtonBackground = Color(0xFF3A3A3C),
            dragHandle = Color(0xFF5A5A5E),
            divider = Color(0xFF3A3A3C),
            segmentBackground = Color(0xFF3A3A3C),
            segmentSelectedBackground = Color(0xFF5A5A5E),
            switchTrack = Color(0xFF39393D),
            pickerBackground = Color(0xFF2C2C2E),
            aboutBackground = Color(0xFF2C2C2E),
        )
    } else {
        SettingsColors(
            primaryText = Color(0xFF1A1A1A),
            secondaryText = Color(0xFF8E8E93),
            cardBackground = Color.White,
            closeButtonBackground = Color.White,
            dragHandle = Color(0xFFD1D1D6),
            divider = Color(0xFFE5E5EA),
            segmentBackground = Color(0xFFE5E5EA),
            segmentSelectedBackground = Color.White,
            switchTrack = Color(0xFFE5E5EA),
            pickerBackground = Color.White,
            aboutBackground = Color(0xFFF2F2F7),
        )
    }
}

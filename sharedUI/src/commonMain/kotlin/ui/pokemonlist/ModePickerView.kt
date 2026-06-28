package ui.pokemonlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davidnardie.pokemon.presentation.state.ThemeMode
import org.jetbrains.compose.resources.painterResource
import pokemonapp.sharedui.generated.resources.Res
import pokemonapp.sharedui.generated.resources.ic_lightmode_checkmark

@Composable
internal fun ModePickerView(
    selectedMode: ThemeMode,
    isDarkTheme: Boolean,
    colors: SettingsColors,
    onSelectMode: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pickerShape = RoundedCornerShape(16.dp)

    val shadowAlpha = if (isDarkTheme) 0.4f else 0.18f

    Column(
        modifier = modifier
            .widthIn(min = 200.dp, max = 240.dp)
            .dropShadow(
                shape = pickerShape,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = shadowAlpha),
                    offset = DpOffset(0.dp, 8.dp),
                    radius = 16.dp,
                ),
            )
            .clip(pickerShape)
            .background(colors.pickerBackground),
    ) {
        ThemeMode.entries.forEach { mode ->
            Row(
                modifier = Modifier
                    .clickable { onSelectMode(mode) }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (selectedMode == mode) {
                    Image(
                        painter = painterResource(Res.drawable.ic_lightmode_checkmark),
                        contentDescription = null,
                        colorFilter = if (isDarkTheme) ColorFilter.tint(Color.White) else null,
                        modifier = Modifier.size(16.dp),
                    )
                } else {
                    Spacer(modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = mode.displayLabel(),
                    color = colors.primaryText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

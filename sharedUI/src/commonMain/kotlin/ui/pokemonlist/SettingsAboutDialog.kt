package ui.pokemonlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.window.Dialog
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.davidnardie.pokemon.presentation.util.AboutAvatarUrl
import org.jetbrains.compose.resources.painterResource
import pokemonapp.sharedui.generated.resources.Res
import pokemonapp.sharedui.generated.resources.ic_darkmode_settings_close
import pokemonapp.sharedui.generated.resources.ic_lightmode_settings_close
import pokemonapp.sharedui.generated.resources.ic_logo

@Composable
internal fun SettingsAboutDialog(
    isDarkTheme: Boolean,
    colors: SettingsColors,
    onClose: () -> Unit,
) {
    Dialog(onDismissRequest = onClose) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(colors.aboutBackground)
                .padding(horizontal = 24.dp, vertical = 20.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "About",
                        color = colors.primaryText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    IconButton(onClick = onClose) {
                        Icon(
                            painter = painterResource(
                                if (isDarkTheme) Res.drawable.ic_darkmode_settings_close
                                else Res.drawable.ic_lightmode_settings_close,
                            ),
                            contentDescription = "Close about",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                AboutAvatarImage(
                    isDarkTheme = isDarkTheme,
                    colors = colors,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Pokemon v1.0",
                    color = colors.secondaryText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun AboutAvatarImage(
    isDarkTheme: Boolean,
    colors: SettingsColors,
) {
    val placeholderBackground = if (isDarkTheme) {
        Color(0xFF3A3A3C)
    } else {
        Color(0xFFE5E5EA)
    }

    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(placeholderBackground),
        contentAlignment = Alignment.Center,
    ) {
        SubcomposeAsyncImage(
            model = AboutAvatarUrl.IMAGE_URL,
            contentDescription = "About avatar",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    strokeWidth = 2.dp,
                    color = colors.secondaryText,
                )
            },
            error = {
                Image(
                    painter = painterResource(Res.drawable.ic_logo),
                    contentDescription = "Pokemon logo",
                    modifier = Modifier.size(48.dp),
                    contentScale = ContentScale.Fit,
                )
            },
            success = {
                SubcomposeAsyncImageContent()
            },
        )
    }
}

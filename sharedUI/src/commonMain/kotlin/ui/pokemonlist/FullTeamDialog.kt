package ui.pokemonlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.davidnardie.pokemon.presentation.util.DialogButtonTheme
import ui.theme.isPokemonDarkTheme
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.painterResource
import pokemonapp.sharedui.generated.resources.Res
import pokemonapp.sharedui.generated.resources.ic_full_team

@Composable
fun FullTeamDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = isPokemonDarkTheme()
    val colors = rememberFullTeamDialogColors(isDarkTheme)

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(colors.background, RoundedCornerShape(32.dp))
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_full_team),
                contentDescription = null,
                modifier = Modifier.size(width = 186.dp, height = 144.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Team is full!",
                color = colors.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = buildAnnotatedString {
                    append("Your team can only have ")
                    withStyle(
                        SpanStyle(
                            color = colors.emphasisText,
                            fontWeight = FontWeight.Bold,
                        ),
                    ) {
                        append("6 Pokemons")
                    }
                    append(". Please, remove a member from your ")
                    withStyle(
                        SpanStyle(
                            color = colors.emphasisText,
                            fontWeight = FontWeight.Bold,
                        ),
                    ) {
                        append("Team")
                    }
                    append(" before adding a new one.")
                },
                color = colors.description,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                DialogButtonTheme.closeButtonGradientTop().toComposeColor(),
                                DialogButtonTheme.closeButtonGradientBottom().toComposeColor(),
                            ),
                        ),
                    )
                    .clickable(onClick = onDismiss),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Close",
                    color = DialogButtonTheme.closeButtonText().toComposeColor(),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 14.dp),
                )
            }
        }
    }
}

private data class FullTeamDialogColors(
    val background: Color,
    val title: Color,
    val description: Color,
    val emphasisText: Color,
)

@Composable
private fun rememberFullTeamDialogColors(isDarkTheme: Boolean): FullTeamDialogColors {
    return if (isDarkTheme) {
        FullTeamDialogColors(
            background = Color(0xFF1C1C1E),
            title = Color(0xFFFFFFFF),
            description = Color(0xFF8E8E93),
            emphasisText = Color(0xFFFFFFFF),
        )
    } else {
        FullTeamDialogColors(
            background = Color(0xFFF2F2F2),
            title = Color(0xFF222222),
            description = Color(0xFF666666),
            emphasisText = Color(0xFF444444),
        )
    }
}

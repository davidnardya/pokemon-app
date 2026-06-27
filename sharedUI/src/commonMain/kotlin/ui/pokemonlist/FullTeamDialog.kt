package ui.pokemonlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    val isDarkTheme = isSystemInDarkTheme()
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

            TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(colors.buttonGradientTop, colors.buttonGradientBottom),
                        ),
                        shape = RoundedCornerShape(50),
                    ),
            ) {
                Text(
                    text = "Close",
                    color = colors.buttonText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 6.dp),
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
    val buttonGradientTop: Color,
    val buttonGradientBottom: Color,
    val buttonText: Color,
)

@Composable
private fun rememberFullTeamDialogColors(isDarkTheme: Boolean): FullTeamDialogColors {
    return if (isDarkTheme) {
        FullTeamDialogColors(
            background = Color(0xFF1C1C1E),
            title = Color(0xFFFFFFFF),
            description = Color(0xFF8E8E93),
            emphasisText = Color(0xFFFFFFFF),
            buttonGradientTop = Color(0xFFEBEBEF),
            buttonGradientBottom = Color(0xFFD1D1D6),
            buttonText = Color(0xFF1C1C1E),
        )
    } else {
        FullTeamDialogColors(
            background = Color(0xFFF2F2F2),
            title = Color(0xFF222222),
            description = Color(0xFF666666),
            emphasisText = Color(0xFF444444),
            buttonGradientTop = Color(0xFF555555),
            buttonGradientBottom = Color(0xFF1A1A1A),
            buttonText = Color(0xFFFFFFFF),
        )
    }
}

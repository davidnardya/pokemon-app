package ui.pokemonlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davidnardie.pokemon.domain.model.PokemonType

internal data class PokemonTypeBadgeColors(
    val background: Color,
    val text: Color,
)

@Composable
internal fun PokemonTypeBadge(
    type: PokemonType,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = pokemonTypeBadgeColors(type, isDarkTheme)
    Box(
        modifier = modifier
            .background(colors.background, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = type.displayName,
            color = colors.text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

internal fun pokemonTypeBadgeColors(type: PokemonType, isDarkTheme: Boolean): PokemonTypeBadgeColors {
    if (isDarkTheme) {
        return PokemonTypeBadgeColors(
            background = darkTypeBackground(type),
            text = Color.White,
        )
    }

    return PokemonTypeBadgeColors(
        background = lightTypeBackground(type),
        text = lightTypeText(type),
    )
}

private fun lightTypeBackground(type: PokemonType): Color = when (type) {
    PokemonType.Grass -> Color(0xFFC2E1B9)
    PokemonType.Poison -> Color(0xFFE5CCEE)
    PokemonType.Fire -> Color(0xFFF8D5B5)
    PokemonType.Water -> Color(0xFFB8D4F0)
    PokemonType.Electric -> Color(0xFFF8E8A0)
    PokemonType.Fairy -> Color(0xFFF5C2D8)
    PokemonType.Steel -> Color(0xFFC8D4E0)
    PokemonType.Dragon -> Color(0xFFD4C2F0)
    PokemonType.Ice -> Color(0xFFB8E8F0)
    PokemonType.Fighting -> Color(0xFFF0C2B8)
    PokemonType.Ground -> Color(0xFFE8D4B8)
    PokemonType.Flying -> Color(0xFFD4E0F0)
    PokemonType.Psychic -> Color(0xFFF0C2D8)
    PokemonType.Bug -> Color(0xFFD4E8B8)
    PokemonType.Rock -> Color(0xFFE0D4C8)
    PokemonType.Ghost -> Color(0xFFD4C8E8)
    PokemonType.Dark -> Color(0xFFC8C8D4)
    PokemonType.Normal -> Color(0xFFE0E0E0)
}

private fun lightTypeText(type: PokemonType): Color = when (type) {
    PokemonType.Grass -> Color(0xFF4E8234)
    PokemonType.Poison -> Color(0xFF9F5BBA)
    PokemonType.Fire -> Color(0xFFB85C1A)
    PokemonType.Water -> Color(0xFF3A6FA0)
    PokemonType.Electric -> Color(0xFF9A8A1A)
    PokemonType.Fairy -> Color(0xFFB85C8A)
    PokemonType.Steel -> Color(0xFF5A6A7A)
    PokemonType.Dragon -> Color(0xFF6A4A9A)
    PokemonType.Ice -> Color(0xFF3A8A9A)
    PokemonType.Fighting -> Color(0xFF9A4A3A)
    PokemonType.Ground -> Color(0xFF8A6A3A)
    PokemonType.Flying -> Color(0xFF5A7A9A)
    PokemonType.Psychic -> Color(0xFFB85C8A)
    PokemonType.Bug -> Color(0xFF6A8A3A)
    PokemonType.Rock -> Color(0xFF7A6A5A)
    PokemonType.Ghost -> Color(0xFF6A5A8A)
    PokemonType.Dark -> Color(0xFF5A5A6A)
    PokemonType.Normal -> Color(0xFF6A6A6A)
}

private fun darkTypeBackground(type: PokemonType): Color = when (type) {
    PokemonType.Grass -> Color(0xFF4CAF50)
    PokemonType.Poison -> Color(0xFF9C27B0)
    PokemonType.Fire -> Color(0xFFE65100)
    PokemonType.Water -> Color(0xFF1976D2)
    PokemonType.Electric -> Color(0xFFF9A825)
    PokemonType.Fairy -> Color(0xFFE91E63)
    PokemonType.Steel -> Color(0xFF607D8B)
    PokemonType.Dragon -> Color(0xFF7B1FA2)
    PokemonType.Ice -> Color(0xFF00ACC1)
    PokemonType.Fighting -> Color(0xFFD84315)
    PokemonType.Ground -> Color(0xFF8D6E63)
    PokemonType.Flying -> Color(0xFF5C6BC0)
    PokemonType.Psychic -> Color(0xFFAB47BC)
    PokemonType.Bug -> Color(0xFF689F38)
    PokemonType.Rock -> Color(0xFF795548)
    PokemonType.Ghost -> Color(0xFF5E35B1)
    PokemonType.Dark -> Color(0xFF424242)
    PokemonType.Normal -> Color(0xFF757575)
}

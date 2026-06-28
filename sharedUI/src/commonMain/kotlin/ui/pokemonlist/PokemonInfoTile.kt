package ui.pokemonlist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun PokemonInfoTile(
    label: String,
    value: String,
    icon: Painter,
    tileBackground: Color,
    borderColor: Color,
    labelColor: Color,
    valueColor: Color,
    iconBackground: Color,
    iconTint: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(tileBackground)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                color = labelColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            )
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(iconBackground),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(16.dp),
                )
            }
        }

        Text(
            text = value,
            color = valueColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
internal fun PokemonMovesTile(
    label: String,
    moves: List<String>,
    icon: Painter,
    tileBackground: Color,
    borderColor: Color,
    labelColor: Color,
    valueColor: Color,
    iconBackground: Color,
    iconTint: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(tileBackground)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                color = labelColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            )
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(iconBackground),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(16.dp),
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            moves.forEach { move ->
                Text(
                    text = move,
                    color = valueColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
internal fun PokemonBaseStatsSection(
    stats: List<com.davidnardie.pokemon.domain.model.PokemonStatEntry>,
    accentColor: Color,
    tileBackground: Color,
    borderColor: Color,
    labelColor: Color,
    valueColor: Color,
    trackColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(tileBackground)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Base Stats",
            color = labelColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            stats.forEach { stat ->
                PokemonStatBar(
                    label = stat.stat.label,
                    value = stat.baseValue,
                    maxValue = 150,
                    accentColor = accentColor,
                    trackColor = trackColor,
                    labelColor = valueColor,
                    valueColor = labelColor,
                )
            }
        }
    }
}

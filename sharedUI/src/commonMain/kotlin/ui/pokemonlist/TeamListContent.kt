package ui.pokemonlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.davidnardie.pokemon.presentation.model.TeamPokemonItem
import com.davidnardie.pokemon.presentation.state.PokemonListState
import org.jetbrains.compose.resources.painterResource
import pokemonapp.sharedui.generated.resources.Res
import pokemonapp.sharedui.generated.resources.img_darkmode_add_to_team
import pokemonapp.sharedui.generated.resources.img_lightmode_add_to_team

@Composable
internal fun TeamListContent(
    teamItems: List<TeamPokemonItem>,
    teamCount: Int,
    isDarkTheme: Boolean,
    colors: PokemonListColors,
    onRemoveFromTeam: (Int) -> Unit,
    onAddMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        teamItems.forEach { item ->
            TeamCard(
                item = item,
                isDarkTheme = isDarkTheme,
                colors = colors,
                onRemove = { onRemoveFromTeam(item.pokemon.id) },
            )
        }

        if (teamCount < PokemonListState.MAX_TEAM_SIZE) {
            AddMoreToTeamButton(
                isDarkTheme = isDarkTheme,
                colors = colors,
                onClick = onAddMoreClick,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun TeamCard(
    item: TeamPokemonItem,
    isDarkTheme: Boolean,
    colors: PokemonListColors,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(24.dp)
    val imageShape = RoundedCornerShape(16.dp)
    val cardBackground = if (isDarkTheme) Color(0xFF2A2A2A) else Color.White
    val imageBackground = if (isDarkTheme) Color(0xFF1C1C1E) else Color(0xFFF1F1F1)
    val removeButtonBackground = if (isDarkTheme) Color.White else Color(0xFF3D3D3D)
    val removeIconColor = if (isDarkTheme) Color.Black else Color.White

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(cardBackground)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(imageShape)
                .background(imageBackground),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = pokemonImageUrl(item.pokemon.id),
                contentDescription = item.pokemon.name,
                modifier = Modifier
                    .size(72.dp)
                    .padding(4.dp),
                contentScale = ContentScale.Fit,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = item.pokemon.displayName(),
                color = colors.primaryText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )

            if (item.types.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    item.types.forEach { type ->
                        PokemonTypeBadge(
                            type = type,
                            isDarkTheme = isDarkTheme,
                        )
                    }
                }
            }

            Text(
                text = item.addedDateLabel,
                color = colors.secondaryText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
            )
        }

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(removeButtonBackground)
                .clickable(onClick = onRemove),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "✕",
                color = removeIconColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun AddMoreToTeamButton(
    isDarkTheme: Boolean,
    colors: PokemonListColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Image(
            painter = painterResource(
                if (isDarkTheme) Res.drawable.img_darkmode_add_to_team
                else Res.drawable.img_lightmode_add_to_team,
            ),
            contentDescription = "Add more Pokémon",
            modifier = Modifier.size(176.dp),
        )
        Text(
            text = "Add more pokemons\nto your team",
            color = if (isDarkTheme) Color(0xFF8E8E93) else colors.primaryText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
        )
    }
}

private fun com.davidnardie.pokemon.domain.model.Pokemon.displayName(): String =
    name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

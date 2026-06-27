package ui.pokemonlist

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.davidnardie.pokemon.domain.model.Pokemon
import ui.theme.PokemonCardTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemon: Pokemon,
    isLoading: Boolean,
    networkError: Boolean,
    isCacheEmpty: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = isSystemInDarkTheme()
    val colors = rememberPokemonListColors(isDarkTheme)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.screenBackground,
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(
                        text = pokemon.displayName(),
                        color = colors.primaryText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text(
                            text = "Back",
                            color = colors.primaryText,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.screenBackground,
                    scrolledContainerColor = colors.screenBackground,
                ),
                windowInsets = WindowInsets(0, 0, 0, 0),
            )
        },
    ) { innerPadding ->
        PokemonLoadingView(
            isLoading = isLoading,
            networkError = networkError,
            isCacheEmpty = isCacheEmpty,
            isDarkTheme = isDarkTheme,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            PokemonDetailContent(
                pokemon = pokemon,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
            )
        }
    }
}

@Composable
private fun PokemonDetailContent(
    pokemon: Pokemon,
    modifier: Modifier = Modifier,
) {
    val usesDistinctImageSurface = PokemonCardTheme.usesDistinctImageSurface

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(PokemonCardTheme.containerColor)
                .then(
                    if (usesDistinctImageSurface) {
                        Modifier.padding(8.dp)
                    } else {
                        Modifier
                    },
                ),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = pokemonImageUrl(pokemon.id),
                contentDescription = pokemon.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentScale = ContentScale.Fit,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = pokemon.displayName(),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = pokemon.formattedId(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

private fun Pokemon.displayName(): String =
    name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

private fun Pokemon.formattedId(): String = "#%03d".format(id)

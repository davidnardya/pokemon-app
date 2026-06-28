package ui.pokemonlist

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.davidnardie.pokemon.presentation.state.PokemonListState
import org.jetbrains.compose.resources.painterResource
import pokemonapp.sharedui.generated.resources.Res
import pokemonapp.sharedui.generated.resources.ic_loading_pokeball_orange
import pokemonapp.sharedui.generated.resources.img_darkmode_loading_list
import pokemonapp.sharedui.generated.resources.img_lightmode_loading_list

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonLoadingView(
    isLoading: Boolean,
    networkError: Boolean,
    isCacheEmpty: Boolean,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    when {
        isLoading && isCacheEmpty -> {
            RefreshableEmptyState(
                modifier = modifier,
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
            ) {
                PokemonLoadingSpinner(modifier = Modifier.fillMaxSize())
            }
        }
        networkError && isCacheEmpty -> {
            RefreshableEmptyState(
                modifier = modifier,
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
            ) {
                PokemonLoadingErrorView(
                    isDarkTheme = isDarkTheme,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        else -> content()
    }
}

@Composable
fun PokemonLoadingView(
    uiState: PokemonListState,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    PokemonLoadingView(
        isLoading = uiState.isLoading,
        networkError = uiState.networkError,
        isCacheEmpty = uiState.isCacheEmpty,
        modifier = modifier,
        isDarkTheme = isDarkTheme,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RefreshableEmptyState(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: (() -> Unit)?,
    content: @Composable () -> Unit,
) {
    if (onRefresh != null) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = modifier,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center,
            ) {
                content()
            }
        }
    } else {
        Box(modifier = modifier) {
            content()
        }
    }
}

@Composable
private fun PokemonLoadingSpinner(
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pokeballSpin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
        ),
        label = "pokeballRotation",
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_loading_pokeball_orange),
            contentDescription = "Loading",
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer { rotationZ = rotation },
        )
    }
}

@Composable
private fun PokemonLoadingErrorView(
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(
                if (isDarkTheme) Res.drawable.img_darkmode_loading_list
                else Res.drawable.img_lightmode_loading_list,
            ),
            contentDescription = "Unable to load Pokémon",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
        )
    }
}

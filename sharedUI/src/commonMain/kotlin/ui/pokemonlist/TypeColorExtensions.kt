package ui.pokemonlist

import androidx.compose.ui.graphics.Color

internal fun Long.toComposeColor(): Color = Color(this.toInt())

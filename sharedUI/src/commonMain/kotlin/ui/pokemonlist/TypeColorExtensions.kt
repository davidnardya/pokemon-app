package ui.pokemonlist

import androidx.compose.ui.graphics.Color

internal fun Long.toComposeColor(): Color = Color(this.toInt())

internal fun Long.toComposeColor(alpha: Float): Color {
    val argb = this.toInt()
    return Color(
        alpha = alpha,
        red = ((argb shr 16) and 0xFF) / 255f,
        green = ((argb shr 8) and 0xFF) / 255f,
        blue = (argb and 0xFF) / 255f,
    )
}

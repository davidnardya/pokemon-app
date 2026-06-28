package com.davidnardie.pokemon.presentation.util

import com.davidnardie.pokemon.domain.model.PokemonType

data class TypeColorPalette(
    val headerBackground: Long,
    val accent: Long,
)

object TypeColors {
    fun palette(type: PokemonType): TypeColorPalette = TypeColorPalette(
        headerBackground = headerBackground(type),
        accent = accent(type),
    )

    fun cardBackground(isDark: Boolean): Long =
        if (isDark) 0xFF1D1D1D else 0xFFF5F5F5

    fun tileBackground(isDark: Boolean): Long =
        if (isDark) 0xFF121212 else 0xFFFFFFFF

    fun statTrack(isDark: Boolean): Long =
        if (isDark) 0xFF2A2A2A else 0xFFE5E5E5

    fun badgeBackground(type: PokemonType, isDark: Boolean): Long =
        if (isDark) darkBadgeBackground(type) else lightBadgeBackground(type)

    fun badgeText(type: PokemonType, isDark: Boolean): Long =
        if (isDark) 0xFFFFFFFF else lightBadgeText(type)

    private fun headerBackground(type: PokemonType): Long = when (type) {
        PokemonType.Bug -> 0xFFB6CD47
        PokemonType.Dark -> 0xFF8A8A8A
        PokemonType.Dragon -> 0xFFB171E1
        PokemonType.Electric -> 0xFFEFCE2B
        PokemonType.Fairy -> 0xFFE67AB5
        PokemonType.Fighting -> 0xFFDA5E5E
        PokemonType.Fire -> 0xFFDA7B5D
        PokemonType.Flying -> 0xFF7F56D6
        PokemonType.Ghost -> 0xFF5F55D9
        PokemonType.Grass -> 0xFF75CC4A
        PokemonType.Ground -> 0xFFF1AC35
        PokemonType.Ice -> 0xFF66A2DE
        PokemonType.Normal -> 0xFFB5B4AF
        PokemonType.Poison -> 0xFFD272E0
        PokemonType.Psychic -> 0xFFE27F93
        PokemonType.Rock -> 0xFF8998DB
        PokemonType.Steel -> 0xFF88B2DA
        PokemonType.Water -> 0xFF66B6D1
    }

    private fun accent(type: PokemonType): Long = headerBackground(type)

    private fun lightBadgeBackground(type: PokemonType): Long = when (type) {
        PokemonType.Grass -> 0xFFC2E1B9
        PokemonType.Poison -> 0xFFE5CCEE
        PokemonType.Fire -> 0xFFF8D5B5
        PokemonType.Water -> 0xFFB8D4F0
        PokemonType.Electric -> 0xFFF8E8A0
        PokemonType.Fairy -> 0xFFF5C2D8
        PokemonType.Steel -> 0xFFC8D4E0
        PokemonType.Dragon -> 0xFFD4C2F0
        PokemonType.Ice -> 0xFFB8E8F0
        PokemonType.Fighting -> 0xFFF0C2B8
        PokemonType.Ground -> 0xFFE8D4B8
        PokemonType.Flying -> 0xFFD4E0F0
        PokemonType.Psychic -> 0xFFF0C2D8
        PokemonType.Bug -> 0xFFD4E8B8
        PokemonType.Rock -> 0xFFE0D4C8
        PokemonType.Ghost -> 0xFFD4C8E8
        PokemonType.Dark -> 0xFFC8C8D4
        PokemonType.Normal -> 0xFFE0E0E0
    }

    private fun lightBadgeText(type: PokemonType): Long = when (type) {
        PokemonType.Grass -> 0xFF4E8234
        PokemonType.Poison -> 0xFF9F5BBA
        PokemonType.Fire -> 0xFFB85C1A
        PokemonType.Water -> 0xFF3A6FA0
        PokemonType.Electric -> 0xFF9A8A1A
        PokemonType.Fairy -> 0xFFB85C8A
        PokemonType.Steel -> 0xFF5A6A7A
        PokemonType.Dragon -> 0xFF6A4A9A
        PokemonType.Ice -> 0xFF3A8A9A
        PokemonType.Fighting -> 0xFF9A4A3A
        PokemonType.Ground -> 0xFF8A6A3A
        PokemonType.Flying -> 0xFF5A7A9A
        PokemonType.Psychic -> 0xFFB85C8A
        PokemonType.Bug -> 0xFF6A8A3A
        PokemonType.Rock -> 0xFF7A6A5A
        PokemonType.Ghost -> 0xFF6A5A8A
        PokemonType.Dark -> 0xFF5A5A6A
        PokemonType.Normal -> 0xFF6A6A6A
    }

    private fun darkBadgeBackground(type: PokemonType): Long = when (type) {
        PokemonType.Grass -> 0xFF4CAF50
        PokemonType.Poison -> 0xFF9C27B0
        PokemonType.Fire -> 0xFFE65100
        PokemonType.Water -> 0xFF1976D2
        PokemonType.Electric -> 0xFFF9A825
        PokemonType.Fairy -> 0xFFE91E63
        PokemonType.Steel -> 0xFF607D8B
        PokemonType.Dragon -> 0xFF7B1FA2
        PokemonType.Ice -> 0xFF00ACC1
        PokemonType.Fighting -> 0xFFD84315
        PokemonType.Ground -> 0xFF8D6E63
        PokemonType.Flying -> 0xFF5C6BC0
        PokemonType.Psychic -> 0xFFAB47BC
        PokemonType.Bug -> 0xFF689F38
        PokemonType.Rock -> 0xFF795548
        PokemonType.Ghost -> 0xFF5E35B1
        PokemonType.Dark -> 0xFF424242
        PokemonType.Normal -> 0xFF757575
    }
}

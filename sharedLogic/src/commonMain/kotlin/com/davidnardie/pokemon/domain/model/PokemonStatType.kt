package com.davidnardie.pokemon.domain.model

enum class PokemonStatType(val label: String) {
    Hp("HP"),
    Attack("ATK"),
    Defense("DEF"),
    SpecialAttack("SATK"),
    SpecialDefense("SDEF"),
    Speed("SPD"),
    ;

    companion object {
        fun fromApiName(name: String): PokemonStatType? = when (name) {
            "hp" -> Hp
            "attack" -> Attack
            "defense" -> Defense
            "special-attack" -> SpecialAttack
            "special-defense" -> SpecialDefense
            "speed" -> Speed
            else -> null
        }
    }
}

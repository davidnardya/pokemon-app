package com.davidnardie.pokemon.domain.model

enum class PokemonType(
    val displayName: String,
    val apiName: String,
) {
    Normal("Normal", "normal"),
    Fire("Fire", "fire"),
    Water("Water", "water"),
    Electric("Electric", "electric"),
    Grass("Grass", "grass"),
    Ice("Ice", "ice"),
    Fighting("Fighting", "fighting"),
    Poison("Poison", "poison"),
    Ground("Ground", "ground"),
    Flying("Flying", "flying"),
    Psychic("Psychic", "psychic"),
    Bug("Bug", "bug"),
    Rock("Rock", "rock"),
    Ghost("Ghost", "ghost"),
    Dragon("Dragon", "dragon"),
    Dark("Dark", "dark"),
    Steel("Steel", "steel"),
    Fairy("Fairy", "fairy"),
    ;

    companion object {
        fun fromApiName(name: String): PokemonType? =
            entries.find { it.apiName == name }
    }
}

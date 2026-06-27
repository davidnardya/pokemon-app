package com.davidnardie.pokemon.di

import com.davidnardie.pokemon.presentation.viewmodel.PokemonViewModel
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin

object KoinHelper {
    private var isStarted = false

    fun start() {
        if (!isStarted) {
            startKoin {
                modules(appModule)
            }
            isStarted = true
        }
    }

    fun pokemonViewModel(): PokemonViewModel = getKoin().get()
}

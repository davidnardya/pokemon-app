package com.davidnardie.pokemon.di

import org.koin.core.context.startKoin

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
}

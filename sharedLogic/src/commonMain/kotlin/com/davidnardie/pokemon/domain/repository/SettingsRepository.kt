package com.davidnardie.pokemon.domain.repository

import com.davidnardie.pokemon.presentation.state.AppSettings

interface SettingsRepository {
    fun loadSettings(): AppSettings

    fun saveSettings(settings: AppSettings)
}

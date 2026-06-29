package com.davidnardie.pokemon.domain.repository

import com.davidnardie.pokemon.presentation.state.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<AppSettings>

    fun loadSettings(): AppSettings

    fun saveSettings(settings: AppSettings)
}

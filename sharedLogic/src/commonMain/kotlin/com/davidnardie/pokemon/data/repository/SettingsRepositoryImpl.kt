package com.davidnardie.pokemon.data.repository

import com.davidnardie.pokemon.data.local.SettingsStorage
import com.davidnardie.pokemon.domain.repository.SettingsRepository
import com.davidnardie.pokemon.presentation.state.AppSettings
import kotlinx.serialization.json.Json

class SettingsRepositoryImpl(
    private val storage: SettingsStorage,
) : SettingsRepository {
    override fun loadSettings(): AppSettings {
        val json = storage.getString(SETTINGS_KEY) ?: return AppSettings()
        return runCatching { jsonParser.decodeFromString<AppSettings>(json) }
            .getOrDefault(AppSettings())
    }

    override fun saveSettings(settings: AppSettings) {
        storage.putString(SETTINGS_KEY, jsonParser.encodeToString(settings))
    }

    private companion object {
        const val SETTINGS_KEY = "app_settings"
        val jsonParser = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }
}

package com.davidnardie.pokemon.data.repository

import com.davidnardie.pokemon.data.local.SettingsStorage
import com.davidnardie.pokemon.domain.repository.SettingsRepository
import com.davidnardie.pokemon.presentation.state.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

class SettingsRepositoryImpl(
    private val storage: SettingsStorage,
) : SettingsRepository {
    private val _settings = MutableStateFlow(loadSettingsInternal())
    override val settings: Flow<AppSettings> = _settings.asStateFlow()

    override fun loadSettings(): AppSettings = _settings.value

    override fun saveSettings(settings: AppSettings) {
        storage.putString(SETTINGS_KEY, jsonParser.encodeToString(settings))
        _settings.value = settings
    }

    private fun loadSettingsInternal(): AppSettings {
        val json = storage.getString(SETTINGS_KEY) ?: return AppSettings()
        return runCatching { jsonParser.decodeFromString<AppSettings>(json) }
            .getOrDefault(AppSettings())
    }

    private companion object {
        const val SETTINGS_KEY = "app_settings"
        val jsonParser = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }
}

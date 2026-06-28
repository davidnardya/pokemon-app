package com.davidnardie.pokemon.data.local

import android.content.Context

private lateinit var settingsApplicationContext: Context

fun initSettingsContext(context: Context) {
    settingsApplicationContext = context.applicationContext
}

actual class SettingsStorage {
    private val preferences by lazy {
        settingsApplicationContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    actual fun getString(key: String): String? = preferences.getString(key, null)

    actual fun putString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "pokemon_app_settings"
    }
}

actual fun createSettingsStorage(): SettingsStorage = SettingsStorage()

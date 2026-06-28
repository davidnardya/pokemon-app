package com.davidnardie.pokemon.data.local

expect class SettingsStorage {
    fun getString(key: String): String?

    fun putString(key: String, value: String)
}

expect fun createSettingsStorage(): SettingsStorage

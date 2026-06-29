package com.davidnardie.pokemon.presentation.state

data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
    val isVisible: Boolean = false,
    val isModePickerVisible: Boolean = false,
    val isAboutVisible: Boolean = false,
)

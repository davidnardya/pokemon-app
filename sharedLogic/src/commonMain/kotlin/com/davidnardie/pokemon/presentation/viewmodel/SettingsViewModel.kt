package com.davidnardie.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidnardie.pokemon.domain.repository.SettingsRepository
import com.davidnardie.pokemon.presentation.state.AppSettings
import com.davidnardie.pokemon.presentation.state.SettingsUiState
import com.davidnardie.pokemon.presentation.state.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        SettingsUiState(settings = settingsRepository.loadSettings()),
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _uiState.update { it.copy(settings = settings) }
            }
        }
    }

    fun openSettings() {
        _uiState.update { it.copy(isVisible = true) }
    }

    fun closeSettings() {
        _uiState.update {
            it.copy(
                isVisible = false,
                isModePickerVisible = false,
                isAboutVisible = false,
            )
        }
    }

    fun setMetricUnits(useMetric: Boolean) {
        updateSettings { settings ->
            settings.copy(useMetricUnits = useMetric)
        }
    }

    fun setUse24HourTime(use24HourTime: Boolean) {
        updateSettings { settings ->
            settings.copy(use24HourTime = use24HourTime)
        }
    }

    fun openModePicker() {
        _uiState.update { it.copy(isModePickerVisible = true) }
    }

    fun closeModePicker() {
        _uiState.update { it.copy(isModePickerVisible = false) }
    }

    fun selectThemeMode(themeMode: ThemeMode) {
        updateSettings { settings ->
            settings.copy(themeMode = themeMode)
        }
        _uiState.update { it.copy(isModePickerVisible = false) }
    }

    fun openAbout() {
        _uiState.update { it.copy(isAboutVisible = true) }
    }

    fun closeAbout() {
        _uiState.update { it.copy(isAboutVisible = false) }
    }

    private fun updateSettings(transform: (AppSettings) -> AppSettings) {
        val updatedSettings = transform(_uiState.value.settings)
        settingsRepository.saveSettings(updatedSettings)
    }
}

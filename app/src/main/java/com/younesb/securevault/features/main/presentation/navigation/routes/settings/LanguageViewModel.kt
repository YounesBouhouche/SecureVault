package com.younesb.securevault.features.main.presentation.navigation.routes.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.core.data.datastore.SettingsPreference
import com.younesb.securevault.core.domain.models.preferences.Language
import com.younesb.securevault.core.domain.models.preferences.Theme
import com.younesb.securevault.core.domain.repositories.PreferencesRepository
import com.younesb.securevault.core.presentation.utils.GlobalEvent
import com.younesb.securevault.core.presentation.utils.GlobalEventsBus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LanguageViewModel(
    val repository: PreferencesRepository
): ViewModel() {
    val language = SettingsPreference.LanguagePref.getState()

    internal fun<T, R> SettingsPreference<T, R>.getState() =
        repository.get(this).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            defaultValue
        )

    internal fun <T, R> SettingsPreference<T, R>.set(value: R) {
        viewModelScope.launch {
            repository.set(this@set, value)
        }
    }

    fun setLanguage(value: Language) {
        SettingsPreference.LanguagePref.set(value)
        viewModelScope.launch {
            GlobalEventsBus.sendEvent(GlobalEvent.SetLanguage(value))
        }
    }
}
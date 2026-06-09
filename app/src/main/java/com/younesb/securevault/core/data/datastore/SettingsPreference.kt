package com.younesb.securevault.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.younesb.securevault.core.domain.models.preferences.ColorScheme
import com.younesb.securevault.core.domain.models.preferences.Language
import com.younesb.securevault.core.domain.models.preferences.Theme

@Suppress("UNCHECKED_CAST")
sealed class SettingsPreference<T, R>(
    val key: Preferences.Key<T>,
    val defaultValue: R,
    val mapToResult: (T) -> R = { it as R },
    val mapToStored: (R) -> T = { it as T },
) {
    data object ThemeMode : SettingsPreference<String, Theme>(
        stringPreferencesKey("theme_mode"),
        Theme.SYSTEM,
        {
            Theme.fromString(it)
        },
        {
            it.name
        },
    )

    data object ExtraDarkMode : SettingsPreference<Boolean, Boolean>(
        booleanPreferencesKey("extra_dark_mode"),
        false,
    )

    data object ColorSchemeMode : SettingsPreference<String, ColorScheme>(
        stringPreferencesKey("color_scheme"),
        ColorScheme.BLUE,
        {
            ColorScheme.fromString(it)
        },
        {
            it.name
        },
    )

    data object LanguagePref : SettingsPreference<String, Language>(
        stringPreferencesKey("language"),
        Language.SYSTEM,
        {
            Language.fromString(it)
        },
        {
            it.name
        },
    )

    data object DynamicColor : SettingsPreference<Boolean, Boolean>(
        booleanPreferencesKey("dynamic_color"),
        true,
    )

    fun getDataFlow(dataStore: DataStore<Preferences>): Flow<R> =
        dataStore.data.map { preferences ->
            preferences[key]?.let { mapToResult(it) } ?: defaultValue
        }

    suspend fun setData(
        dataStore: DataStore<Preferences>,
        value: R,
    ) {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                this[key] = mapToStored(value)
            }
        }
    }
}

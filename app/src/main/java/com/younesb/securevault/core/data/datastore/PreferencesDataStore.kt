package com.younesb.securevault.core.data.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

class PreferencesDataStore(
    private val context: Context,
) {
    companion object {
        private val Context.dataStore by preferencesDataStore(name = "settings")
    }

    suspend fun <T, R> set(
        key: SettingsPreference<T, R>,
        value: R,
    ) {
        key.setData(context.dataStore, value)
    }

    fun <T, R> get(key: SettingsPreference<T, R>) = key.getDataFlow(context.dataStore)
}

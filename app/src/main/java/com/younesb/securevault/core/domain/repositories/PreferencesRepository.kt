package com.younesb.securevault.core.domain.repositories

import com.younesb.securevault.core.data.datastore.SettingsPreference
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun <T, R> set(
        key: SettingsPreference<T, R>,
        value: R,
    )

    fun <T, R> get(key: SettingsPreference<T, R>): Flow<R>
}

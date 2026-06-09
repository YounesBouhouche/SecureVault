package com.younesb.securevault.core.data.repositories

import com.younesb.securevault.core.data.datastore.PreferencesDataStore
import com.younesb.securevault.core.data.datastore.SettingsPreference
import com.younesb.securevault.core.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class PreferencesRepositoryImpl(
    private val dataStore: PreferencesDataStore,
) : PreferencesRepository {
    override suspend fun <T, R> set(
        key: SettingsPreference<T, R>,
        value: R,
    ) {
        dataStore.set(key, value)
    }

    override fun <T, R> get(key: SettingsPreference<T, R>): Flow<R> = dataStore.get(key)
}

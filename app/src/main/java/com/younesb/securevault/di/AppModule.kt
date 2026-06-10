package com.younesb.securevault.di

import com.younesb.securevault.BuildConfig
import com.younesb.securevault.core.data.datastore.PreferencesDataStore
import com.younesb.securevault.core.data.repositories.PreferencesRepositoryImpl
import com.younesb.securevault.core.domain.repositories.PreferencesRepository
import com.younesb.securevault.core.presentation.theme.ThemeViewModel
import com.younesb.securevault.core.util.crypto.Crypto
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::ThemeViewModel)
    singleOf(::PreferencesDataStore)
    single<PreferencesRepository> {
        PreferencesRepositoryImpl(get())
    }
    single { Crypto(BuildConfig.KEY_ALIAS) }
}
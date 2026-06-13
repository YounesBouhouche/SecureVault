package com.younesb.securevault.di

import com.younesb.securevault.features.main.data.datasource.local.database.DocumentsDatabase
import com.younesb.securevault.features.main.data.files_utils.FilesManager
import com.younesb.securevault.features.main.data.repository.DocumentsRepositoryImpl
import com.younesb.securevault.features.main.data.repository.FilesRepositoryImpl
import com.younesb.securevault.features.main.domain.repository.DocumentsRepository
import com.younesb.securevault.features.main.domain.repository.FilesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mainModule = module {
    single {
        FilesManager(context = androidContext(), crypto = get())
    }

    single<DocumentsRepository> { DocumentsRepositoryImpl(get<DocumentsDatabase>().dao) }
    single<FilesRepository> { FilesRepositoryImpl(get()) }
}
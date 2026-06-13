package com.younesb.securevault.di

import com.younesb.securevault.features.main.data.datasource.local.database.DocumentsDatabase
import com.younesb.securevault.features.main.data.repository.DocumentsRepositoryImpl
import com.younesb.securevault.features.main.domain.repository.DocumentsRepository
import org.koin.dsl.module

val mainModule = module {
    single<DocumentsRepository> { DocumentsRepositoryImpl(get<DocumentsDatabase>().dao) }
}
package com.younesb.securevault.di

import com.younesb.securevault.features.main.data.datasource.local.database.DocumentsDatabase
import com.younesb.securevault.features.main.data.files_utils.FilesManager
import com.younesb.securevault.features.main.data.repository.DocumentsRepositoryImpl
import com.younesb.securevault.features.main.data.repository.FilesRepositoryImpl
import com.younesb.securevault.features.main.domain.repository.DocumentsRepository
import com.younesb.securevault.features.main.domain.repository.FilesRepository
import com.younesb.securevault.features.main.domain.usecases.OpenDocumentUseCase
import com.younesb.securevault.features.main.domain.usecases.SaveDocumentUseCase
import com.younesb.securevault.features.main.domain.usecases.SaveNoteUseCase
import com.younesb.securevault.features.main.presentation.screens.MainViewModel
import com.younesb.securevault.features.main.presentation.screens.new_item.NewDocumentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainModule = module {
    single {
        FilesManager(context = androidContext(), crypto = get())
    }
    single<DocumentsRepository> { DocumentsRepositoryImpl(get<DocumentsDatabase>().dao) }
    single<FilesRepository> { FilesRepositoryImpl(get()) }

    viewModelOf(::MainViewModel)
    viewModelOf(::NewDocumentViewModel)

    factoryOf(::SaveDocumentUseCase)
    factoryOf(::SaveNoteUseCase)
    factoryOf(::OpenDocumentUseCase)
}
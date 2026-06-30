package com.younesb.securevault.di

import com.younesb.securevault.features.main.data.datasource.local.database.DocumentsDatabase
import com.younesb.securevault.features.main.data.files_utils.Archiver
import com.younesb.securevault.features.main.data.files_utils.FilesManager
import com.younesb.securevault.features.main.data.repository.DocumentsRepositoryImpl
import com.younesb.securevault.features.main.data.repository.FilesRepositoryImpl
import com.younesb.securevault.features.main.domain.repository.DocumentsRepository
import com.younesb.securevault.features.main.domain.repository.FilesRepository
import com.younesb.securevault.features.main.domain.usecases.CreateFolderUseCase
import com.younesb.securevault.features.main.domain.usecases.CreateTagUseCase
import com.younesb.securevault.features.main.domain.usecases.DeleteDocumentUseCase
import com.younesb.securevault.features.main.domain.usecases.ExportDocumentsUseCase
import com.younesb.securevault.features.main.domain.usecases.GetDocumentUseCase
import com.younesb.securevault.features.main.domain.usecases.GetDocumentsUseCase
import com.younesb.securevault.features.main.domain.usecases.GetFoldersUseCase
import com.younesb.securevault.features.main.domain.usecases.ObserveDocumentsUseCase
import com.younesb.securevault.features.main.domain.usecases.ObserveFolderUseCase
import com.younesb.securevault.features.main.domain.usecases.ObserveFoldersUseCase
import com.younesb.securevault.features.main.domain.usecases.ObserveTagsUseCase
import com.younesb.securevault.features.main.domain.usecases.OpenDocumentUseCase
import com.younesb.securevault.features.main.domain.usecases.RenameDocumentUseCase
import com.younesb.securevault.features.main.domain.usecases.ResetAppUseCase
import com.younesb.securevault.features.main.domain.usecases.SaveDocumentUseCase
import com.younesb.securevault.features.main.domain.usecases.SaveNoteUseCase
import com.younesb.securevault.features.main.domain.usecases.SetFavoriteUseCase
import com.younesb.securevault.features.main.presentation.navigation.routes.home.HomeViewModel
import com.younesb.securevault.features.main.presentation.navigation.routes.document.DocumentViewModel
import com.younesb.securevault.features.main.presentation.navigation.routes.folder.FolderViewModel
import com.younesb.securevault.features.main.presentation.navigation.routes.settings.SettingsViewModel
import com.younesb.securevault.features.main.presentation.screens.MainViewModel
import com.younesb.securevault.features.main.presentation.screens.new_item.NewDocumentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainModule = module {
    single {
        FilesManager(context = androidContext(), crypto = get())
    }
    single {
        Archiver(context = androidContext(), filesManager = get())
    }
    single<DocumentsRepository> { DocumentsRepositoryImpl(get<DocumentsDatabase>()) }
    single<FilesRepository> { FilesRepositoryImpl(get()) }

    viewModelOf(::MainViewModel)
    viewModelOf(::NewDocumentViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::SettingsViewModel)
    viewModel {
        FolderViewModel(
            observeDocumentsUseCase = get(),
            observeFolderUseCase = get(),
            folderId = it[0]
        )
    }
    viewModel {
        DocumentViewModel(
            getDocumentUseCase = get(),
            openDocumentUseCase = get(),
            setFavoriteUseCase = get(),
            renameDocumentUseCase = get(),
            deleteDocumentUseCase = get(),
            documentId = it[0]
        )
    }

    factoryOf(::SaveDocumentUseCase)
    factoryOf(::GetFoldersUseCase)
    factoryOf(::ObserveFoldersUseCase)
    factoryOf(::ObserveDocumentsUseCase)
    factoryOf(::CreateFolderUseCase)
    factoryOf(::CreateTagUseCase)
    factoryOf(::ObserveTagsUseCase)
    factoryOf(::SaveNoteUseCase)
    factoryOf(::OpenDocumentUseCase)
    factoryOf(::GetDocumentUseCase)
    factoryOf(::GetDocumentsUseCase)
    factoryOf(::ObserveFolderUseCase)
    factoryOf(::SetFavoriteUseCase)
    factoryOf(::RenameDocumentUseCase)
    factoryOf(::DeleteDocumentUseCase)
    factoryOf(::ResetAppUseCase)
    factoryOf(::ExportDocumentsUseCase)
}
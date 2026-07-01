package com.younesb.securevault.features.main.presentation.util

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.younesb.securevault.core.presentation.events.CollectEvents
import com.younesb.securevault.features.export.presentation.util.SaveFileDialogManager
import com.younesb.securevault.features.main.presentation.NewDocument.*

@Composable
fun CollectMainEvents(
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) {
        it?.let {
            FilePickerManager.emitResult(
                File(
                    it,
                    FileInfo.getFileProviderInfo(context, it)
                )
            )
        }
    }
    val pickPictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) {
        it?.let {
            FilePickerManager.emitResult(
                Image(
                    it,
                    FileInfo.getFileProviderInfo(context, it)
                )
            )
        }
    }
    val takePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { accepted ->
        ComposeFileProvider.getUri().takeIf { accepted }?.let {
            FilePickerManager.emitResult(
                Image(
                    it,
                    FileInfo.getFileProviderInfo(context, it)
                )
            )
        }
    }
    val saveFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("*/*")
    ) { result ->
        result?.let {
            SaveFileDialogManager.emitResult(it)
        }
    }
    CollectEvents(MainEventsBus.events) {
        when (it) {
            is MainEvent.PickFile -> {
                filePickerLauncher.launch(arrayOf("*/*"))
            }

            is MainEvent.PickPicture -> {
                pickPictureLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }

            is MainEvent.TakePicture -> {
                takePickerLauncher.launch(ComposeFileProvider.createImageUri(context))
            }

            is MainEvent.MainNavigate -> {
                navController.navigate(it.route)
            }

            is MainEvent.MainPopBackStack -> {
                navController.popBackStack()
            }

            MainEvent.RequestNewNote -> {
                FilePickerManager.emitResult(Note())
            }

            is MainEvent.PickSavePath -> {
                saveFileLauncher.launch(it.fileName)
            }
        }
    }
}
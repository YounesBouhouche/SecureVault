package com.younesb.securevault.features.main.presentation.util

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.younesb.securevault.core.presentation.events.CollectEvents
import com.younesb.securevault.features.main.presentation.NewDocument

@Composable
fun CollectMainEvents(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) {
        it?.let {
            FilePickerManager.emitResult(NewDocument.File(it))
        }
    }
    val pickPictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) {
        it?.let {
            FilePickerManager.emitResult(NewDocument.Image(it))
        }
    }
    val takePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { accepted ->
        ComposeFileProvider.getUri().takeIf { accepted }?.let {
            FilePickerManager.emitResult(NewDocument.Image(it))
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

            MainEvent.RequestNewNote -> {
                FilePickerManager.emitResult(NewDocument.Note())
            }
        }
    }
}
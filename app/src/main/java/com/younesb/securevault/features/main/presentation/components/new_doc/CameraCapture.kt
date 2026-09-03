package com.younesb.securevault.features.main.presentation.components.new_doc

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.runtime.Stable
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Stable
sealed class CameraFacing {
    data object Front : CameraFacing()
    data object Back : CameraFacing()
    data class Custom(val selector: CameraSelector) : CameraFacing()
}

private fun CameraFacing.toCameraSelector(): CameraSelector = when (this) {
    CameraFacing.Back -> CameraSelector.DEFAULT_BACK_CAMERA
    CameraFacing.Front -> CameraSelector.DEFAULT_FRONT_CAMERA
    is CameraFacing.Custom -> selector
}

@Composable
fun CameraCapture(
    modifier: Modifier = Modifier,
    cameraFacing: CameraFacing = CameraFacing.Back,
    flashlight: Boolean = false,
    imageCaptureConfig: ImageCapture.Builder.() -> Unit = {},
    content: @Composable (
        imageCapture: ImageCapture,
        hasPermission: Boolean,
        requestPermission: () -> Unit,
        preview: @Composable () -> Unit
    ) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var previewUseCase by remember { mutableStateOf<Preview?>(null) }
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }

    val imageCaptureUseCase = remember {
        ImageCapture.Builder()
            .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
            .apply(imageCaptureConfig)
            .build()
    }

    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        cameraProvider = context.getCameraProvider()
    }

    val currentFlashlight by rememberUpdatedState(flashlight)

    LaunchedEffect(cameraFacing, previewUseCase, hasCameraPermission, cameraProvider) {
        val provider = cameraProvider ?: return@LaunchedEffect
        val preview = previewUseCase ?: return@LaunchedEffect

        try {
            provider.unbindAll()
            val camera = provider.bindToLifecycle(
                lifecycleOwner,
                cameraFacing.toCameraSelector(),
                preview,
                imageCaptureUseCase
            )
            cameraControl = camera.cameraControl
            cameraControl?.enableTorch(currentFlashlight)
        } catch (ex: Exception) {
            Log.e("CameraCapture", "Failed to bind camera use cases", ex)
        }
    }

    LaunchedEffect(flashlight) {
        cameraControl?.enableTorch(flashlight)
    }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            cameraProvider?.unbindAll()
        }
    }

    content(
        imageCaptureUseCase,
        hasCameraPermission,
        { permissionLauncher.launch(Manifest.permission.CAMERA) }
    ) {
        CameraPreview(
            modifier = modifier.fillMaxSize(),
            onUseCase = { previewUseCase = it }
        )
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    onUseCase: (Preview) -> Unit = {}
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            onUseCase(
                Preview.Builder()
                    .build()
                    .also { it.surfaceProvider = previewView.surfaceProvider }
            )
            previewView
        }
    )
}

suspend fun ImageCapture.takePicture(executor: Executor): File {
    val photoFile = withContext(Dispatchers.IO) {
        File.createTempFile("image", "jpg")
    }

    return suspendCancellableCoroutine { continuation ->
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    continuation.resume(photoFile)
                }

                override fun onError(ex: ImageCaptureException) {
                    Log.e("TakePicture", "Image capture failed", ex)
                    continuation.resumeWithException(ex)
                }
            }
        )
    }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCancellableCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { future ->
            Futures.addCallback(future, object : FutureCallback<ProcessCameraProvider> {
                override fun onSuccess(result: ProcessCameraProvider) {
                    continuation.resume(future.get())
                }

                override fun onFailure(t: Throwable) {
                    continuation.resumeWithException(t)
                }
            }, executor)
        }
    }

val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)
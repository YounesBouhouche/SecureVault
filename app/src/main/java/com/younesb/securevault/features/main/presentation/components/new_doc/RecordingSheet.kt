package com.younesb.securevault.features.main.presentation.components.new_doc

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.ButtonsRow
import com.younesb.securevault.core.presentation.components.ExpressiveButton
import kotlinx.collections.immutable.toImmutableList
import java.io.File

@Composable
fun RecordingSheet(
    modifier: Modifier,
    onClose: () -> Unit,
    onConfirm: (Uri) -> Unit
) {
    val context = LocalContext.current
    val file = remember {
        File.createTempFile("rec_", "")
    }
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
    }
    val state = rememberAudioRecorderState {
        setOutputFile(file)
    }
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shadowElevation = 4.dp
    ) {
        Column(
            Modifier.fillMaxWidth().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(hasPermission) { permitted ->
                if (permitted) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.large)
                                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedContent(state.status == AudioRecorderStatus.Idle) {
                                if (it) {
                                        Text(
                                            text = stringResource(R.string.recording_sheet_hint),
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                } else {
                                    LiveWaveform(
                                        amplitudes = state.amplitudes.toImmutableList(),
                                        modifier = Modifier.fillMaxWidth(),
                                        maxHeight = 200.dp
                                    )
                                }
                            }
                        }
                        AnimatedContent(
                            targetState = state.status,
                            contentKey = {
                                when(it) {
                                    AudioRecorderStatus.Idle -> 0
                                    AudioRecorderStatus.Recording -> 1
                                    AudioRecorderStatus.Paused -> 1
                                    AudioRecorderStatus.Stopped -> 2
                                }
                            }
                        ) { status ->
                            when(status) {
                                AudioRecorderStatus.Idle -> {
                                    ButtonsRow(
                                        count = 2,
                                        icon = { null },
                                        outlined = { it == 0 },
                                        text = {
                                            when(it) {
                                                0 -> stringResource(R.string.cancel)
                                                else -> stringResource(R.string.start)
                                            }
                                        }
                                    ) {
                                        when(it) {
                                            0 -> onClose()
                                            else -> state.start()
                                        }
                                    }
                                }
                                AudioRecorderStatus.Stopped -> {
                                    ButtonsRow(
                                        count = 2,
                                        icon = { null },
                                        outlined = { it == 0 },
                                        text = {
                                            when(it) {
                                                0 -> stringResource(R.string.cancel)
                                                else -> stringResource(R.string.ok)
                                            }
                                        }
                                    ) {
                                        when(it) {
                                            0 -> state.clear()
                                            else -> onConfirm(file.toUri())
                                        }
                                    }
                                }
                                else -> {
                                    ButtonsRow(
                                        count = 3,
                                        icon = { null },
                                        outlined = { it != 1 },
                                        text = {
                                            when(it) {
                                                0 -> stringResource(R.string.cancel)
                                                1 -> stringResource(
                                                    if (status == AudioRecorderStatus.Paused) R.string.resume
                                                    else R.string.pause
                                                )
                                                else -> stringResource(R.string.stop)
                                            }
                                        }
                                    ) {
                                        when(it) {
                                            0 -> state.clear()
                                            1 ->
                                                if (status == AudioRecorderStatus.Paused) state.start()
                                                else state.pause()
                                            else -> state.stop()
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    ExpressiveButton(
                        stringResource(R.string.request_permission),
                        size = ButtonDefaults.MediumContainerHeight
                    ) {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }
            }
        }
    }
}
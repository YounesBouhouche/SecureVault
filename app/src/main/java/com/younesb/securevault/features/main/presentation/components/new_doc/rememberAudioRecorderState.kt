package com.younesb.securevault.features.main.presentation.components.new_doc

import android.media.MediaRecorder
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun rememberAudioRecorderState(
    onError: (e: Exception) -> Unit = {},
    config: MediaRecorder.() -> Unit = {},
): AudioRecorderState {
    var status by rememberSaveable { mutableStateOf(AudioRecorderStatus.Idle) }
    val amplitudes = remember { mutableStateListOf<Int>() }
    val context = LocalContext.current
    var recorder = remember<MediaRecorder?> { null }

    LaunchedEffect(status) {
        when(status) {
            AudioRecorderStatus.Recording -> {
                if (recorder == null) {
                    recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        MediaRecorder(context)
                    } else {
                        @Suppress("DEPRECATION")
                        MediaRecorder()
                    }.apply {
                        setAudioSource(MediaRecorder.AudioSource.MIC)
                        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                        setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                        config()
                    }
                    recorder?.prepare()
                    recorder?.start()
                }
                try {
                    while (status == AudioRecorderStatus.Recording) {
                        val maxAmp = try {
                            recorder?.maxAmplitude ?: 0
                        } catch (_: Exception) {
                            0
                        }
                        amplitudes += maxAmp
                        delay(50L.milliseconds)
                    }
                } catch (e: Exception) {
                    onError(e)
                }
            }
            AudioRecorderStatus.Idle -> {
                amplitudes.clear()
                recorder?.stop()
                recorder?.release()
            }
            AudioRecorderStatus.Paused -> {
                recorder?.pause()
            }
            AudioRecorderStatus.Stopped -> {
                recorder?.stop()
                recorder?.release()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            recorder?.stop()
            recorder?.release()
            recorder = null
        }
    }

    return remember(status) {
        AudioRecorderState(
            status = status,
            amplitudes = amplitudes,
            start = {
                status = AudioRecorderStatus.Recording
                    },
            pause = {
                status = AudioRecorderStatus.Paused
                    },
            stop = {
                status = AudioRecorderStatus.Stopped
            },
            clear = {
                status = AudioRecorderStatus.Idle
            }
        )
    }
}

enum class AudioRecorderStatus {
    Idle,
    Recording,
    Paused,
    Stopped
}

data class AudioRecorderState(
    val status: AudioRecorderStatus,
    val amplitudes: List<Int>,
    val start: () -> Unit,
    val pause: () -> Unit,
    val stop: () -> Unit,
    val clear: () -> Unit
)

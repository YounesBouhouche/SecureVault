package com.younesb.securevault.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

internal fun KeyEvent.getDigit() = when (key) {
    Key.Zero, Key.NumPad0 -> 0
    Key.One, Key.NumPad1 -> 1
    Key.Two, Key.NumPad2 -> 2
    Key.Three, Key.NumPad3 -> 3
    Key.Four, Key.NumPad4 -> 4
    Key.Five, Key.NumPad5 -> 5
    Key.Six, Key.NumPad6 -> 6
    Key.Seven, Key.NumPad7 -> 7
    Key.Eight, Key.NumPad8 -> 8
    Key.Nine, Key.NumPad9 -> 9
    else -> null
}

@OptIn(ExperimentalGridApi::class)
@Composable
fun PinGrid(
    addDigit: (Int) -> Unit,
    removeLastDigit: () -> Unit,
    clearPin: () -> Unit,
    modifier: Modifier = Modifier,
    numbersContainerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    textStyle: TextStyle = MaterialTheme.typography.headlineLarge,
    enabled: Boolean = true,
) {
    Column(
        modifier = modifier.onKeyEvent {
            it.getDigit()?.let { digit ->
                if (it.type == KeyEventType.KeyUp) addDigit(digit)
                return@onKeyEvent true
            }
            if (it.key == Key.Backspace) {
                if (it.type == KeyEventType.KeyUp) removeLastDigit()
                return@onKeyEvent true
            }
            false
        },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(4) { i ->
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) { j ->
                    val index = i * 3 + j
                    PinButton(
                        onClick = {
                            when (index) {
                                in 0..8 -> addDigit((index + 1) % 10)
                                10 -> addDigit(0)
                                9 -> clearPin()
                                11 -> removeLastDigit()
                            }
                        },
                        enabled = enabled,
                        style = textStyle,
                        modifier = Modifier.fillMaxSize(),
                        containerColor = when(index) {
                            9 -> MaterialTheme.colorScheme.errorContainer
                            11 -> MaterialTheme.colorScheme.secondaryContainer
                            else -> numbersContainerColor
                        },
                        text = {
                            when (index) {
                                9 -> Icon(
                                    Icons.Default.Clear,
                                    null
                                )
                                11 -> Icon(
                                    Icons.AutoMirrored.Default.Backspace,
                                    null
                                )
                                else -> Text(
                                    text = when (index) {
                                        in 0..8 -> "${index + 1}"
                                        else -> "0"
                                    },
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PinGridPreview() {
    Box(Modifier.fillMaxSize()) {
        PinGrid(
            addDigit = {},
            removeLastDigit = {},
            clearPin = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
        modifier = modifier,
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
                        modifier = Modifier.fillMaxSize().weight(1f),
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
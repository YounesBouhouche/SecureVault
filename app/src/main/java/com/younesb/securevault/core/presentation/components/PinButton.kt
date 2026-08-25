package com.younesb.securevault.core.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.younesb.securevault.R

@Composable
fun PinButton(
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = contentColorFor(containerColor),
    enabled: Boolean = true,
    style: TextStyle = MaterialTheme.typography.headlineLarge,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val width by animateFloatAsState(
if (pressed) 160f else 100f
    )
    val variableFontFamily by remember {
        derivedStateOf {
            FontFamily(
                Font(
                    resId = R.font.gflex,
                    variationSettings = FontVariation.Settings(
                        FontVariation.width(width),
                        FontVariation.Setting("ROND", 100f),
                        FontVariation.weight(600)
                    )
                )
            )
        }
    }
    ExpressiveButton(
        text = {
            ProvideTextStyle(
                value = style.copy(
                    fontFamily = variableFontFamily,
                    color = contentColor
                ),
                content = text
            )
        },
        contentPadding = PaddingValues(0.dp),
        size = ButtonDefaults.MediumContainerHeight,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    )
}

@Preview
@Composable
private fun PinButtonPrev() {
    Box(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth()) {
            repeat(5) {
                PinButton(
                    text = {
                        Text(text = "$it")
                    },
                    modifier = Modifier.weight(1f),
                    onClick = {}
                )
            }
        }
    }
}
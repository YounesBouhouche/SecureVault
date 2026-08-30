package com.younesb.securevault.features.auth.presentation.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.MeshGradientPainter

fun Modifier.meshPainter(
    color1: Color,
    color2: Color,
    color3: Color,
    color4: Color,
) = then(
    Modifier.paint(
        MeshGradientPainter(
            rows = 3,
            columns = 3,
            hasBicubicColor = true,
        ) {
            setVertex(0, 0, Offset(0.0000f, 0.0000f), color1)
            setVertex(0, 1, Offset(0.3000f, 0.0000f), color2)
            setVertex(0, 2, Offset(0.7000f, 0.0000f), color3)
            setVertex(0, 3, Offset(1.0000f, 0.0000f), color2)
            setVertex(1, 0, Offset(0.0000f, 0.3000f), color4)
            setVertex(1, 1, Offset(0.2000f, 0.4000f), color4)
            setVertex(1, 2, Offset(0.7000f, 0.2000f), color4)
            setVertex(1, 3, Offset(1.0000f, 0.3000f), color1)
            setVertex(2, 0, Offset(0.0000f, 0.7000f), color2)
            setVertex(2, 1, Offset(0.3000f, 0.8000f), color4)
            setVertex(2, 2, Offset(0.7000f, 0.6000f), color1)
            setVertex(2, 3, Offset(1.0000f, 0.7000f), color3)
            setVertex(3, 0, Offset(0.0000f, 1.0000f), color1)
            setVertex(3, 1, Offset(0.3000f, 1.0000f), color4)
            setVertex(3, 2, Offset(0.7000f, 1.0000f), color4)
            setVertex(3, 3, Offset(1.0000f, 1.0000f), color1)
        }
    )
)
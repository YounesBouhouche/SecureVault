package com.younesb.securevault.features.main.presentation.components.new_doc

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.younesb.mydesignsystem.presentation.components.ExpressiveIconButton
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.ButtonsRow
import com.younesb.securevault.core.presentation.components.ExpressiveButton
import com.younesb.securevault.core.presentation.components.Image
import kotlinx.coroutines.launch

@Composable
fun CameraSheet(
    modifier: Modifier,
    onClose: () -> Unit,
    onConfirm: (Uri) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var cameraFacing by remember { mutableStateOf<CameraFacing>(CameraFacing.Back) }
    var image by remember { mutableStateOf<Uri?>(null) }
    var flashlightOn by remember { mutableStateOf(false) }
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shadowElevation = 4.dp
    ) {
        CameraCapture(
            cameraFacing = cameraFacing,
            flashlight = flashlightOn
        ) { imageCapture, hasPermission, requestPermission, preview ->
            val state by remember {
                derivedStateOf {
                    if (!hasPermission) 0
                    else if (image == null) 1
                    else 2
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(6.dp, 6.dp, 6.dp, 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp, max = 400.dp),
                    shape = MaterialTheme.shapes.largeIncreased,
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                ) {
                    Box {
                        preview()
                        image?.let {
                            Image(
                                model = it,
                                icon = Icons.Default.Image,
                                modifier = Modifier.fillMaxSize(),
                                shape = MaterialTheme.shapes.large
                            )
                        }
                        ExpressiveIconButton(
                            Icons.AutoMirrored.Default.ArrowBack,
                            modifier = Modifier.padding(16.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(.4f)
                            ),
                            onClick = onClose
                        )
                    }
                }
                AnimatedContent(
                    targetState = state
                ) { currentState ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        when (currentState) {
                            1 -> {
                                ExpressiveIconButton(
                                    icon = ImageVector.vectorResource(R.drawable.cameraswitch),
                                    size = IconButtonDefaults.mediumIconSize
                                ) {
                                    cameraFacing =
                                        if (cameraFacing == CameraFacing.Back)
                                            CameraFacing.Front
                                        else
                                            CameraFacing.Back
                                }
                                ShutterButton {
                                    scope.launch {
                                        image = imageCapture.takePicture(context.executor).toUri()
                                    }
                                }
                                ExpressiveIconButton(
                                    icon = ImageVector.vectorResource(
                                        if (flashlightOn) R.drawable.flashlight_on
                                        else R.drawable.flashlight_off
                                    ),
                                    size = IconButtonDefaults.mediumIconSize
                                ) {
                                    flashlightOn = !flashlightOn
                                }
                            }
                            2 -> {
                                ButtonsRow(
                                    count = 2,
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    outlined = { it == 0 },
                                    icon = {
                                        if (it == 0) Icons.Default.Delete
                                        else Icons.AutoMirrored.Default.ArrowForward
                                    },
                                    text = {
                                        if (it == 0) stringResource(R.string.retake)
                                        else stringResource(R.string.next)
                                    }
                                ) {
                                    if (it == 0) image = null
                                    else image?.let(onConfirm)
                                }
                            }
                            else -> {
                                ExpressiveButton(
                                    stringResource(R.string.request_permission),
                                    size = ButtonDefaults.MediumContainerHeight
                                ) {
                                    requestPermission()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShutterButton(
    modifier: Modifier = Modifier,
    size: Dp = ButtonDefaults.MediumContainerHeight,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val padding by animateDpAsState(
        if (pressed) 8.dp else 4.dp
    )
    Box(
        modifier = modifier
            .border(2.dp, color, CircleShape)
            .size(size)
            .padding(padding)
            .clip(CircleShape)
            .clickable(onClick = onClick, interactionSource = interactionSource)
            .background(color),
    )
}
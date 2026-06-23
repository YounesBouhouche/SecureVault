package com.younesb.securevault.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.younesb.securevault.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String? = null
) {
    if (visible) {
        BasicAlertDialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            modifier = modifier
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(.9f),
                shape = MaterialTheme.shapes.extraExtraLarge,
                color = MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp, 24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    icon?.let {
                        Icon(
                            icon,
                            null,
                            Modifier.size(60.dp).align(Alignment.CenterHorizontally),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    title?.let {
                        Text(
                            text = title,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                        )
                    }
                    content()
                }
            }
        }
    }
}
package com.younesb.securevault.features.export.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EnhancedEncryption
import androidx.compose.material.icons.rounded.NoEncryption
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.ButtonsRow
import com.younesb.securevault.core.presentation.utils.expressiveListItemShape

@Composable
fun IdleContent(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onExport: (Boolean) -> Unit,
) {
    var isEncrypted by rememberSaveable {
        mutableStateOf(false)
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(2) { index ->
            val selected = isEncrypted == (index == 0)
            val color by animateColorAsState(
                if (selected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceContainer
            )
            Surface(
                color = color,
                shape = expressiveListItemShape(
                    index,
                    2,
                    largeShape = MaterialTheme.shapes.extraLarge
                ),
                onClick = {
                    isEncrypted = index == 0
                }
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector =
                            if (index == 0) Icons.Rounded.EnhancedEncryption
                            else Icons.Rounded.NoEncryption,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column(
                        Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(
                                if (index == 0) R.string.encrypted_export
                                else R.string.decrypted_export
                            ),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = stringResource(
                                if (index == 0) R.string.encrypted_export_description
                                else R.string.decrypted_export_description
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = LocalContentColor.current.copy(
                                alpha = 0.7f
                            )
                        )
                    }
                }
            }
        }

        ButtonsRow(
            count = 2,
            onClick = { index ->
                when (index) {
                    0 -> onCancel()
                    1 -> onExport(isEncrypted)
                }
            },
            text = { index ->
                when (index) {
                    0 -> stringResource(R.string.cancel)
                    1 -> stringResource(R.string.ok)
                    else -> ""
                }
            },
            icon = { null },
            outlined = { index -> index == 0 },
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
        )
    }
}
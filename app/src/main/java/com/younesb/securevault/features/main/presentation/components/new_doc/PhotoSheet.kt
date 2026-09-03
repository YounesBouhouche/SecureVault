package com.younesb.securevault.features.main.presentation.components.new_doc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.ButtonsRow
import com.younesb.securevault.core.presentation.components.ExpressiveTextField
import com.younesb.securevault.core.presentation.components.Image

@Composable
fun PhotoSheet(
    modifier: Modifier,
    filename: String,
    uri: String?,
    onCancel: () -> Unit,
    onConfirm: (name: String, description: String) -> Unit,
) {
    val fileNameState = rememberTextFieldState(initialText = filename)
    val descriptionState = rememberTextFieldState(initialText = "")
    Surface(
        modifier = modifier.imePadding(),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shadowElevation = 4.dp
    ) {
        Column(
            Modifier.fillMaxWidth().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(100.dp),
                    model = uri,
                    icon = ImageVector.vectorResource(R.drawable.photo_prints_filled),
                )
                ExpressiveTextField(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    state = fileNameState,
                    label = {
                        Text(text = stringResource(R.string.name))
                    },
                    lineLimits = TextFieldLineLimits.SingleLine
                )
            }
            ExpressiveTextField(
                modifier = Modifier.fillMaxWidth(),
                state = descriptionState,
                label = {
                    Text(text = stringResource(R.string.description))
                },
            )
            ButtonsRow(
                count = 2,
                onClick = { index ->
                    when (index) {
                        0 -> onCancel()
                        1 -> onConfirm(
                            fileNameState.text.toString(),
                            descriptionState.text.toString()
                        )
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
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
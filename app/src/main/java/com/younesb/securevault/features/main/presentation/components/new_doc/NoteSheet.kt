package com.younesb.securevault.features.main.presentation.components.new_doc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.ButtonsRow
import com.younesb.securevault.core.presentation.components.ExpressiveTextField

@Composable
fun NoteSheet(
    modifier: Modifier,
    onCancel: () -> Unit,
    onConfirm: (name: String, description: String) -> Unit
) {
    val fileNameState = rememberTextFieldState()
    val descriptionState = rememberTextFieldState()
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
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
            ExpressiveTextField(
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                state = fileNameState,
                label = {
                    Text(text = stringResource(R.string.name))
                },
                lineLimits = TextFieldLineLimits.SingleLine
            )
            ExpressiveTextField(
                modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
                state = descriptionState,
                label = {
                    Text(text = stringResource(R.string.note_content))
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
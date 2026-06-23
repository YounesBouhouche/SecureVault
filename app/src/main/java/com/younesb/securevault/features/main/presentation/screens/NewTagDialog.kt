package com.younesb.securevault.features.main.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.ExpressiveButton
import com.younesb.securevault.core.presentation.components.ExpressiveTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTagDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: (name: String) -> Unit
) {
    val textFieldState = rememberTextFieldState()
    LaunchedEffect(visible) {
        if (visible) {
            textFieldState.setTextAndPlaceCursorAtEnd("")
        }
    }
    if (visible) {
        BasicAlertDialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(.9f),
                shape = MaterialTheme.shapes.extraExtraLarge,
                color = MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp, 24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.new_tag),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    ExpressiveTextField(
                        state = textFieldState,
                        placeholder = {
                            Text(stringResource(R.string.tag_name))
                        },
                        lineLimits = TextFieldLineLimits.SingleLine,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ExpressiveButton(
                            text = stringResource(R.string.cancel),
                            size = ButtonDefaults.MediumContainerHeight,
                            outlined = true,
                            modifier = Modifier.weight(1f),
                            onClick = onDismissRequest
                        )
                        ExpressiveButton(
                            text = stringResource(R.string.ok),
                            size = ButtonDefaults.MediumContainerHeight,
                            modifier = Modifier.weight(1f)
                        ) {
                            onConfirm(textFieldState.text.toString())
                        }
                    }
                }
            }
        }
    }
}
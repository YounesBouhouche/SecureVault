package com.younesb.securevault.features.main.presentation.navigation.routes.document

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndSelectAll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.ButtonsRow
import com.younesb.securevault.core.presentation.components.Dialog
import com.younesb.securevault.core.presentation.components.ExpressiveTextField

@Composable
fun RenameDialog(
    visible: Boolean,
    documentName: String,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onConfirm: (newName: String) -> Unit = {},
) {
    Dialog(
        visible = visible,
        title = stringResource(R.string.rename),
        content = {
            val textFieldState = rememberTextFieldState()
            LaunchedEffect(Unit) {
                textFieldState.setTextAndSelectAll(documentName)
            }
            ExpressiveTextField(
                state = textFieldState,
                label = { Text(stringResource(R.string.new_name)) },
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier = Modifier.fillMaxWidth()
            )
            ButtonsRow(
                count = 2,
                onClick = { index ->
                    when (index) {
                        0 -> onDismissRequest()
                        1 -> onConfirm(textFieldState.text.toString())
                    }
                },
                text = { index ->
                    when (index) {
                        0 -> stringResource(R.string.cancel)
                        1 -> stringResource(R.string.confirm)
                        else -> ""
                    }
                },
                icon = { null },
                outlined = { index -> index == 0 },
                modifier = Modifier.fillMaxWidth()
            )
        },
        onDismissRequest = onDismissRequest,
        modifier = modifier
    )
}
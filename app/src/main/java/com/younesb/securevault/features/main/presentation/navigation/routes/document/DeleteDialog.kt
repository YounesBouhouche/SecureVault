package com.younesb.securevault.features.main.presentation.navigation.routes.document

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.ButtonsRow
import com.younesb.securevault.core.presentation.components.Dialog

@Composable
fun DeleteDialog(
    visible: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    Dialog(
        visible = visible,
        title = stringResource(R.string.delete),
        icon = Icons.Rounded.Delete,
        content = {
            Text(
                text = stringResource(R.string.delete_confirmation),
                modifier = Modifier.fillMaxWidth(),
            )
            ButtonsRow(
                count = 2,
                onClick = { index ->
                    when (index) {
                        0 -> onDismissRequest()
                        1 -> onConfirm()
                    }
                },
                text = { index ->
                    when (index) {
                        0 -> stringResource(R.string.no)
                        1 -> stringResource(R.string.yes)
                        else -> ""
                    }
                },
                icon = { null },
                outlined = { index -> index == 0 },
                colors = {
                    when (it) {
                        0 -> ButtonDefaults.outlinedButtonColors()
                        1 -> ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                        else -> ButtonDefaults.buttonColors()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        onDismissRequest = onDismissRequest,
        modifier = modifier
    )
}
package com.younesb.securevault.core.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ExpressiveTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    label: @Composable (TextFieldLabelScope.() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    error: String? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    TextField(
        state = state,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        readOnly = readOnly,
        enabled = enabled,
        shape = MaterialTheme.shapes.large,
        contentPadding = PaddingValues(16.dp),
        lineLimits = lineLimits,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = error != null,
        supportingText = error?.let { e ->
            {
                Text(e)
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            errorContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            errorLabelColor = MaterialTheme.colorScheme.error,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}
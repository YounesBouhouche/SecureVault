package com.younesb.securevault.features.main.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.younesb.securevault.core.presentation.components.ExpressiveTextField

@Composable
fun SearchTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    error: String? = null,
    focusedContainerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    unfocusedContainerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    interactionSource: MutableInteractionSource? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) =
    ExpressiveTextField(
        state,
        modifier.height(60.dp),
        null,
        placeholder?.let { { Text(it, maxLines = 1, overflow = TextOverflow.Ellipsis) } },
        readOnly,
        enabled,
        error,
        RoundedCornerShape(100),
        PaddingValues(horizontal = 8.dp),
        focusedContainerColor,
        unfocusedContainerColor,
        keyboardOptions,
        onKeyboardAction,
        lineLimits,
        interactionSource,
        {
            Icon(Icons.Default.Search, null, Modifier.padding(start = 12.dp))
        },
        trailingIcon,
    )
package com.younesb.securevault.features.main.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

fun LazyListScope.listHeader(
    key: Any? = null,
    text: @Composable () -> String,
) {
    stickyHeader(key = key) {
        Text(
            text = text(),
            modifier = Modifier
                .padding(2.dp, 6.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp, 6.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}
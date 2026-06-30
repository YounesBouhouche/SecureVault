package com.younesb.securevault.features.main.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.younesb.securevault.core.presentation.utils.expressiveListItemShape

fun LazyListScope.listHeader(
    key: Any? = null,
    text: @Composable () -> String,
) {
    stickyHeader(key = key) {
        Text(
            text = text(),
            modifier = Modifier
                .fillMaxWidth()
                .clip(expressiveListItemShape(0, 2))
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(16.dp, 12.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}
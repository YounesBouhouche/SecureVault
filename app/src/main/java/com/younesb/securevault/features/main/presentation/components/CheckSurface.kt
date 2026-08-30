package com.younesb.securevault.features.main.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.younesb.securevault.core.presentation.utils.expressiveListItemShape

@Composable
fun CheckSurface(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        if (checked) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainer,
        MaterialTheme.motionScheme.fastSpatialSpec()
    )
    val contentColor by animateColorAsState(
        if (checked) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurface,
        MaterialTheme.motionScheme.fastSpatialSpec()
    )
    Surface(
        shape = expressiveListItemShape(
            index = 0,
            count = 1,
            largeShape = MaterialTheme.shapes.extraLarge,
            smallShape = MaterialTheme.shapes.medium,
            selected = checked
        ),
        color = containerColor,
        contentColor = contentColor,
        onClick = {
            onCheckedChange(!checked)
        },
        modifier = modifier
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp),
                fontWeight = if (checked) FontWeight.SemiBold else FontWeight.Normal,
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}
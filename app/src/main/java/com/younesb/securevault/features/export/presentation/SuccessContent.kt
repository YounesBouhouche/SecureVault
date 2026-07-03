package com.younesb.securevault.features.export.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.ExpressiveButton
import com.younesb.securevault.core.presentation.components.IconContainer

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SuccessContent(modifier: Modifier = Modifier, onClose: () -> Unit) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconContainer(
            modifier = Modifier.size(100.dp),
            shape = MaterialShapes.Cookie12Sided.toShape(),
            icon = Icons.Rounded.Check,
            backgroundColor = MaterialTheme.colorScheme.primary,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = stringResource(R.string.export_success),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        ExpressiveButton(
            size = ButtonDefaults.MediumContainerHeight,
            text = stringResource(R.string.ok),
            onClick = onClose,
            outlined = true
        )
    }
}
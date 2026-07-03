package com.younesb.securevault.features.main.presentation.navigation.routes.document

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.ExpressiveTextField
import com.younesb.securevault.core.presentation.components.IconContainer
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.models.DocumentType
import com.younesb.securevault.features.main.presentation.navigation.routes.document.viewers.ImageViewer
import com.younesb.securevault.features.main.presentation.navigation.routes.document.viewers.pdf.PdfViewerScreen

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FileViewer(
    document: DocumentDto,
    file: ByteArray?,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onToggleToolbar: () -> Unit,
    onShowInfoSheet: () -> Unit,
) {
    val textFieldState = rememberTextFieldState()

    LaunchedEffect(file) {
        if (document.type == DocumentType.NOTE) {
            textFieldState.setTextAndPlaceCursorAtEnd((file?.decodeToString()) ?: "")
        }
    }
    when (document.type) {
        DocumentType.IMAGE ->
            ImageViewer(
                file,
                modifier = modifier.fillMaxSize(),
                onToggleToolbar = onToggleToolbar,
                onShowInfo = onShowInfoSheet
            )

        DocumentType.NOTE ->
            ExpressiveTextField(
                state = textFieldState,
                readOnly = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(contentPadding)
                    .padding(16.dp),
                lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 5),
                label = {
                    Text(stringResource(R.string.note_content))
                }
            )

        else -> when (document.mimeType) {
            "application/pdf" -> file?.let {
                PdfViewerScreen(
                    it,
                    contentPadding = contentPadding,
                    onToggleToolbar = onToggleToolbar
                )
            }
            else -> Column(
                modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconContainer(
                    modifier = Modifier.size(200.dp),
                    icon = Icons.Rounded.QuestionMark,
                    shape = MaterialShapes.Cookie12Sided.toShape(),
                    color = MaterialTheme.colorScheme.background,
                    backgroundColor = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    stringResource(R.string.unsupported_file_type),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
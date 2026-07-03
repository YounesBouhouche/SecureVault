package com.younesb.securevault.features.main.presentation.navigation.routes.document.viewers.pdf

import android.graphics.Bitmap
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.younesb.securevault.features.main.presentation.util.ResourcePresenter
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PdfViewerScreen(
    byteArray: ByteArray,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onToggleToolbar: () -> Unit = {},
) {
    val viewModel = koinViewModel<PdfViewerViewModel> {
        parametersOf(byteArray)
    }
    val renderedPages by viewModel.renderedPages.collectAsStateWithLifecycle()

    ResourcePresenter(
        resource = renderedPages,
        modifier = modifier.fillMaxSize(),
    ) { pages ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                detectTapGestures {
                    onToggleToolbar()
                }
            },
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(pages, { _, i -> i }) { _, page ->
                PdfPage(page = page)
            }
        }
    }
}

@Composable
fun PdfPage(
    page: Bitmap,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = page,
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(page.width.toFloat() / page.height.toFloat())
    )
}
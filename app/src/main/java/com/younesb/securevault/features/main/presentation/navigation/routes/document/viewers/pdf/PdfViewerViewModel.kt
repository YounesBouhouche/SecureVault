package com.younesb.securevault.features.main.presentation.navigation.routes.document.viewers.pdf

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.features.main.presentation.navigation.routes.document.viewers.pdf.util.PdfBitmapConverter
import com.younesb.securevault.features.main.presentation.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PdfViewerViewModel(
    converter: PdfBitmapConverter,
    private val byteArray: ByteArray
) : ViewModel() {
    private val _renderedPages = MutableStateFlow<Resource<List<Bitmap>, Exception>>(Resource.Idle)
    val renderedPages = _renderedPages.asStateFlow()

    init {
        viewModelScope.launch {
            _renderedPages.value = Resource.Loading
            _renderedPages.value = Resource.Success(converter.pdfToBitmaps(byteArray))
        }
    }
}
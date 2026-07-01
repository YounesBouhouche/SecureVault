package com.younesb.securevault.features.main.presentation.navigation.routes.document.viewers.pdf.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import androidx.core.graphics.createBitmap
import java.io.File
import java.io.RandomAccessFile

class PdfBitmapConverter(
    private val context: Context
) {
    var renderer: PdfRenderer? = null

    suspend fun pdfToBitmaps(byteArray: ByteArray): List<Bitmap> {
        return withContext(Dispatchers.IO) {
            renderer?.close()
            val tempFile = File.createTempFile("pdf", ".pdf", context.cacheDir)
            try {
                tempFile.writeBytes(byteArray)
                RandomAccessFile(tempFile, "r").use { randomAccessFile ->
                    with(PdfRenderer(ParcelFileDescriptor.dup(randomAccessFile.fd))) {
                        renderer = this
                        return@withContext (0 until pageCount).map { index ->
                            async {
                                openPage(index).use { page ->
                                    val bitmap = createBitmap(page.width, page.height)
                                    Canvas(bitmap).apply {
                                        drawColor(Color.WHITE)
                                    }
                                    page.render(
                                        bitmap,
                                        null,
                                        null,
                                        PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                                    )
                                    bitmap
                                }
                            }
                        }.awaitAll()
                    }
                }
            } finally {
                tempFile.delete()
            }
        }
    }
}
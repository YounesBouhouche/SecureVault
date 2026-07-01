package com.younesb.securevault.features.export.domain.use_cases

import android.net.Uri
import com.younesb.securevault.core.domain.utils.EmptyResult
import com.younesb.securevault.core.domain.utils.Result
import com.younesb.securevault.core.domain.utils.pipe
import com.younesb.securevault.features.main.data.files_utils.Archiver
import com.younesb.securevault.features.main.domain.models.DocumentType
import com.younesb.securevault.features.main.domain.repository.FilesRepository
import com.younesb.securevault.features.main.domain.usecases.GetDocumentsUseCase
import com.younesb.securevault.features.main.presentation.util.FileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExportDocumentsUseCase(
    val getDocumentsUseCase: GetDocumentsUseCase,
    val filesRepository: FilesRepository,
    val archiver: Archiver
) {
    suspend operator fun invoke(
        documents: List<String>?,
        destination: Uri,
        encrypted: Boolean
    ): EmptyResult<Exception> =
        withContext(Dispatchers.IO) {
            getDocumentsUseCase(documents).pipe { docs ->
                Result.run {
                    archiver.zip(
                        docs.map { doc ->
                            val docName = doc.name.let { name ->
                                val extension = when (doc.type) {
                                    DocumentType.NOTE -> ".txt"
                                    else -> FileInfo.mimeTypeToExtension(doc.mimeType)
                                        ?.let { ext -> ".$ext" } ?: ""
                                }
                                if (!name.endsWith(extension)) {
                                    name + extension
                                } else name
                            }
                            filesRepository.getFileUri(doc.id) to docName
                        },
                        destination,
                        encrypted
                    )
                }
            }
        }
}
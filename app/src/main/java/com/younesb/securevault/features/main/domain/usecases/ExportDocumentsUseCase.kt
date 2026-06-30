package com.younesb.securevault.features.main.domain.usecases

import android.net.Uri
import com.younesb.securevault.core.domain.utils.EmptyResult
import com.younesb.securevault.core.domain.utils.Result
import com.younesb.securevault.core.domain.utils.pipe
import com.younesb.securevault.features.main.data.files_utils.Archiver
import com.younesb.securevault.features.main.domain.models.DocumentType
import com.younesb.securevault.features.main.domain.repository.FilesRepository
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
                            val docName = StringBuilder(doc.name).also {
                                when (doc.type) {
                                    DocumentType.NOTE -> it.append(".txt")
                                    DocumentType.IMAGE -> it.append(".jpg")
                                    else -> Unit
                                }
                            }.toString()
                            filesRepository.getFileUri(doc.id) to docName
                        },
                        destination,
                        encrypted
                    )
                }
            }
        }
}

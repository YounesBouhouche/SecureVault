package com.younesb.securevault.features.main.presentation.components.new_doc

import android.net.Uri
import com.younesb.securevault.features.main.presentation.util.NewItemType

sealed class NewDocState {
    data object Collapsed: NewDocState()
    data object Expanded: NewDocState()
    data class New(val type: NewItemType): NewDocState()
    data class Photo(val uri: Uri, val name: String): NewDocState()
    data class File(val name: String): NewDocState()
    data object Folder: NewDocState()
    data object Note: NewDocState()
}
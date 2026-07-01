package com.younesb.securevault.features.main.presentation.screens.new_item

import com.younesb.securevault.R

enum class DocNameError(val stringRes: Int) {
    EMPTY(R.string.error_empty_doc_name),
    INVALID_CHARACTERS(R.string.error_invalid_characters_doc_name),
    TOO_LONG(R.string.error_too_long_doc_name),
    ALREADY_EXISTS(R.string.already_exists_doc_name)
}
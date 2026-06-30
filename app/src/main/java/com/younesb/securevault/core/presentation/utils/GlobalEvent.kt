package com.younesb.securevault.core.presentation.utils

import com.younesb.securevault.core.domain.models.preferences.Language

sealed interface GlobalEvent {
    data class SetLanguage(val language: Language): GlobalEvent
    data class LaunchExternalLink(val link: String, val alternative: String = link): GlobalEvent
}
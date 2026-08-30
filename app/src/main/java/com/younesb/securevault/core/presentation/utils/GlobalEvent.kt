package com.younesb.securevault.core.presentation.utils

import androidx.navigation.NavOptionsBuilder
import com.younesb.securevault.core.domain.models.preferences.Language
import com.younesb.securevault.features.navigation.NavRoutes

sealed interface GlobalEvent {
    data class SetLanguage(val language: Language) : GlobalEvent
    data class Navigate(val destination: NavRoutes, val builder: NavOptionsBuilder.() -> Unit) :
        GlobalEvent

    data class LaunchExternalLink(val link: String, val alternative: String = link) : GlobalEvent
}
package com.younesb.securevault.core.presentation.utils

import com.younesb.securevault.core.domain.models.preferences.Language
import com.younesb.securevault.features.auth.presentation.navigation.AuthRoutes
import com.younesb.securevault.features.navigation.NavRoutes

sealed interface GlobalEvent {
    data class SetLanguage(val language: Language): GlobalEvent
}
package com.younesb.securevault.di

import com.younesb.securevault.features.auth.presentation.screens.OnboardingViewModel
import com.younesb.securevault.features.auth.presentation.screens.SetupPinViewModel
import com.younesb.securevault.features.auth.presentation.util.BiometricPromptManager
import com.younesb.securevault.features.auth.presentation.screens.SetupViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    singleOf(::BiometricPromptManager)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::SetupViewModel)
    viewModelOf(::SetupPinViewModel)
}
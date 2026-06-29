package com.younesb.securevault.core.presentation.utils

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.younesb.securevault.core.presentation.events.CollectEvents
import com.younesb.securevault.features.auth.presentation.util.AuthEvent
import com.younesb.securevault.features.auth.presentation.util.AuthEventsBus
import com.younesb.securevault.features.auth.presentation.util.BiometricPromptManager
import com.younesb.securevault.features.navigation.NavRoutes

@Composable
fun AppCompatActivity.CollectGlobalEvents() {
    CollectEvents(GlobalEventsBus.events) {
        when (it) {
            is GlobalEvent.SetLanguage -> setLanguage(it.language)
        }
    }
}
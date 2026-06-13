package com.younesb.securevault.features.auth.presentation.util

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
import com.younesb.securevault.features.navigation.NavRoutes

@Composable
fun AppCompatActivity.CollectAuthEvents(
    navController: NavHostController = rememberNavController(),
    biometricPromptManager: BiometricPromptManager = BiometricPromptManager()
) {
    val enrollLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {}
    )
    CollectEvents(AuthEventsBus.events) {
        when (it) {
            is AuthEvent.ShowBiometricPrompt -> {
                biometricPromptManager.showBiometricPrompt(
                    this,
                    it.title,
                    it.description,
                )
            }
            is AuthEvent.AuthNavigate -> {
                navController.navigate(it.route) {
                    launchSingleTop = true
                    popUpTo(it.route) { inclusive = true }
                }
            }
            is AuthEvent.LaunchScreenLockSettings -> {
                if(Build.VERSION.SDK_INT >= 30) {
                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                        )
                    }
                    enrollLauncher.launch(enrollIntent)
                }
            }
            is AuthEvent.Navigate -> navController.navigate(it.route)  {
                popUpTo(NavRoutes.Auth) {
                    inclusive = true
                }
            }
        }
    }
}
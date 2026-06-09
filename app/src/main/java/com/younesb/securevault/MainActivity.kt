package com.younesb.securevault

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.younesb.securevault.core.presentation.theme.AppTheme
import com.younesb.securevault.core.presentation.theme.SetSystemBarColors
import com.younesb.securevault.core.presentation.theme.ThemeViewModel
import com.younesb.securevault.features.auth.presentation.util.BiometricPromptManager
import com.younesb.securevault.features.auth.presentation.util.CollectEvents
import com.younesb.securevault.features.auth.presentation.util.Event
import com.younesb.securevault.features.navigation.NavRoutes
import com.younesb.securevault.features.navigation.NavigationHost
import org.koin.android.ext.android.inject
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : AppCompatActivity() {
    val biometricPromptManager by inject<BiometricPromptManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = koinViewModel<ThemeViewModel>()
            val themeMode by viewModel.themeMode.collectAsState()
            val extraDark by viewModel.extraDark.collectAsState()
            val colorTheme by viewModel.colorTheme.collectAsState()
            val dynamicColors by viewModel.dynamicColors.collectAsState()
            val navController = rememberNavController()
            val enrollLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
                onResult = {}
            )
            CollectEvents {
                when (it) {
                    is Event.ShowBiometricPrompt -> {
                        biometricPromptManager.showBiometricPrompt(
                            this@MainActivity,
                            it.title,
                            it.description,
                        )
                    }
                    is Event.AuthNavigate -> {
                        navController.navigate(it.route) {
                            launchSingleTop = true
                            popUpTo(it.route) { inclusive = true }
                        }
                    }
                    is Event.LaunchScreenLockSettings -> {
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
                    is Event.Navigate -> navController.navigate(it.route)  {
                        popUpTo(NavRoutes.Auth) {
                            inclusive = true
                        }
                    }
                }
            }
            SetSystemBarColors(themeMode)
            AppTheme(
                themeMode = themeMode,
                extraDark = extraDark,
                colorTheme = colorTheme,
                dynamicColors = dynamicColors
            ) {
                Surface(Modifier.fillMaxSize()) {
                    NavigationHost(navController = navController)
                }
            }
        }
    }
}

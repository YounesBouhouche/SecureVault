package com.younesb.securevault

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.younesb.securevault.core.presentation.theme.AppTheme
import com.younesb.securevault.core.presentation.theme.SetSystemBarColors
import com.younesb.securevault.core.presentation.theme.ThemeViewModel
import com.younesb.securevault.core.presentation.utils.CollectGlobalEvents
import com.younesb.securevault.features.auth.presentation.util.BiometricPromptManager
import com.younesb.securevault.features.auth.presentation.util.CollectAuthEvents
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
            CollectAuthEvents(navController, biometricPromptManager)
            CollectGlobalEvents()
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

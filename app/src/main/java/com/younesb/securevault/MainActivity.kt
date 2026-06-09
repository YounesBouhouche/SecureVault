package com.younesb.securevault

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.younesb.securevault.core.presentation.theme.AppTheme
import com.younesb.securevault.core.presentation.theme.SetSystemBarColors
import com.younesb.securevault.core.presentation.theme.ThemeViewModel
import com.younesb.securevault.features.navigation.NavigationHost
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = koinViewModel<ThemeViewModel>()
            val themeMode by viewModel.themeMode.collectAsState()
            val extraDark by viewModel.extraDark.collectAsState()
            val colorTheme by viewModel.colorTheme.collectAsState()
            val dynamicColors by viewModel.dynamicColors.collectAsState()
            SetSystemBarColors(themeMode)
            AppTheme(
                themeMode = themeMode,
                extraDark = extraDark,
                colorTheme = colorTheme,
                dynamicColors = dynamicColors
            ) {
                NavigationHost()
            }
        }
    }
}

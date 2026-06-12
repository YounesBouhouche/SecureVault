package com.younesb.securevault.features.main.presentation.navigation.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun<T, R> NavHostController.getCurrentRoute(
    entries: List<T>,
    callback: (T) -> R,
): T?
    = currentBackStackEntryAsState().value?.destination?.route?.let { route ->
        entries
            .firstOrNull {
                callback(it)?.javaClass?.kotlin?.qualifiedName?.contains(route) == true
            }
    }
package com.younesb.securevault.core.presentation.utils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.navigation.NavOptionsBuilder
import com.younesb.securevault.core.presentation.events.CollectEvents
import com.younesb.securevault.features.navigation.NavRoutes

@Composable
fun AppCompatActivity.CollectGlobalEvents(
    onNavigate: (route: NavRoutes, builder: NavOptionsBuilder.() -> Unit) -> Unit
) {
    CollectEvents(GlobalEventsBus.events) { event ->
        when (event) {
            is GlobalEvent.SetLanguage -> setLanguage(event.language)
            is GlobalEvent.LaunchExternalLink -> {
                with(
                    Intent(
                        Intent.ACTION_VIEW,
                        event.link.toUri(),
                    ),
                ) {
                    if (resolveActivity(packageManager) != null) {
                        startActivity(this)
                    } else {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                event.alternative.toUri(),
                            ),
                        )
                    }
                }
            }

            is GlobalEvent.Navigate -> {
                onNavigate(event.destination, event.builder)
            }
        }
    }
}
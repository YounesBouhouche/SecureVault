package com.younesb.securevault.features.auth.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.younesb.securevault.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun OnboardingContent(modifier: Modifier = Modifier) {
    var slide by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000.milliseconds)
            slide = (slide + 1) % 3
        }
    }
    AnimatedContent(
        targetState = slide,
        modifier = modifier.fillMaxWidth(),
        transitionSpec = {
            if (targetState > initialState) {
                slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
            } else {
                slideInHorizontally { -it } + fadeIn() togetherWith
                        slideOutHorizontally { it } + fadeOut()
            }
        }
    ) {
        when(it) {
            0 -> SetupExplanationElement(
                vector = R.drawable.mobile_lock,
                title = stringResource(R.string.biometric_authentication),
                text = stringResource(R.string.biometric_authentication_description)
            )
            1 -> SetupExplanationElement(
                vector = R.drawable.mobile_lock,
                title = stringResource(R.string.fallback_authentication),
                text = stringResource(R.string.fallback_authentication_description)
            )
            2 -> SetupExplanationElement(
                vector = R.drawable.av_timer,
                title = stringResource(R.string.auto_lock_and_timeout),
                text = stringResource(R.string.auto_lock_and_timeout_description)
            )
        }
    }
}
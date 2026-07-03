package com.younesb.securevault.features.auth.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.younesb.securevault.R
import com.younesb.securevault.core.data.util.AuthManager
import com.younesb.securevault.core.domain.models.preferences.Theme
import com.younesb.securevault.core.presentation.components.ExpressiveButton
import com.younesb.securevault.core.presentation.components.AnimatedAppLogo
import com.younesb.securevault.features.auth.presentation.components.AnimatedCounter
import com.younesb.securevault.features.auth.presentation.components.CounterAnimationDirection
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OnboardingScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    onSetup: () -> Unit,
) {
    val onboardingViewModel = koinViewModel<OnboardingViewModel>()
    val authState by onboardingViewModel.authState.collectAsState()
    val theme by onboardingViewModel.theme.collectAsState()
    val remainingTime by onboardingViewModel.remainingTime.collectAsState()

    val containerColor by animateColorAsState(
        if (authState is AuthManager.AuthState.AttemptsExceeded)
            MaterialTheme.colorScheme.errorContainer
        else
            MaterialTheme.colorScheme.surfaceContainer
    )
    Column(
        modifier = modifier
            .systemBarsPadding()
            .padding(28.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedAppLogo()
        with(sharedTransitionScope) {
            Surface(
                color = containerColor,
                shape = MaterialTheme.shapes.extraExtraLarge,
                modifier = Modifier.sharedBounds(
                    sharedContentState = sharedTransitionScope
                        .rememberSharedContentState(key = "onboarding-setup"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedVisibility(authState !is AuthManager.AuthState.AttemptsExceeded) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.welcome_to),
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center,
                                softWrap = false,
                                autoSize = TextAutoSize.StepBased(
                                    minFontSize = 10.sp,
                                    maxFontSize = 100.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = stringResource(R.string.app_name),
                                style = MaterialTheme.typography.headlineLarge,
                                textAlign = TextAlign.Center,
                                softWrap = false,
                                autoSize = TextAutoSize.StepBased(
                                    minFontSize = 10.sp,
                                    maxFontSize = 100.sp
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    AnimatedContent(authState) { state ->
                        when(state) {
                            is AuthManager.AuthState.AttemptsExceeded -> {
                                Column(
                                    Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Rounded.Warning,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(64.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.too_many_failed_attempts),
                                        style = MaterialTheme.typography.titleMedium,
                                        textAlign = TextAlign.Center,
                                        softWrap = false,
                                        autoSize = TextAutoSize.StepBased(
                                            minFontSize = 10.sp,
                                            maxFontSize = 40.sp
                                        ),
                                        modifier = Modifier.fillMaxWidth(),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Text(
                                        text = stringResource(R.string.try_again_after),
                                        style = MaterialTheme.typography.bodyMedium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth(),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    ProvideTextStyle(
                                        MaterialTheme.typography.headlineMedium.copy(
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    ) {
                                        AnimatedCounter(
                                            counter = "$remainingTime",
                                            digits = 2,
                                            modifier = Modifier.fillMaxWidth(),
                                            direction = CounterAnimationDirection.Down
                                        )
                                    }
                                    ExpressiveButton(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = stringResource(R.string.reset_app),
                                        size = ButtonDefaults.MediumContainerHeight,
                                        icon = Icons.Default.LockReset,
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = MaterialTheme.colorScheme.onErrorContainer,
                                            contentColor = MaterialTheme.colorScheme.onError
                                        )
                                    ) {
                                    }
                                }
                            }
                            AuthManager.AuthState.NoCredentials -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    AnimatedContent(theme) {
                                        ExpressiveButton(
                                            text = null,
                                            size = ButtonDefaults.MediumContainerHeight,
                                            icon = when(it) {
                                                Theme.SYSTEM -> Icons.Default.BrightnessAuto
                                                Theme.LIGHT -> Icons.Default.LightMode
                                                Theme.DARK -> Icons.Default.DarkMode
                                            },
                                            colors = ButtonDefaults.filledTonalButtonColors()
                                        ) {
                                            onboardingViewModel.setTheme(
                                                when(it) {
                                                    Theme.SYSTEM -> Theme.LIGHT
                                                    Theme.LIGHT -> Theme.DARK
                                                    Theme.DARK -> Theme.SYSTEM
                                                }
                                            )
                                        }
                                    }
                                    ExpressiveButton(
                                        modifier = Modifier.weight(1f),
                                        text = {
                                            Text(
                                                text = stringResource(R.string.get_started),
                                                softWrap = false,
                                                autoSize = TextAutoSize.StepBased(
                                                    minFontSize = 10.sp,
                                                    maxFontSize = 40.sp
                                                ),
                                            )
                                        },
                                        size = ButtonDefaults.MediumContainerHeight,
                                        icon = Icons.AutoMirrored.Filled.ArrowForward,
                                        onClick = onSetup
                                    )
                                }
                            }
                            null,
                            AuthManager.AuthState.Authenticated -> {
                                LinearProgressIndicator(Modifier.fillMaxWidth())
                            }
                            else -> {
                                ExpressiveButton(
                                    modifier = Modifier.weight(1f),
                                    text = stringResource(R.string.authenticate),
                                    size = ButtonDefaults.MediumContainerHeight,
                                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                                ) {
                                    onboardingViewModel.authenticateWithBiometrics()
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}

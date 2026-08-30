package com.younesb.securevault.features.auth.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.younesb.securevault.R
import com.younesb.securevault.core.data.util.AuthManager
import com.younesb.securevault.features.auth.presentation.components.AnimatedCounter
import com.younesb.securevault.features.auth.presentation.components.CounterAnimationDirection
import com.younesb.securevault.features.auth.presentation.components.DragValue
import com.younesb.securevault.features.auth.presentation.components.DraggableButton
import com.younesb.securevault.features.auth.presentation.components.OnboardingContent
import com.younesb.securevault.features.auth.presentation.components.SetupScreenWrapper
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
context(transitionScope: SharedTransitionScope, animatedVisibilityScope: AnimatedContentScope)
fun OnboardingScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<OnboardingViewModel>()
    val authState by viewModel.authState.collectAsState()
    val loading by remember(authState) {
        derivedStateOf {
            (authState == null) or (authState is AuthManager.AuthState.Authenticated)
        }
    }
    val remainingTime by viewModel.remainingTime.collectAsState()
    val containerColor by animateColorAsState(
        if (authState is AuthManager.AuthState.AttemptsExceeded)
            MaterialTheme.colorScheme.errorContainer
        else
            MaterialTheme.colorScheme.surfaceContainer
    )
    val draggableButtonColor by animateColorAsState(
        if (authState is AuthManager.AuthState.AttemptsExceeded)
            MaterialTheme.colorScheme.error
        else
            MaterialTheme.colorScheme.primary
    )
    val draggableButtonContainerColor by animateColorAsState(
        if (authState is AuthManager.AuthState.AttemptsExceeded)
            MaterialTheme.colorScheme.error.copy(alpha = .2f).compositeOver(MaterialTheme.colorScheme.errorContainer)
        else
            MaterialTheme.colorScheme.surface
    )

    LaunchedEffect(Unit) {
        viewModel.draggableState.snapTo(DragValue.Start)
    }
    SetupScreenWrapper(
        icon = ImageVector.vectorResource(R.drawable.app_logo),
        title = stringResource(R.string.app_name).uppercase(),
        surfaceColor = containerColor,
        surfaceVisible = !loading,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            AnimatedContent(
                targetState = authState,
                contentKey = {
                    if (it is AuthManager.AuthState.AttemptsExceeded) "attempts_exceeded"
                    else "onboarding"
                             },
            ) { state ->
                when (state) {
                    is AuthManager.AuthState.AttemptsExceeded -> {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 32.dp),
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
                        }
                    }
                    null,
                    AuthManager.AuthState.RequiresBiometric,
                    is AuthManager.AuthState.RequiresPin -> {}
                    else -> {
                        OnboardingContent(Modifier.padding(bottom = 32.dp))
                    }
                }
            }
            DraggableButton(
                modifier = Modifier.fillMaxWidth(),
                state = viewModel.draggableState,
                shape = RectangleShape,
                text = stringResource(
                    when (authState) {
                        is AuthManager.AuthState.NoCredentials -> R.string.get_started
                        is AuthManager.AuthState.AttemptsExceeded -> R.string.reset_app
                        else -> R.string.slide_to_unlock
                    }
                ),
                releaseText = stringResource(R.string.release_to_start),
                color = draggableButtonColor,
                containerColor = draggableButtonContainerColor
            ) {
                viewModel.setup()
            }
        }
    }
}

package com.younesb.securevault.features.auth.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.ExpressiveButton
import com.younesb.securevault.core.presentation.components.IconContainer
import com.younesb.securevault.features.auth.presentation.components.PinTextRow
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalGridApi::class)
@Composable
fun AuthPinScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    val numberColors = ButtonDefaults.filledTonalButtonColors()
    val backSpaceColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.error
    )
    val clearColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
    val authPinViewModel = koinViewModel<AuthPinViewModel>()
    val pin by authPinViewModel.pin.collectAsState()
    val wrongPin by authPinViewModel.wrongPin.collectAsState()
    val remainingAttempts by authPinViewModel.remainingAttempts.collectAsState()
    val loading by authPinViewModel.loading.collectAsState()
    val textRes by remember {
        derivedStateOf {
            if (wrongPin) R.string.wrong_pin_try_again
            else R.string.enter_your_pin
        }
    }
    Column(
        modifier = modifier
            .systemBarsPadding()
            .padding(28.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconContainer(
            icon = Icons.Default.Password,
            shape = MaterialShapes.Cookie4Sided.toShape(),
            fraction = .5f,
            modifier = Modifier.size(200.dp),
            color = MaterialTheme.colorScheme.tertiary,
            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer
        )
        PinTextRow(
            pin = pin,
            modifier = Modifier.fillMaxWidth(),
            loadingAnimation = loading
        )
        with(sharedTransitionScope) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.extraExtraLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .sharedBounds(
                        sharedContentState = sharedTransitionScope
                            .rememberSharedContentState(key = "onboarding-setup"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                    )
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    AnimatedContent(textRes) {
                        Text(
                            text = stringResource(it),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            softWrap = false,
                            autoSize = TextAutoSize.StepBased(
                                minFontSize = 10.sp,
                                maxFontSize = 100.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = pluralStringResource(
                            R.plurals.remaining_attempts,
                            remainingAttempts,
                            remainingAttempts
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Grid(
                        config = {
                            repeat(3) {
                                column(1.fr)
                            }
                            repeat(4) {
                                row(1.fr)
                            }
                            gap(8.dp)
                        }
                    ) {
                        repeat(12) { index ->
                            ExpressiveButton(
                                onClick = {
                                    when (index) {
                                        in 0..8 -> authPinViewModel.addDigit((index + 1) % 10)
                                        10 -> authPinViewModel.addDigit(0)
                                        9 -> authPinViewModel.clearPin()
                                        11 -> authPinViewModel.removeLastDigit()
                                    }
                                },
                                enabled = !loading,
                                modifier = Modifier.fillMaxSize(),
                                size = ButtonDefaults.MediumContainerHeight,
                                colors = when(index) {
                                    9 -> clearColors
                                    11 -> backSpaceColors
                                    else -> numberColors
                                },
                                text = {
                                    when (index) {
                                        9 -> Icon(
                                            Icons.Default.Clear,
                                            null
                                        )
                                        11 -> Icon(
                                            Icons.AutoMirrored.Default.Backspace,
                                            null
                                        )
                                        else -> Text(
                                            text = when (index) {
                                                in 0..8 -> "${index + 1}"
                                                else -> "0"
                                            },
                                            style = MaterialTheme.typography.titleMedium,
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

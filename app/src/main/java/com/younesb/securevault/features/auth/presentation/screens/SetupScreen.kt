package com.younesb.securevault.features.auth.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.ExpressiveButton
import com.younesb.securevault.core.presentation.components.IconContainer
import com.younesb.securevault.features.auth.presentation.components.SetupExplanationElement
import com.younesb.securevault.features.auth.presentation.util.BiometricPromptManager
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<SetupViewModel>()
    val promptResult by viewModel.promptResult.collectAsState()
    var showSkipDialog by rememberSaveable {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .systemBarsPadding()
            .padding(28.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconContainer(
            icon = Icons.Default.Fingerprint,
            shape = MaterialShapes.Cookie4Sided.toShape(),
            fraction = .5f,
            modifier = Modifier.size(200.dp),
            color = MaterialTheme.colorScheme.tertiary,
            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer
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
                ) {
                    Text(
                        text = stringResource(R.string.let_s_set_up_your_vault),
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        softWrap = false,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 10.sp,
                            maxFontSize = 100.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    AnimatedContent(promptResult, Modifier.weight(1f).fillMaxSize()) {
                        when (it) {
                            BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                                Box(
                                    modifier = Modifier.fillMaxSize().weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    IconContainer(
                                        icon = Icons.Default.Check,
                                        shape = MaterialShapes.Cookie12Sided.toShape(),
                                        fraction = .5f,
                                        modifier = Modifier.size(100.dp),
                                        color = MaterialTheme.colorScheme.primary,
                                        backgroundColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                }
                            }
                            BiometricPromptManager.BiometricResult.AuthenticationNotSet,
                            null -> {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    SetupExplanationElement(
                                        title = "Biometric authentication",
                                        text = "Use your fingerprint or face to quickly and securely access your vault."
                                    )
                                    SetupExplanationElement(
                                        title = "Fallback authentication",
                                        text = "Set up a PIN as a backup authentication method."
                                    )
                                    SetupExplanationElement(
                                        title = "Auto-lock and timeout",
                                        text = "Configure auto-lock settings to protect your vault when you're away."
                                    )
                                }
                            }

                            else -> {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Rounded.Warning,
                                        null,
                                        Modifier.size(100.dp),
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                    Text(
                                        text = "Biometric authentication is not set up or failed to authenticate. Please set it up to enhance the security of your vault.",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (promptResult != BiometricPromptManager.BiometricResult.AuthenticationSuccess)
                            ExpressiveButton(
                                modifier = Modifier.weight(1f),
                                text = "Skip",
                                size = ButtonDefaults.MediumContainerHeight,
                                outlined = true
                            ) {
                                showSkipDialog = true
                            }
                        ExpressiveButton(
                            modifier = Modifier.weight(1f),
                            text =
                                if (promptResult == BiometricPromptManager.BiometricResult.AuthenticationSuccess)
                                    "Already set!"
                                else "Opt in"
                            ,
                            size = ButtonDefaults.MediumContainerHeight,
                        ) {
                            viewModel.showBiometricPrompt()
                        }
                    }
                }
            }
        }
    }
    if (showSkipDialog) {
        BasicAlertDialog(
            onDismissRequest = {
                showSkipDialog = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(.9f),
                shape = MaterialTheme.shapes.extraExtraLarge,
                color = MaterialTheme.colorScheme.surfaceContainer
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp, 24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Rounded.Warning,
                        null,
                        Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Are you sure?",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Using biometric authentication makes your vault more secure, you can use PIN only (if you insist)",
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ExpressiveButton(
                            text = "No",
                            size = ButtonDefaults.MediumContainerHeight,
                            outlined = true,
                            modifier = Modifier.weight(1f)
                        ) {
                            showSkipDialog = false
                        }
                        ExpressiveButton(
                            text = "Use PIN Only",
                            size = ButtonDefaults.MediumContainerHeight,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            showSkipDialog = false
                            viewModel.skip()
                        }
                    }
                }
            }
        }
    }
}

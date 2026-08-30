package com.younesb.securevault.features.auth.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.PinGrid
import com.younesb.securevault.features.auth.presentation.components.PinTextRow
import com.younesb.securevault.features.auth.presentation.components.SetupScreenWrapper
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalGridApi::class)
@Composable
context(transitionScope: SharedTransitionScope, animatedVisibilityScope: AnimatedContentScope)
fun SetupPinScreen(modifier: Modifier = Modifier) {
    val setupPinViewModel = koinViewModel<SetupPinViewModel>()
    val pin by setupPinViewModel.pin.collectAsState()
    val repeatPin by setupPinViewModel.repeatPin.collectAsState()
    val repeatPinMismatch by setupPinViewModel.repeatPinMismatch.collectAsState()
    val loading by setupPinViewModel.loading.collectAsState()
    val textRes by remember {
        derivedStateOf {
            when {
                repeatPin == null -> R.string.add_a_pin_code
                repeatPinMismatch -> R.string.pins_do_not_match
                else -> R.string.repeat_your_pin
            }
        }
    }
    SetupScreenWrapper(
        modifier = modifier.fillMaxSize(),
        icon = Icons.Default.Password,
        title = stringResource(textRes)
    ) {
        Column(
            modifier = Modifier.padding(24.dp, 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            AnimatedContent(
                repeatPin == null,
                transitionSpec = {
                    if (targetState) {
                        slideInVertically { -it } togetherWith slideOutVertically { it }
                    } else {
                        slideInVertically { it } togetherWith slideOutVertically { -it }
                    }
                }
            ) {
                if (it) {
                    PinTextRow(
                        pin = pin,
                        loadingAnimation = loading,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    PinTextRow(
                        pin = repeatPin ?: "",
                        loadingAnimation = loading,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            PinGrid(
                addDigit = { setupPinViewModel.addDigit(it) },
                removeLastDigit = { setupPinViewModel.removeLastDigit() },
                clearPin = { setupPinViewModel.clearPin() },
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                numbersContainerColor = MaterialTheme.colorScheme.surface,
                enabled = !loading
            )
        }
    }
}

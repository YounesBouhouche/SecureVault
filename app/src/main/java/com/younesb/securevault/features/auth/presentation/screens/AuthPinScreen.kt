package com.younesb.securevault.features.auth.presentation.screens

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.res.pluralStringResource
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
fun AuthPinScreen(modifier: Modifier = Modifier) {
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
    SetupScreenWrapper(
        modifier = modifier.fillMaxSize(),
        icon = Icons.Default.Password,
        title = stringResource(textRes),
        supportingText = pluralStringResource(
            R.plurals.remaining_attempts,
            remainingAttempts,
            remainingAttempts
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp).navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PinTextRow(
                pin = pin,
                modifier = Modifier.fillMaxWidth(),
                loadingAnimation = loading
            )
            PinGrid(
                addDigit = { authPinViewModel.addDigit(it) },
                removeLastDigit = { authPinViewModel.removeLastDigit() },
                clearPin = { authPinViewModel.clearPin() },
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                numbersContainerColor = MaterialTheme.colorScheme.surface,
                enabled = !loading
            )
        }
    }
}

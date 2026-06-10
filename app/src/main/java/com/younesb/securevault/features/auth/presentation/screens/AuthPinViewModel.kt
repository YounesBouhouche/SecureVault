package com.younesb.securevault.features.auth.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.core.data.util.AuthManager
import com.younesb.securevault.core.domain.repositories.AuthRepository
import com.younesb.securevault.features.auth.presentation.navigation.AuthRoutes
import com.younesb.securevault.features.auth.presentation.util.Event
import com.younesb.securevault.features.auth.presentation.util.EventBus
import com.younesb.securevault.features.navigation.NavRoutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

class AuthPinViewModel(
    val authRepository: AuthRepository
) : ViewModel() {
    private val _pin = MutableStateFlow("")
    val pin = _pin.asStateFlow()
    private val _wrongPin = MutableStateFlow(false)
    val wrongPin = _wrongPin.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()
    private val _authState = authRepository.observeAuthState()
    val remainingAttempts = _authState.map {
        (it as? AuthManager.AuthState.RequiresPin)?.remainingAttempts ?: 0
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        0
    )

    fun addDigit(digit: Int) {
        _wrongPin.value = false
        _pin.updateAndGet { it + digit }.let {
            if (it.length == 6) {
                viewModelScope.launch {
                    _loading.value = true
                    if (authRepository.authenticate(it)) {
                        EventBus.sendEvent(Event.Navigate(NavRoutes.Main))
                    } else if (_authState.value is AuthManager.AuthState.AttemptsExceeded) {
                        EventBus.sendEvent(Event.Navigate(NavRoutes.Auth))
                    } else {
                        _wrongPin.value = true
                        _pin.value = ""
                    }
                    _loading.value = false
                }
            }
        }
    }

    fun removeLastDigit() {
        if (pin.value.isEmpty()) return
        _pin.value = pin.value.dropLast(1)
    }

    fun clearPin() {
        _pin.value = ""
        _wrongPin.value = false
    }
}
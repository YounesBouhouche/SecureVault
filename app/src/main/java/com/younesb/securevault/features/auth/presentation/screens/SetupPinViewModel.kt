package com.younesb.securevault.features.auth.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.core.domain.repositories.AuthRepository
import com.younesb.securevault.features.auth.presentation.navigation.AuthRoutes
import com.younesb.securevault.features.auth.presentation.util.AuthEvent
import com.younesb.securevault.core.presentation.events.EventBus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

class SetupPinViewModel(
    val authRepository: AuthRepository
) : ViewModel() {
    private val _pin = MutableStateFlow("")
    private val _repeatPin = MutableStateFlow<String?>(null)
    val pin = _pin.asStateFlow()
    val repeatPin = _repeatPin.asStateFlow()

    private val _repeatPinMismatch = MutableStateFlow(false)
    val repeatPinMismatch = _repeatPinMismatch.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun addDigit(digit: Int) {
        _repeatPinMismatch.value = false
        if (_repeatPin.value != null) {
            _repeatPin.updateAndGet { (it ?: "") + digit }?.let {
                if (it.length == 6) {
                    viewModelScope.launch {
                        _loading.value = true
                        if (it == _pin.value) {
                            authRepository.updateSetupCredentialsPin(it)
                            authRepository.saveCredentials()
                            EventBus.sendEvent(
                                AuthEvent.AuthNavigate(AuthRoutes.FinishSetup)
                            )
                        } else {
                            _repeatPinMismatch.value = true
                            _repeatPin.value = ""
                        }
                        _loading.value = false
                    }
                }
            }
            return
        }
        if (_pin.updateAndGet { it + digit }.length == 6) {
            _repeatPin.value = ""
        }
    }

    fun removeLastDigit() {
        _repeatPin.value?.let {
            if (it.isEmpty()) return
            _repeatPin.value = it.dropLast(1)
            return
        }
        if (pin.value.isEmpty()) return
        _pin.value = pin.value.dropLast(1)
    }

    fun clearPin() {
        _pin.value = ""
        _repeatPin.value = null
    }
}
package com.younesb.securevault.features.auth.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.features.auth.presentation.navigation.AuthRoutes
import com.younesb.securevault.features.auth.presentation.util.BiometricPromptManager
import com.younesb.securevault.features.auth.presentation.util.Event
import com.younesb.securevault.features.auth.presentation.util.EventBus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

class SetupPinViewModel : ViewModel() {
    private val _pin = MutableStateFlow("")
    private val _repeatPin = MutableStateFlow<String?>(null)
    val pin = _pin.asStateFlow()
    val repeatPin = _repeatPin.asStateFlow()

    private val _repeatPinMismatch = MutableStateFlow(false)
    val repeatPinMismatch = _repeatPinMismatch.asStateFlow()

    fun addDigit(digit: Int) {
        _repeatPinMismatch.value = false
        if (_repeatPin.value != null) {
            _repeatPin.updateAndGet { (it ?: "") + digit }?.let {
                if (it.length == 6) {
                    if (it == _pin.value)
                        viewModelScope.launch {
                            EventBus.sendEvent(
                                Event.Navigate(AuthRoutes.FinishSetup)
                            )
                        }
                    else {
                        _repeatPinMismatch.value = true
                        _repeatPin.value = ""
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
package com.younesb.securevault.features.auth.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.core.domain.repositories.AuthRepository
import com.younesb.securevault.features.auth.presentation.navigation.AuthRoutes
import com.younesb.securevault.features.auth.presentation.util.Event
import com.younesb.securevault.features.auth.presentation.util.EventBus
import com.younesb.securevault.features.navigation.NavRoutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

class AuthPinViewModel(
    val authRepository: AuthRepository
) : ViewModel() {
    private val _pin = MutableStateFlow("")
    val pin = _pin.asStateFlow()
    private val _wrongPin = MutableStateFlow<Boolean>(false)
    val wrongPin = _wrongPin.asStateFlow()

    fun addDigit(digit: Int) {
        _wrongPin.value = false
        _pin.updateAndGet { it + digit }.let {
            if (it.length == 6) {
                viewModelScope.launch {
                    if (authRepository.authenticate(it)) {
                        EventBus.sendEvent(Event.Navigate(NavRoutes.Main))
                    } else {
                        _wrongPin.value = true
                        _pin.value = ""
                    }
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
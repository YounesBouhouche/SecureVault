package com.younesb.securevault.features.main.presentation.util

import com.younesb.securevault.core.domain.utils.Result

sealed interface Resource<out D, out E: Throwable> {
    data object Idle: Resource<Nothing, Nothing>
    data object Loading: Resource<Nothing, Nothing>
    data class Success<out D>(val data: D): Resource<D, Nothing>
    data class Error<out E : Throwable>(val error: E): Resource<Nothing, E>
}

fun <T, R, E: Throwable> Resource<T, E>.map(transform: (T) -> R) =
    when (this) {
        is Resource.Idle -> Resource.Idle
        is Resource.Loading -> Resource.Loading
        is Resource.Success -> Resource.Success(transform(data))
        is Resource.Error -> Resource.Error(error)
    }

fun <T, E: Throwable> Resource<T, E>.getOrNull(): T? =
    when (this) {
        is Resource.Idle -> null
        is Resource.Loading -> null
        is Resource.Success -> data
        is Resource.Error -> null
    }

fun <T, E: Exception> Result<T, E>.toResource(): Resource<T, E> =
    when (this) {
        is Result.Success -> Resource.Success(data)
        is Result.Error -> Resource.Error(error)
    }


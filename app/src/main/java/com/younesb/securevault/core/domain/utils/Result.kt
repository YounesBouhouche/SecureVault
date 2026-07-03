package com.younesb.securevault.core.domain.utils


sealed interface Result<out D, out E: Exception> {
    data class Success<out D>(val data: D): Result<D, Nothing>
    data class Error<out E: Exception>(val error: E): Result<Nothing, E>

    companion object {
        suspend fun <T> run(block: suspend () -> T): Result<T, Exception> {
            return try {
                Success(block())
            } catch (e: Exception) {
                Error(e)
            }
        }
    }
}

inline fun <I, O, E: Exception> Result<I, E>.pipe(pipe: (I) -> Result<O, E>): Result<O, E> {
    return when (this) {
        is Result.Error -> this
        is Result.Success -> pipe(data)
    }
}

inline fun <T, E: Exception, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when(this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

fun <T, E: Exception> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map {  }
}

inline fun <T, E: Exception> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}
inline fun <T, E: Exception> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Error -> {
            action(error)
            this
        }
        is Result.Success -> this
    }
}

typealias EmptyResult<E> = Result<Unit, E>
package com.antoniomy.domain.repository.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

sealed class RemoteStatus<out T> {
    data class Success<out T>(val value: T) : RemoteStatus<T>()
    data class Error(
        val isNetworkError: Boolean?,
        val errorCode: Int?,
        val errorBody: String?
    ) : RemoteStatus<Nothing>()
}

suspend fun <T : Any> safeApiCall(
    apiCall: suspend () -> T,
) : RemoteStatus<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response = apiCall.invoke()
            RemoteStatus.Success(response)
        } catch (throwable: Throwable) {
            when(throwable){
                is HttpException -> {
                    RemoteStatus.Error(false, throwable.code(), throwable.message)
                }
                else -> {
                    RemoteStatus.Error(true, null, throwable.message)
                }
            }
        }
    }
}
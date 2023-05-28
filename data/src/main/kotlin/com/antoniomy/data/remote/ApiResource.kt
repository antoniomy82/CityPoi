package com.antoniomy.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

sealed class ApiResource<out T> {
    data class Success<out T>(val value: T) : ApiResource<T>()
    data class Error(
        val isNetworkError: Boolean?,
        val errorCode: Int?,
        val errorBody: String?
    ) : ApiResource<Nothing>()
}

suspend fun <T : Any> safeApiCall(
    apiCall: suspend () -> T,
) : ApiResource<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response = apiCall.invoke()
            ApiResource.Success(response)
        } catch (throwable: Throwable) {
            when(throwable){
                is HttpException -> {
                    ApiResource.Error(false, throwable.code(), throwable.message)
                }
                else -> {
                    ApiResource.Error(true, null, throwable.message)
                }
            }
        }
    }
}
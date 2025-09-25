package com.sudesh.data.repository

import com.sudesh.core.AppError
import com.sudesh.core.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

abstract class BaseRepository {

    protected fun <T> safeApiCall(apiCall: suspend () -> T): Flow<Resource<T>> = flow {
        emit(Resource.Loading)
        val result = apiCall()
        emit(Resource.Success(result))
    }.catch { e ->
        emit(Resource.Error(AppError.Unknown(e.message ?: "Unknown error")))
    }
}
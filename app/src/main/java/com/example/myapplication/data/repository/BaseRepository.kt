package com.example.myapplication.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseRepository {


    protected fun <T> doRequest(
        request: suspend () -> T
    ) = flow {
        request().also { data ->
            emit(Result.success(data))
        }
    }.flowOn(Dispatchers.IO).catch { exception ->
        emit(Result.failure(Exception(exception.localizedMessage)))
    }
}
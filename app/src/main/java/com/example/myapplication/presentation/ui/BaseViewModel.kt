package com.example.myapplication.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.Result

abstract class BaseViewModel : ViewModel() {

    /**
     * Creates [MutableStateFlow] with [UIState] and the given initial state [UIState.Idle]
     */
    @Suppress("FunctionName")
    protected fun <T> MutableUIStateFlow() = MutableStateFlow<UIState<T>>(UIState.Idle())


    protected fun <T> Flow<Result<T>>.collectRequest(
        state: MutableStateFlow<UIState<T>>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            state.value = UIState.Loading()
            this@collectRequest.collect {
                if (it.isFailure) state.value =
                    UIState.Error(it.exceptionOrNull()?.message ?: "Unknown error")
                if (it.isSuccess) state.value = UIState.Success((it.getOrNull()!!))
            }
        }
    }

    /**
     * Collect network request and return [UIState] depending request result
     */
    protected fun <T, S : Any> Flow<Result<T>>.collectRequest(
        state: MutableStateFlow<UIState<S>>,
        data: MutableStateFlow<S>,
        mappedData: (T) -> S
    ): S? {
        viewModelScope.launch(Dispatchers.IO) {
            state.value = UIState.Loading()
            this@collectRequest.collect {
                if (it.isFailure) state.value =
                    UIState.Error(it.exceptionOrNull()?.message ?: "Unknown error")
                if (it.isSuccess) {
                    state.value = UIState.Success(mappedData(it.getOrNull()!!))
                    data.value = (mappedData(it.getOrNull()!!))
                }
            }
        }
        return null
    }

}
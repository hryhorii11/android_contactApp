package com.example.myapplication.presentation.ui.auth.fragments.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.User
import com.example.myapplication.data.model.UserRegisterResponse
import com.example.myapplication.data.retrofit.UserRemoteData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val userRemoteData: UserRemoteData) :
    ViewModel() {
    private val registerResponse = MutableLiveData<UserRegisterResponse>()
    val _registerResponse:LiveData<UserRegisterResponse> = registerResponse
    private val errorMessage = MutableLiveData<String>()
    val _errorMessage:LiveData<String> = errorMessage
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun register(requestBody: User) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = userRemoteData.register(requestBody)
            withContext(Dispatchers.Main) {
                registerResponse.value = response
            }
        }
    }

    private fun onError(message: String) {
        viewModelScope.launch(Dispatchers.Main) { // Switch to main thread for LiveData update
            errorMessage.value = message
        }
    }
}
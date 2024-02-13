package com.example.myapplication.presentation.ui.auth.fragments.login


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.LoginData
import com.example.myapplication.data.model.UserRegisterResponse
import com.example.myapplication.data.retrofit.UserRemoteData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel

class LoginViewModel @Inject constructor(private val userRemoteData: UserRemoteData) : ViewModel() {
    private val loginResponse = MutableLiveData<UserRegisterResponse>()
    val _loginResponse: LiveData<UserRegisterResponse> = loginResponse
    private val errorMessage = MutableLiveData<String>()
    val _errorMessage: LiveData<String> = errorMessage
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun login(loginData: LoginData) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = userRemoteData.login(loginData)
            withContext(Dispatchers.Main) {
                loginResponse.value = response
            }

        }
    }

    private fun onError(message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            errorMessage.value = message
        }
    }
}
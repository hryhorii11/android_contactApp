package com.example.myapplication.presentation.ui.auth.fragments.signUpExtended

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.EditBody
import com.example.myapplication.data.model.EditResponse
import com.example.myapplication.data.retrofit.UserRemoteData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpExtendedViewModel @Inject constructor(private val userRemoteData: UserRemoteData) :
    ViewModel() {
    private val editResponse = MutableLiveData<EditResponse>()
    val _registerResponse: LiveData<EditResponse> = editResponse
    private val errorMessage = MutableLiveData<String>()
    val _errorMessage: LiveData<String> = errorMessage
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private fun onError(message: String) {
        viewModelScope.launch(Dispatchers.Main) { // Switch to main thread for LiveData update
            errorMessage.value = message
        }
    }

    fun editUser(requestBody: EditBody, token: String, id: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = userRemoteData.editUser("Bearer $token", id, requestBody)
            withContext(Dispatchers.Main)
            {
                editResponse.value = response
            }
        }

    }
}
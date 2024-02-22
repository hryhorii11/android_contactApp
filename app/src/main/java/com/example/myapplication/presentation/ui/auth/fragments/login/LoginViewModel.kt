package com.example.myapplication.presentation.ui.auth.fragments.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.datastore.DataStorePreferences
import com.example.myapplication.data.users.repository.UsersRepository
import com.example.myapplication.domain.model.LoginData
import com.example.myapplication.domain.model.RefreshTokenResponse
import com.example.myapplication.domain.model.RegisterData
import com.example.myapplication.domain.model.Response
import com.example.myapplication.presentation.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class LoginViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val dataStore: DataStorePreferences
) :
    BaseViewModel() {

    private val _user = MutableUIStateFlow<Response<RegisterData>>()
    val user = _user

    private val _tokens = MutableUIStateFlow<Response<RefreshTokenResponse>>()
    val tokens = _tokens

    private val _loginData = MutableLiveData<LoginData>()
    val loginData = _loginData

    private val _isAuthorized=MutableLiveData<Boolean>()
     val isAuthorized=_isAuthorized



    fun refreshToken( ) {
        usersRepository.refreshToken().collectRequest(_tokens)
    }

    fun login(loginData: LoginData) {
        usersRepository.login(loginData).collectRequest(_user)
    }

    fun getLoginData() {
        viewModelScope.launch {
            loginData.value = dataStore.getLoginData()
        }
    }

    fun rememberLoginData(loginData: LoginData, checked: Boolean) {
        if (checked) {
            viewModelScope.launch {
                dataStore.rememberUser(loginData)
            }
        }
    }

    fun saveUserData(data: RegisterData) {
        viewModelScope.launch {
            dataStore.saveUserIdTokens(data.user.id, data.accessToken, data.refreshToken)
        }
    }

    fun saveNewToken(tokens: RefreshTokenResponse) {
        viewModelScope.launch {
            dataStore.saveNewTokens(tokens.accessToken, tokens.refreshToken)
        }
    }

    fun setAuthorizedState(isAuthorize: Boolean) {
        viewModelScope.launch { dataStore.setAuthorizeState(isAuthorize) }
    }

    fun getAuthorizeState() {
        viewModelScope.launch {
          _isAuthorized.value=  dataStore.getAuthorizeState()
        }
    }


}
package com.example.myapplication.presentation.ui.auth.fragments.login


import com.example.myapplication.data.repository.users.UsersRepository
import com.example.myapplication.domain.model.LoginData
import com.example.myapplication.domain.model.UserRegisterResponse
import com.example.myapplication.presentation.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel

class LoginViewModel @Inject constructor(private val usersRepository: UsersRepository) :
    BaseViewModel() {

    private val _user = MutableUIStateFlow<UserRegisterResponse>()
    val user = _user

    fun login(loginData: LoginData) {
        usersRepository.login(loginData).collectRequest(_user)
    }


}
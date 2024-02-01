package com.example.myapplication.presentation.ui.auth.fragments.signUp


import com.example.myapplication.data.repository.users.UsersRepository
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.model.UserRegisterResponse
import com.example.myapplication.presentation.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val usersRepository: UsersRepository) :
    BaseViewModel() {
    private val _user = MutableUIStateFlow<UserRegisterResponse>()
    val user = _user

    fun register(user: User) {
        usersRepository.register(user).collectRequest(_user)
    }
}
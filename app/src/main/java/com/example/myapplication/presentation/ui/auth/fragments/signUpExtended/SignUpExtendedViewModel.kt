package com.example.myapplication.presentation.ui.auth.fragments.signUpExtended


import com.example.myapplication.data.repository.users.UsersRepository
import com.example.myapplication.domain.model.EditBody
import com.example.myapplication.domain.model.EditResponse
import com.example.myapplication.presentation.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpExtendedViewModel @Inject constructor(private val usersRepository: UsersRepository) :
    BaseViewModel() {
    private val _user = MutableUIStateFlow<EditResponse>()
    val user = _user
    fun editUser(requestBody: EditBody) {

        usersRepository.editUser(requestBody ).collectRequest(_user)


    }
}
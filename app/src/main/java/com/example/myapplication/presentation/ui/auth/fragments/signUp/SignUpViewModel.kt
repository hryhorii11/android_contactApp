package com.example.myapplication.presentation.ui.auth.fragments.signUp


import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.datastore.DataStorePreferences
import com.example.myapplication.data.users.repository.UsersRepository
import com.example.myapplication.domain.model.RegisterData
import com.example.myapplication.domain.model.Response
import com.example.myapplication.domain.model.User
import com.example.myapplication.presentation.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val dataStore: DataStorePreferences
) :
    BaseViewModel() {
    private val _user = MutableUIStateFlow<Response<RegisterData>>()
    val user = _user

    fun register(user: User) {
        usersRepository.register(user).collectRequest(_user)
    }

    fun setAuthorizeState() {
        viewModelScope.launch {
            dataStore.setAuthorizeState(true)
        }

    }
}
package com.example.myapplication.presentation.ui.main.fragments.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.datastore.DataStorePreferences
import com.example.myapplication.data.users.repository.UsersRepository
import com.example.myapplication.data.users.repository.UsersRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UsersRepository,
    private val dataStore: DataStorePreferences
) : ViewModel() {
    fun setCurrentUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            UsersRepositoryImpl.currentUser =
                repository.getUser(UsersRepositoryImpl.currentUser.id.toInt()).toCurrentUser(
                    UsersRepositoryImpl.currentUser.accessToken,
                    UsersRepositoryImpl.currentUser.refreshToken
                )
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStore.setAuthorizeState(false)
        }
    }
}

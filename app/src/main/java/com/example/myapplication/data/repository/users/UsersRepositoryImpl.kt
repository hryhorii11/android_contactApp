package com.example.myapplication.data.repository.users

import com.example.myapplication.data.api.UserApi
import com.example.myapplication.data.repository.BaseRepository
import com.example.myapplication.domain.model.CurrentUser
import com.example.myapplication.domain.model.EditBody
import com.example.myapplication.domain.model.LoginData
import com.example.myapplication.domain.model.User
import com.example.myapplication.presentation.utils.Constants.AUTHORIZATION_HEADER
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(private val remoteData: UserApi) : UsersRepository,
    BaseRepository() {
    override fun login(loginData: LoginData) = doRequest {
        remoteData.loginUser(loginData).also {
            currentUser = it.data.user.toCurrentUser(it.data.accessToken, it.data.refreshToken)
        }
    }

    override fun register(user: User) = doRequest {
        remoteData.registerUser(user).also {
            currentUser = it.data.user.toCurrentUser(it.data.accessToken, it.data.refreshToken)
        }
    }

    override fun editUser(body: EditBody) = doRequest {
        remoteData.editUser(
            AUTHORIZATION_HEADER + currentUser.accessToken,
            currentUser.id.toInt(),
            body
        )
            .also {
                currentUser =
                    it.data.user.toCurrentUser(currentUser.accessToken, currentUser.refreshToken)

            }
    }

    companion object {
        var currentUser = CurrentUser()
    }
}
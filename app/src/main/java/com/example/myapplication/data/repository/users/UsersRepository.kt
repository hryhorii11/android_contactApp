package com.example.myapplication.data.repository.users

import com.example.myapplication.domain.model.EditBody
import com.example.myapplication.domain.model.EditResponse
import com.example.myapplication.domain.model.LoginData
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.model.UserRegisterResponse
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun login(loginData: LoginData): Flow<Result<UserRegisterResponse>>
    fun register(user: User): Flow<Result<UserRegisterResponse>>
    fun editUser(body: EditBody): Flow<Result<EditResponse>>


}
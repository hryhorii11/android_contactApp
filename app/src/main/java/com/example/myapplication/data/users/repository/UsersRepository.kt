package com.example.myapplication.data.users.repository

import com.example.myapplication.domain.model.EditBody
import com.example.myapplication.domain.model.EditUserResponseData
import com.example.myapplication.domain.model.LoginData
import com.example.myapplication.domain.model.RefreshTokenResponse
import com.example.myapplication.domain.model.RegisterData
import com.example.myapplication.domain.model.Response
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.model.UserFromLogin
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun login(loginData: LoginData): Flow<Result<Response<RegisterData>>>
    fun register(user: User): Flow<Result<Response<RegisterData>>>
    fun editUser(body: EditBody): Flow<Result<Response<EditUserResponseData>>>
    fun refreshToken():Flow<Result<Response<RefreshTokenResponse>>>
    fun getUser(userId:Int):UserFromLogin
}
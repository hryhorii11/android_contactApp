package com.example.myapplication.data.retrofit

import com.example.myapplication.data.model.AddContactBody
import com.example.myapplication.data.api.UserApi
import com.example.myapplication.data.model.LoginData
import com.example.myapplication.data.model.User
import com.example.myapplication.data.model.EditBody
import javax.inject.Inject

class UserRemoteData @Inject constructor(private val mainService: UserApi) {

    suspend fun login(loginData: LoginData) = mainService.loginUser(loginData)
    suspend fun register(user: User) = mainService.registerUser(user)
    suspend fun editUser(token: String, id: Int, body: EditBody) =
        mainService.editUser(token, id, body)

    suspend fun getUserContacts(token: String, id: Int) = mainService.getUserContacts(token, id)
    suspend fun getAllUsers(token: String) = mainService.getAllUsers(token)
    suspend fun deleteContact(token: String, userId: Int, contactId: Int) =
        mainService.deleteContact(token, userId, contactId)

    suspend fun addContact(token: String, userId: Int, requestBody: AddContactBody) =
        mainService.addContact(token, userId, requestBody)
}

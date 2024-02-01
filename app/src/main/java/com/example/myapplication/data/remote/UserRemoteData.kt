package com.example.myapplication.data.remote

import com.example.myapplication.domain.model.AddContactBody
import com.example.myapplication.domain.model.EditBody
import com.example.myapplication.domain.model.EditResponse
import com.example.myapplication.domain.model.GetContactsResponse
import com.example.myapplication.domain.model.GetUsersResponse
import com.example.myapplication.domain.model.LoginData
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.model.UserRegisterResponse

interface UserRemoteData {

    suspend fun login(loginData: LoginData): UserRegisterResponse
    suspend fun register(user: User): UserRegisterResponse
    suspend fun editUser(token: String, id: Int, body: EditBody): EditResponse

    suspend fun getUserContacts(token: String, id: Int): GetContactsResponse
    suspend fun getAllUsers(token: String): GetUsersResponse
    suspend fun deleteContact(token: String, userId: Int, contactId: Int)
    suspend fun addContact(token: String, userId: Int, requestBody: AddContactBody)


}
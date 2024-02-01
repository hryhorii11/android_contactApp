//package com.example.myapplication.data.remote
//
//import com.example.myapplication.domain.model.AddContactBody
//import com.example.myapplication.data.api.UserApi
//import com.example.myapplication.domain.model.LoginData
//import com.example.myapplication.domain.model.User
//import com.example.myapplication.domain.model.EditBody
//import javax.inject.Inject
//
//class UserRemoteDataImpl @Inject constructor(private val mainService: UserApi) : UserRemoteData {
//
//    override suspend fun login(loginData: LoginData) = mainService.loginUser(loginData)
//    override suspend fun register(user: User) = mainService.registerUser(user)
//    override suspend fun editUser(token: String, id: Int, body: EditBody) =
//        mainService.editUser(token, id, body)
//
//    override suspend fun getUserContacts(token: String, id: Int) =
//        mainService.getUserContacts(token, id)
//
//    override suspend fun getAllUsers(token: String) = mainService.getAllUsers(token)
//    override suspend fun deleteContact(token: String, userId: Int, contactId: Int) =
//        mainService.deleteContact(token, userId, contactId)
//
//    override suspend fun addContact(token: String, userId: Int, requestBody: AddContactBody) =
//        mainService.addContact(token, userId, requestBody)
//}

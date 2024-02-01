package com.example.myapplication.data.api

import com.example.myapplication.domain.model.AddContactBody
import com.example.myapplication.domain.model.GetContactsResponse
import com.example.myapplication.domain.model.GetUsersResponse
import com.example.myapplication.domain.model.LoginData
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.model.UserRegisterResponse
import com.example.myapplication.domain.model.EditBody
import com.example.myapplication.domain.model.EditResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    @POST("users")
    suspend fun registerUser(
        @Body user: User
    ): UserRegisterResponse

    @Headers("Content-type: application/json")
    @PUT("users/{userId}")
    suspend fun editUser(
        @Header("Authorization") accessToken: String,
        @Path("userId") userId: Int,
        @Body editBody: EditBody
    ): EditResponse

    @POST("login")
    suspend fun loginUser(@Body loginData: LoginData): UserRegisterResponse

    @GET("users/{userId}/contacts")
    suspend fun getUserContacts(
        @Header("Authorization") accessToken: String,
        @Path("userId") userId: Int
    ): GetContactsResponse

    @GET("users")
    suspend fun getAllUsers(@Header("Authorization") accessToken: String): GetUsersResponse

    @Headers("Content-type: application/json")
    @PUT("users/{userId}/contacts")
    suspend fun addContact(
        @Header("Authorization") accessToken: String,
        @Path("userId") userId: Int,
        @Body body: AddContactBody
    ):GetContactsResponse

    @Headers("Content-type: application/json")
    @DELETE("users/{userId}/contacts/{contactId}")
    suspend fun deleteContact(
        @Header("Authorization") accessToken: String,
        @Path("userId") userId: Int,
        @Path("contactId") contactId: Int,
    ):GetContactsResponse
}

package com.example.myapplication.data.api

import com.example.myapplication.domain.model.AddContactBody
import com.example.myapplication.domain.model.Contacts
import com.example.myapplication.domain.model.LoginData
import com.example.myapplication.domain.model.EditBody
import com.example.myapplication.domain.model.EditUserResponseData
import com.example.myapplication.domain.model.RefreshTokenResponse
import com.example.myapplication.domain.model.RegisterData
import com.example.myapplication.domain.model.Response
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.model.Users
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @POST("users")
    suspend fun registerUser(
        @Body user: User
    ): Response<RegisterData>

    @Headers("Content-type: application/json")
    @PUT("users/{userId}")
    suspend fun editUser(
        @Header("Authorization") accessToken: String,
        @Path("userId") userId: Int,
        @Body editBody: EditBody
    ): Response<EditUserResponseData>

    @POST("login")
    suspend fun loginUser(@Body loginData: LoginData): Response<RegisterData>

    @GET("users/{userId}/contacts")
    suspend fun getUserContacts(
        @Header("Authorization") accessToken: String,
        @Path("userId") userId: Int
    ): Response<Contacts>

    @GET("users")
    suspend fun getAllUsers(@Header("Authorization") accessToken: String): Response<Users>

    @Headers("Content-type: application/json")
    @PUT("users/{userId}/contacts")
    suspend fun addContact(
        @Header("Authorization") accessToken: String,
        @Path("userId") userId: Int,
        @Body body: AddContactBody
    ): Response<Contacts>

    @Headers("Content-type: application/json")
    @DELETE("users/{userId}/contacts/{contactId}")
    suspend fun deleteContact(
        @Header("Authorization") accessToken: String,
        @Path("userId") userId: Int,
        @Path("contactId") contactId: Int,
    ): Response<Contacts>

    @POST("refresh")
    suspend fun refreshToken(@Header("RefreshToken") refreshToken: String): Response<RefreshTokenResponse>
}

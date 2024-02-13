package com.example.myapplication.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateResponse(
    val user: UserFromLogin,
    val accessToken: String,
    val refreshToken: String
) : Parcelable

@Parcelize
data class CreateUserResponseData(
    val email: String,
    val name: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val career: String? = null,
    val birthday: String? = null,
    val facebook: String? = null,
    val instagram: String? = null,
    val twitter: String? = null,
    val linkedin: String? = null,
    val id: Int? = null
) : Parcelable

data class UserRegisterResponse(
    val status: String,
    val code: Int,
    val message: String,
    val data: CreateResponse
)

data class LoginData(
    val email: String,
    val password: String
)
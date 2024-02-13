package com.example.myapplication.data.model

data class EditResponse(
    val status: String,
    val code: Int,
    val message: String,
    val data: EditUserResponse
)

data class EditUserResponse(
    val user: UserFromLogin
)

data class EditBody(
    val name: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val career: String? = null,
    val birthday: String? = null,
    val facebook: String? = null,
    val instagram: String? = null,
    val twitter: String? = null,
    val linkedin: String? = null,
    val image: String? = null
)

data class GetContactsResponse(
    val code: Int,
    val data: Contacts,
    val message: String,
    val status: String
)

data class Contacts(
    val contacts: List<UserFromLogin>?
)

data class GetUsersResponse(
    val code: Int,
    val data: Users,
    val message: String,
    val status: String
)

data class Users(
    val users: List<UserFromLogin>?
)

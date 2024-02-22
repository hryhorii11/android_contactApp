package com.example.myapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val email: String,
    val password: String,
    val name: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val career: String? = null,
    val birthday: String? = null,
    val facebook: String? = null,
    val instagram: String? = null,
    val twitter: String? = null,
    val linkedin: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
) : Parcelable

data class CurrentUser(
    val id: Long = 0,
    val name: String? = null,
    val email: String,
    val phone: String? = null,
    val address: String? = null,
    val career: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
)

@Parcelize
data class UserFromLogin(
    val id: Int,
    val name: String? = null,
    val email: String,
    val phone: String? = null,
    val address: String? = null,
    val career: String? = null,
    val birthday: String? = null,
    val facebook: String? = null,
    val instagram: String? = null,
    val twitter: String? = null,
    val linkedin: String? = null,
    val image: String? = null,
) : Parcelable {
    fun toContact() =
        Contact(id, name, career, address, image)

    fun toCurrentUser(accessToken: String?, refreshToken: String?) = CurrentUser(
        id.toLong(),
        name,
        email,
        phone,
        address,
        career,
        accessToken,
        refreshToken
    )
}
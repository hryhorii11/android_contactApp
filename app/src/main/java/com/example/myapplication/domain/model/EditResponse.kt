package com.example.myapplication.domain.model


data class EditUserResponseData(
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

data class Contacts(
    val contacts: List<UserFromLogin>?
){
    fun toUi(): List<Contact> {
        return contacts?.map { it.toContact() }!!
    }
}
data class Users(
    val users: List<UserFromLogin>?
){
    fun toUi(): List<Contact> {
        return users?.map { it.toContact() }!!
    }
}

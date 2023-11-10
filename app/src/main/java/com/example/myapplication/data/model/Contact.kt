package com.example.myapplication.data.model

import java.util.UUID

data class Contact(
    val name: String,
    val career: String,
    val address:String,
    val photo: String,
    var isChecked:Boolean=false,  // TODO var in data class - bad practice
    val id: UUID = UUID.randomUUID()
)

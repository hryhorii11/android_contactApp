package com.example.myapplication.data.model

import java.util.UUID

data class Contact(
    val name: String,
    val career: String,
    val address:String,
    val photo: String,
    val isChecked:Boolean=false,
    val id: UUID = UUID.randomUUID()
)

package com.example.level2.data.model

import java.util.UUID

data class Contact( // TODO: can set default values
    val name: String,
    val career: String,
    val photo: String,
    val id: UUID = UUID.randomUUID()
)

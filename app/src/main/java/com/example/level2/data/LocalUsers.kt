package com.example.level2.data

import com.example.level2.data.model.Contact

class LocalUsers {
    fun getUsers() :List<Contact> = listOf(
        Contact("NAME1", "career1", "address","photo"),
        Contact("NAME2", "career2", "address","photo"),
        Contact("NAME3", "career3","address", "photo"),
        Contact("NAME4", "career4","address", "photo"),
        Contact("NAME5", "career5","address", "photo"),
        Contact("NAME6", "career6","address", "photo"),
        Contact("NAME7", "career7", "address","photo"),
        Contact("NAME8", "career8","address", "photo"),
        Contact("NAME9", "career9","address", "photo"),
        Contact("NAME10", "career10", "address","photo"),
    )
}

package com.example.level2.data

import com.example.level2.data.model.Contact

class LocalUsers {
    fun getUsers() :List<Contact> = listOf(
        Contact("NAME1", "career1", "address","https://images.unsplash.com/photo-1600267185393-e158a98703de?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NjQ0&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800"),
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

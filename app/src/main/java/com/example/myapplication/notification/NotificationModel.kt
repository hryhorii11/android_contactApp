package com.example.myapplication.notification


data class NotificationModel(
    val to: String,
    val data: Data,
)

data class Data(
    val title: String,
    val message: String,
    val uri: String
)


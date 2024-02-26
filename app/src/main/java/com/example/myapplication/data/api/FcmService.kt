package com.example.myapplication.data.api

import com.example.myapplication.notification.NotificationModel
import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface FcmService {
    companion object {
        const val BASE_URL = "https://fcm.googleapis.com/"
    }
    @Headers(
        "Authorization: key=AAAAaHNOue0:APA91bGq_U1sXrMVyBH-yOU2zZ7E1wLox6ZoZ4MFD7QL5mhqJ7DkBZAMSEH0Yo9RZ5MJLgIOx3n6vkksK6l6-LAKcdVAYB_rMDeFFajFaWVOZSC4Ubr4lSS-mUjBNVUJO1aypWX-TXs-",
        "Content-Type:application/json"
    )
    @POST("fcm/send")
    suspend fun sendNotification(@Body notificationModel: NotificationModel): Response<ResponseBody>


}

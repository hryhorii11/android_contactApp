package com.example.myapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Contact(
    val id:Int,
    val name: String?="",
    val career: String?="",
    val address:String?="",
    val photo: String?="",
    val isChecked:Boolean=false,
):Parcelable

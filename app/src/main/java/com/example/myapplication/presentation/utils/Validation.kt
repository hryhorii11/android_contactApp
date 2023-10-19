package com.example.myapplication.presentation.utils

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.Locale

object Validation {
    fun isUsernameValid(userName: String): Boolean {
        return userName.matches(Constants.USERNAME_VALIDATION_PATTERN.toRegex())
    }

    fun isCareerValid(career: String): Boolean {
        return career.isNotBlank()
    }

    fun isEmailValid(email: String): Boolean {
        return email.matches(Patterns.EMAIL_ADDRESS.toRegex())
    }

    fun isPhoneValid(phone: String): Boolean {
        return phone.length == Constants.phoneNumbers
    }

    fun isValidDate(inputDate: String): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false

        return try {
            dateFormat.parse(inputDate)
            true
        } catch (e: Exception) {
            false
        }
    }
}
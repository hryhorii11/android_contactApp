package com.example.myapplication.presentation.ui.fragments.interfaces

import androidx.navigation.Navigator
import com.example.myapplication.data.model.Contact

interface ItemClickListener {
    fun onContactDelete(contact: Contact)
    fun onContactClick(contact: Contact, extras: Navigator.Extras)
    fun onContactChangeMode()
    fun onCheckBoxClick(contact: Contact)
    fun togleSelect(contact:Contact)
}
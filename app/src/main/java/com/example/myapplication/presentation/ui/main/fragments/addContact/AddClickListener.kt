package com.example.myapplication.presentation.ui.main.fragments.addContact

import com.example.myapplication.domain.model.Contact

interface AddClickListener {
    fun addContact(id:Int)
    fun onItemClick(contact: Contact)
}
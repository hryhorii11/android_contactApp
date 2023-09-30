package com.example.level2.presentation.ui.activities.contacts.intrefaces

import com.example.level2.data.model.Contact

interface ItemClickListener {
    fun onUserDelete(contact: Contact)
}
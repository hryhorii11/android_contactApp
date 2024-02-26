package com.example.myapplication.presentation.ui.main.fragments.contactList.util

import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.domain.model.Contact

object ItemCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean
            = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}

package com.example.level2.presentation.ui.fragments.adapters.util

import androidx.recyclerview.widget.DiffUtil
import com.example.level2.data.model.Contact

object ItemCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean
            = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}

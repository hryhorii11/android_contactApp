package com.example.level2.presentation.ui.activities.contacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.level2.presentation.ui.activities.contacts.adapter.utils.ItemCallback
import com.example.level2.presentation.ui.activities.contacts.intrefaces.ItemClickListener
import com.example.level2.presentation.utils.ext.setPhoto
import com.example.level2.data.model.Contact
import com.example.level2.databinding.UserItemBinding


class ContactAdapter(private val itemClickListener: ItemClickListener)
    : ListAdapter<Contact, ContactAdapter.ContactHolder>(ItemCallback) {

    inner class ContactHolder(private val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
           with(binding) {
               buttonDeleteUser.tag = contact
               textViewContactName.text = contact.name
               textViewContactCareer.text = contact.career
               imageViewContactPhoto.setPhoto(contact.photo)
           }
            setListeners(contact)
        }

        private fun setListeners(contact: Contact) {
            binding.buttonDeleteUser.setOnClickListener { itemClickListener.onUserDelete(contact) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding=UserItemBinding.inflate(inflater,parent,false)

        return ContactHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind(currentList[position])
    }
}


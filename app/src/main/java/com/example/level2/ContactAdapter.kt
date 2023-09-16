package com.example.level2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.level2.databinding.UserItemBinding
import com.example.level2.ext.setPhoto
import com.example.level2.model.Contact


class ContactAdapter(private val itemClickListener: ItemClickListener)
    : ListAdapter<Contact, ContactAdapter.ContactHolder>(ItemCallback),
    View.OnClickListener {

    class ContactHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding=UserItemBinding.inflate(inflater,parent,false)

        binding.buttonDeleteUser.setOnClickListener(this)
        return ContactHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val contact = getItem(position)

        with(holder.binding) {
            root.tag = contact
            buttonDeleteUser.tag = contact
            textViewContactName.text = contact.name
            textViewContactCareer.text = contact.career
            imageViewContactPhoto.setPhoto(contact.photo)
        }
    }

    override fun onClick(v: View?) {
        val contact = v?.tag as Contact
        when (v.id) {
            R.id.buttonDeleteUser -> itemClickListener.onUserDelete(contact)
        }
    }

    object ItemCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }

}


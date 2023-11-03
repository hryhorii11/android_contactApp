package com.example.level2.presentation.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.level2.presentation.utils.ext.setPhoto
import com.example.level2.data.model.Contact
import com.example.level2.databinding.UserItemBinding
import com.example.level2.presentation.ui.fragments.adapters.util.ItemCallback
import com.example.level2.presentation.ui.fragments.ItemClickListener
import com.example.level2.presentation.utils.Constants.DETAIL_EXTRAS_NAME


class ContactAdapter(private val itemClickListener: ItemClickListener) :
    ListAdapter<Contact, ContactAdapter.ContactHolder>(ItemCallback) {

    inner class ContactHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
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
            with(binding) {
                cardViewItem.setOnClickListener { toDetainScreen(contact) }
                buttonDeleteUser.setOnClickListener { itemClickListener.onContactDelete(contact) }
            }
        }

        private fun toDetainScreen(contact: Contact) {
            val extras =
                FragmentNavigatorExtras(binding.imageViewContactPhoto to DETAIL_EXTRAS_NAME)
            itemClickListener.onContactDetail(
                contact,
                extras
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = UserItemBinding.inflate(inflater, parent, false)

        return ContactHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind(currentList[position])
    }


}






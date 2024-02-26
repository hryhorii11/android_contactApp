package com.example.myapplication.presentation.ui.main.fragments.addContact

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.domain.model.Contact
import com.example.myapplication.databinding.AddContactItemBinding
import com.example.myapplication.presentation.ui.main.fragments.contactList.util.ItemCallback
import com.example.myapplication.presentation.utils.ext.setPhoto

class AddContactAdapter(private val clickListener: AddClickListener) :
    ListAdapter<Contact, AddContactAdapter.ContactHolder>(ItemCallback) {
    inner class ContactHolder(private val binding: AddContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val clickedPositions = mutableSetOf<Int>()

        fun bind(contact: Contact) {
            with(binding) {
                textViewContactName.text = contact.name
                textViewContactCareer.text = contact.career
                if (contact.photo != null)
                    imageViewContactPhoto.setPhoto(contact.photo)
                else imageViewContactPhoto.setPhoto()
                if (contact.isChecked) buttonDeleteUser.setImageResource(R.drawable.ic_added)
                else buttonDeleteUser.setImageResource(R.drawable.ic_add)
                setListeners(contact)
            }
        }

        private fun setListeners(contact: Contact) {
            binding.buttonDeleteUser.setOnClickListener {
               if (!clickedPositions.contains(currentList.indexOf(contact)))
                {
                   clickedPositions.add((currentList.indexOf(contact)))
                    clickListener.addContact(contact.id)
                    notifyItemChanged(currentList.indexOf(contact))
                }
            }
            binding.cardViewItem.setOnClickListener{
                clickListener.onItemClick(contact)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddContactAdapter.ContactHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AddContactItemBinding.inflate(inflater, parent, false)

        return ContactHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind(currentList[position])
    }

}


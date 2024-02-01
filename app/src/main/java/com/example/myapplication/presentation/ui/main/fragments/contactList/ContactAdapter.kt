package com.example.myapplication.presentation.ui.main.fragments.contactList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.domain.model.Contact
import com.example.myapplication.databinding.UserItemBinding
import com.example.myapplication.presentation.ui.main.fragments.contactList.util.ItemCallback
import com.example.myapplication.presentation.utils.ext.setPhoto


class ContactAdapter(private val itemClickListener: ItemClickListener) :
    ListAdapter<Contact, ContactAdapter.ContactHolder>(ItemCallback) {
    var isSelectionMode = false

    inner class ContactHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            with(binding) {
                if (isSelectionMode) {
                    checkboxMultiSelect.visibility = View.VISIBLE
                    buttonDeleteUser.visibility = View.GONE
                    cardViewItem.setCardBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.multiselectBackground
                        )
                    )
                    checkboxMultiSelect.isChecked = contact.isChecked
                } else {
                    checkboxMultiSelect.visibility = View.GONE
                    buttonDeleteUser.visibility = View.VISIBLE
                    cardViewItem.setCardBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.white
                        )
                    )
                }
                textViewContactName.text = contact.name
                textViewContactCareer.text = contact.career
                contact.photo?.let { imageViewContactPhoto.setPhoto(it) }
            }
            setListeners(contact)
        }

        private fun setListeners(contact: Contact) {
            val extras =
                FragmentNavigatorExtras(binding.imageViewContactPhoto to "sharedImageFromDetail")
            with(binding) {
                cardViewItem.setOnClickListener {
                    itemClickListener.onContactClick(
                        contact,
                        extras
                    )
                }
                checkboxMultiSelect.setOnClickListener {
                    itemClickListener.onCheckBoxClick(contact)
                }
                cardViewItem.setOnLongClickListener {
                    if (!isSelectionMode) {
                        itemClickListener.onContactChangeMode()
                        itemClickListener.toggleSelect(contact)
                    }
                    true
                }
                buttonDeleteUser.setOnClickListener {
                    if (currentList.indexOf(contact) != -1)
                        itemClickListener.onContactDelete(contact)
                }
            }
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

    @SuppressLint("NotifyDataSetChanged")
    fun changeMode() {
        isSelectionMode = !isSelectionMode
        notifyDataSetChanged()
    }

}






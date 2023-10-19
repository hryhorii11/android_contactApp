package com.example.myapplication.presentation.ui.fragments.adapters

import com.example.myapplication.presentation.ui.fragments.interfaces.ItemClickListener
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Contact
import com.example.myapplication.databinding.UserItemBinding
import com.example.myapplication.presentation.ui.fragments.adapters.util.ItemCallback
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
                imageViewContactPhoto.setPhoto(contact.photo)
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
                    itemClickListener.onContactChangeMode()
                    contact.isChecked = !contact.isChecked
                    true
                }
                buttonDeleteUser.setOnClickListener {
                    itemClickListener.onContactDelete(
                        contact
                    )
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


    fun toggleSelection(position: Int) {
        currentList[position].isChecked = !currentList[position].isChecked
        notifyItemChanged(position)
        if (currentList.none { it.isChecked })
            changeMode()
    }


}






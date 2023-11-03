package com.example.myapplication.presentation.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.myapplication.data.model.Contact

class ContactListViewModel
    : ViewModel() {
    private var lastDeletedContact: Pair<Int, Contact>? = null

    private val _contacts = MutableLiveData<List<Contact>>()
    var contacts: LiveData<List<Contact>> = _contacts

    fun setUsers(contactList: List<Contact>) {
        _contacts.value = contactList.toMutableList()
    }

    fun deleteContact(contact: Contact) {
        if(lastDeletedContact?.second != contact) {
            _contacts.value = contacts.value?.toMutableList()?.apply {
                lastDeletedContact = indexOf(contact) to contact
                remove(contact)
            }
        }

    }

    fun addContact(contact: Contact) {
        val currentList = _contacts.value?.toMutableList()
        currentList?.add(contact)
        _contacts.value = currentList?.toList()
    }

    fun deleteSelectedContacts()
    {
        val contactList=_contacts.value?.toMutableList()
        _contacts.value=contactList?.filter { !it.isChecked }
    }
    fun returnContact() {
        lastDeletedContact?.let {
            val currentList = _contacts.value.orEmpty().toMutableList()
            currentList.add(it.first, it.second)
            lastDeletedContact = null
            _contacts.value = currentList
        }
    }
}
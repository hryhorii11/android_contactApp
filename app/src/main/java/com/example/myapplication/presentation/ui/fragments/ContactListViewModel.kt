package com.example.myapplication.presentation.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.myapplication.data.model.Contact

class ContactListViewModel
    : ViewModel() {
    private var contactToReturn: Contact? = null
    private var contactToReturnPosition: Int? = null
    private val _contacts = MutableLiveData<List<Contact>>()
    var contacts: LiveData<List<Contact>> = _contacts

    fun setUsers(contactList: List<Contact>) {
        _contacts.value = contactList.toMutableList()
    }

    fun deleteContact(contact: Contact) {
        _contacts.value = contacts.value?.toMutableList()?.apply {
            contactToReturn = contact
            contactToReturnPosition = indexOf(contact)
            remove(contact)
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
        val currentList = _contacts.value?.toMutableList()
        if (!currentList?.contains(contactToReturn)!!) {
            currentList.let {
                contactToReturn?.let { it1 ->
                    contactToReturnPosition?.let { it2 ->
                        it.add(
                            it2,
                            it1
                        )
                    }
                }
                _contacts.value = it.toList()
            }
        }
        contactToReturn = null
        contactToReturnPosition = null
    }
}
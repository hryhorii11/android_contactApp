package com.example.level2.presentation.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.level2.data.LocalUsers
import com.example.level2.data.model.Contact


class ContactListViewModel : ViewModel() {
    private var contactToReturn: Contact? = null
    private var contactToReturnPosition: Int? = null
    private var lastDeletedContact: Pair<Int, Contact>? = null

    private val _contacts = MutableLiveData<List<Contact>>()
    var contacts: LiveData<List<Contact>> = _contacts

    init {
        _contacts.value = LocalUsers().getUsers()
    }

    fun setUsers(contactList: List<Contact>) {
        // _contacts.value = contactList
    }

    fun deleteContact(contact: Contact) {
        if(lastDeletedContact?.second != contact) {
            _contacts.value = contacts.value?.toMutableList()?.apply {
                lastDeletedContact = indexOf(contact) to contact
                remove(contact)
            }
        }
    }

    fun getContactFromPosition(position: Int): Contact {
        return _contacts.value?.get(position)!!
    }

    fun addContact(user: Contact) {
        val currentList = _contacts.value?.toMutableList()
        currentList?.add(user)
        _contacts.value = currentList?.toList()
    }

//    fun returnContact() { // TODO: not correctly
//        val currentList = _contacts.value.orEmpty().toMutableList()
//        if (!currentList?.contains(contactToReturn)!!) {
//            currentList.let {
//                contactToReturn?.let { it1 ->
//                    contactToReturnPosition?.let { it2 ->
//                        it.add(
//                            it2,
//                            it1
//                        )
//                    }
//                }
//                _contacts.value = it.toList()
//            }
//        }
//        contactToReturn = null
//        contactToReturnPosition = null
//    }


    fun returnContact() { // TODO: not correctly
        lastDeletedContact?.let {
            val currentList = _contacts.value.orEmpty().toMutableList()
            currentList.add(it.first, it.second)
            lastDeletedContact = null
            _contacts.value = currentList
        }
    }
}
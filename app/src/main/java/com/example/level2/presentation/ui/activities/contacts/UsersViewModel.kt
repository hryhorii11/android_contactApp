package com.example.level2.presentation.ui.activities.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.level2.data.LocalUsers
import com.example.level2.data.model.Contact

class UsersViewModel : ViewModel() {
    private var contactToReturn: Contact? = null
    private var contactToReturnPosition: Int? = null
    private val _contacts = MutableLiveData<List<Contact>>(listOf())
    val contacts = _contacts

    fun setUsers(contactList: List<Contact>) {
        if (contactList.isNotEmpty())
            _contacts.value = contactList
        else _contacts.value = LocalUsers().getUsers()
    }

    fun deleteContact(contact: Contact) {
        _contacts.value = contacts.value?.toMutableList()?.apply {
            contactToReturn = contact
            contactToReturnPosition = indexOf(contact)
            remove(contact)
        }
    }

    fun addContact(user: Contact) {
        val currentList = _contacts.value?.toMutableList()
        currentList?.add(user)
        _contacts.value = currentList?.toList()
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

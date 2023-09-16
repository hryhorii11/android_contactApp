package com.example.level2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.level2.model.Contact

class UsersViewModel
    :ViewModel() {
    private var contactToReturn: Contact? =null
    private var contactToReturnPosition:Int?=null
    private val _contacts=MutableLiveData<List<Contact>>()
    var contacts:LiveData<List<Contact>> = _contacts

    fun setUsers(contactList: List<Contact>)
    {
        _contacts.value=contactList.toMutableList()
        contacts=_contacts
    }
    fun deleteContact(contact: Contact)
    {
      _contacts.value= contacts.value?.toMutableList()?.apply {
          contactToReturn=contact
          contactToReturnPosition=indexOf(contact)
          remove(contact) }
    }
    fun addContact(user: Contact)
    {
        val currentList = _contacts.value?.toMutableList()
         currentList?.add(user)
        _contacts.value=currentList?.toList()
    }
    fun returnContact()
    {
        val currentList = _contacts.value?.toMutableList()
        currentList?.let {
            contactToReturn?.let { it1 -> contactToReturnPosition?.let { it2 -> it.add(it2,it1) } }
            _contacts.value = it.toList()
        }
        contactToReturn=null
        contactToReturnPosition=null
    }
}
package com.example.myapplication.presentation.ui.main.fragments.contactList

import com.example.myapplication.data.model.AddContactBody
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.myapplication.data.model.Contact
import com.example.myapplication.data.retrofit.UserRemoteData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(private val userRemoteData: UserRemoteData) :
    ViewModel() {
    val errorMessage = MutableLiveData<String>()
    private var lastDeletedContact: Pair<Int, Contact>? = null
    private val _contacts = MutableLiveData<List<Contact>>()
    var contacts: LiveData<List<Contact>> = _contacts
    private lateinit var originalList: MutableList<Contact>
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun setUsers(token: String?, id: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = userRemoteData.getUserContacts("Bearer $token", id)
            withContext(Dispatchers.Main) {
                _contacts.value = response.data.contacts?.map { it.toContact() }
                if (_contacts.value != null)
                    originalList = _contacts.value?.toMutableList()!!
            }
        }
    }

    fun deleteContact(contact: Contact, userId: Int, token: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            userRemoteData.deleteContact("Bearer $token", userId, contact.id)
        }
        if (lastDeletedContact?.second != contact) {
            _contacts.value = contacts.value?.toMutableList()?.apply {
                lastDeletedContact = indexOf(contact) to contact
                remove(contact)
            }
            originalList.remove(contact)
        }
    }

    fun search(name: String) {
        _contacts.value =
            originalList.filter {
                if (it.name == null) false
                else it.name.lowercase().contains(name.lowercase())
            }
        if (name == "") _contacts.value = originalList
    }

    fun toggleSelect(contact: Contact): Boolean {
        _contacts.value = contacts.value?.map {
            if (it == contact) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
        if (_contacts.value?.none { it.isChecked } == true) {
            return true
        }
        return false
    }


    fun deleteSelectedContacts(token: String, userId: Int) {
        val contactList = _contacts.value?.toMutableList()
        if (contactList != null) {
            for (contact: Contact in contactList) {
                if (contact.isChecked) deleteContact(contact, userId, token)
            }
        }
    }

    fun returnContact(userId: Int, token: String) {
        lastDeletedContact?.let {
            val currentList = _contacts.value.orEmpty().toMutableList()
            if (!currentList.contains(lastDeletedContact!!.second)) {
                currentList.add(it.first, it.second)
                originalList.add(it.first, it.second)
                _contacts.value = currentList
                viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                    userRemoteData.addContact(
                        "Bearer $token", userId,
                        AddContactBody(lastDeletedContact!!.second.id)
                    )
                    lastDeletedContact = null
                }
            }
        }
    }


    private fun onError(message: String) {
        viewModelScope.launch {
            Dispatchers.Main
            errorMessage.value = message
        }
    }
}
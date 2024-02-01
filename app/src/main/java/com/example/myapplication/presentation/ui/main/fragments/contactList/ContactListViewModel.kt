package com.example.myapplication.presentation.ui.main.fragments.contactList


import com.example.myapplication.data.repository.contacts.ContactsRepository
import com.example.myapplication.domain.model.Contact
import com.example.myapplication.domain.UIState
import com.example.myapplication.presentation.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(private val contactsRepository: ContactsRepository) :
    BaseViewModel() {
    private val originalList: MutableStateFlow<List<Contact>> = MutableStateFlow(emptyList())

    private val _contacts = MutableUIStateFlow<List<Contact>>()
    val contacts = _contacts.asStateFlow()
    fun setUsers() {
        contactsRepository.getUserContacts()
            .collectRequest(_contacts, originalList) { it.data.toUi() }
    }

    fun deleteContact(contact: Contact) {
        contactsRepository.deleteContact(contact.id)
            .collectRequest(_contacts, originalList) { it.data.toUi() }
    }

    fun search(name: String) {
        _contacts.value =
            UIState.Success(originalList.value.filter {
                if (it.name == null) false
                else it.name.lowercase().contains(name.lowercase())
            })
        if (name == "") _contacts.value = UIState.Success(originalList.value)
    }

    fun toggleSelect(contact: Contact): Boolean {
        originalList.value = originalList.value.map {
            if (it == contact) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
        _contacts.value = UIState.Success(originalList.value)
        if (originalList.value.none { it.isChecked }) {
            return true
        }
        return false
    }


    fun deleteSelectedContacts() {
        val contactList = (_contacts.value as? UIState.Success)?.data?.toMutableList()
        if (contactList != null) {
            for (contact in contactList) {
                if (contact.isChecked)
                    deleteContact(contact)
            }
        }
    }

    fun returnDeletedContact() {
        contactsRepository.returnDeletedContact()
            ?.collectRequest(_contacts, originalList) { it.data.toUi() }
    }

}
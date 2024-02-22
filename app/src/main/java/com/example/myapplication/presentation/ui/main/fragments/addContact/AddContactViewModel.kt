package com.example.myapplication.presentation.ui.main.fragments.addContact


import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.contacts.repository.ContactsRepository
import com.example.myapplication.domain.model.AddContactBody
import com.example.myapplication.domain.model.Contact
import com.example.myapplication.domain.UIState
import com.example.myapplication.presentation.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor(private val contactsRepository: ContactsRepository) :
    BaseViewModel() {
    private val _contacts = MutableUIStateFlow<List<Contact>>()
    val contacts = _contacts.asStateFlow()
    private val originalList: MutableStateFlow<List<Contact>> = MutableStateFlow(emptyList())


    fun setContacts() {
        contactsRepository.getContactsToAdd()
            .collectRequest(_contacts, originalList) { it.toUi() }
    }

    fun addContact(contactId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _contacts.value = UIState.Loading()
            contactsRepository.addContact(AddContactBody(contactId))
                .collect {
                    if (it.isFailure) _contacts.value =
                        UIState.Error(it.exceptionOrNull()?.message ?: "Unknown error")
                    if (it.isSuccess) {
                        originalList.value = (originalList.value.map { contact ->
                            if (contact.id == contactId)
                                contact.copy(isChecked = !contact.isChecked)
                            else contact
                        })
                        _contacts.value = UIState.Success(originalList.value)
                    }
                }
        }

    }

    fun search(name: String) {
        _contacts.value =
            UIState.Success(originalList.value.filter {
                if (it.name == null) false
                else it.name.lowercase().contains(name.lowercase())
            })
        if (name == "") _contacts.value = UIState.Success(originalList.value)
    }
}


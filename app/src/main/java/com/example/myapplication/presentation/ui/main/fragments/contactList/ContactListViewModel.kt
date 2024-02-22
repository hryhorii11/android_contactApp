package com.example.myapplication.presentation.ui.main.fragments.contactList


import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Contact
import com.example.myapplication.domain.UIState
import com.example.myapplication.data.api.FcmService
import com.example.myapplication.data.contacts.repository.ContactsRepository
import com.example.myapplication.notification.NotificationModel
import com.example.myapplication.presentation.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
    private val api: FcmService
) :
    BaseViewModel() {
    private val originalList: MutableStateFlow<List<Contact>> = MutableStateFlow(emptyList())
    private val _contacts = MutableUIStateFlow<List<Contact>>()
    val contacts = _contacts.asStateFlow()

    fun setUsers() {
        contactsRepository.fetch().collectRequest(_contacts, originalList) { it.toUi() }
    }

    fun deleteContact(contact: Contact) {
        contactsRepository.deleteContact(contact.id)
            .collectRequest(_contacts, originalList) { it.toUi() }
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
            ?.collectRequest(_contacts, originalList) { it.toUi() }
    }

    private val _connectionError = MutableStateFlow("")

    fun sendNotification(notificationModel: NotificationModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _connectionError.emit("sending")
            try {
                val response = api.sendNotification(notificationModel)
                if (response.isSuccessful) {
                    _connectionError.emit("sent")
                } else {
                    _connectionError.emit("error while sending")

                }
            } catch (e: Exception) {
                _connectionError.emit("error while sending")
            }

        }
    }
}



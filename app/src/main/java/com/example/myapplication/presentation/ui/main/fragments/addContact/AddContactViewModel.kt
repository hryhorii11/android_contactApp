package com.example.myapplication.presentation.ui.main.fragments.addContact

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
class AddContactViewModel @Inject constructor(private val userRemoteData: UserRemoteData): ViewModel() {
    private val _contacts = MutableLiveData<List<Contact>>()
    var contacts: LiveData<List<Contact>> = _contacts
    private lateinit var originalList: MutableList<Contact>
    val loading = MutableLiveData<Boolean>()
    private val errorMessage = MutableLiveData<String>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun setContacts(token: String, userId: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val responseAllContacts = userRemoteData.getAllUsers("Bearer $token")
            val responseUserContacts = userRemoteData.getUserContacts("Bearer $token", userId)
            withContext(Dispatchers.Main) {
                _contacts.value = responseAllContacts.data.users?.filter {
                    !responseUserContacts.data.contacts?.contains(it)!!
                }?.map { it.toContact() }
                if (_contacts.value != null)
                    originalList = _contacts.value?.toMutableList()!!
            }
        }
    }

    fun addContact(token: String, contactId: Int, userId: Int) {
        loading.value = true
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            userRemoteData.addContact("Bearer $token", userId, AddContactBody(contactId))
            withContext(Dispatchers.Main) {
                _contacts.value = contacts.value?.map {
                    if (it.id == contactId) {
                        it.copy(isChecked = !it.isChecked)
                    } else {
                        it
                    }
                }
                loading.value = false
            }
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

    private fun onError(message: String) {
        viewModelScope.launch {
            Dispatchers.Main
            errorMessage.value = message
        }
    }
}
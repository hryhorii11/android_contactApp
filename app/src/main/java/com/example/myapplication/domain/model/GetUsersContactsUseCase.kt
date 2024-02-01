//package com.example.myapplication.domain.model
//
//import com.example.myapplication.data.repository.contacts.ContactsRepository
//import javax.inject.Inject
//
//class GetUsersContactsUseCase @Inject constructor(
//    private val repository: ContactsRepository
//) {
//    operator fun invoke(token: String, id: Int) = repository.getUserContacts(token, id)
//}
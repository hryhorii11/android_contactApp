package com.example.myapplication.data.repository.contacts

import com.example.myapplication.domain.model.AddContactBody
import com.example.myapplication.domain.model.Contacts
import com.example.myapplication.domain.model.GetContactsResponse
import com.example.myapplication.domain.model.GetUsersResponse
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun getUserContacts(): Flow<Result<GetContactsResponse>>
    fun getAllUsers(): Flow<Result<GetUsersResponse>>
    fun deleteContact( contactId: Int): Flow<Result<GetContactsResponse>>
    fun addContact(
        requestBody: AddContactBody
    ): Flow<Result<GetContactsResponse>>
    fun getContactsToAdd(): Flow<Result<Contacts>>
    fun returnDeletedContact(): Flow<Result<GetContactsResponse>>?
}
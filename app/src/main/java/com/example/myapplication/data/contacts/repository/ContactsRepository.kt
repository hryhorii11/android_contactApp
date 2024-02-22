package com.example.myapplication.data.contacts.repository

import com.example.myapplication.domain.model.AddContactBody
import com.example.myapplication.domain.model.Contacts

import com.example.myapplication.domain.model.Users
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun fetch():Flow<Result<Contacts>>
    fun getUserContacts(): Flow<Result<Contacts>>
    fun deleteContact( contactId: Int): Flow<Result<Contacts>>
    fun addContact(
        requestBody: AddContactBody
    ): Flow<Result<Contacts>>
    fun getContactsToAdd(): Flow<Result<Users>>
    fun returnDeletedContact(): Flow<Result<Contacts>>?
}
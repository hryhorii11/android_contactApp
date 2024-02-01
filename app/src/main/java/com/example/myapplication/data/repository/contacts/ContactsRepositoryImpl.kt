package com.example.myapplication.data.repository.contacts

import android.util.Log
import com.example.myapplication.data.api.UserApi
import com.example.myapplication.data.repository.BaseRepository
import com.example.myapplication.data.repository.users.UsersRepositoryImpl
import com.example.myapplication.domain.model.AddContactBody
import com.example.myapplication.domain.model.GetContactsResponse
import com.example.myapplication.presentation.utils.Constants.AUTHORIZATION_HEADER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    private val remoteData: UserApi
) : ContactsRepository, BaseRepository() {
    private var lastDeletedContact: Int? = null

    override fun getUserContacts(): Flow<Result<GetContactsResponse>> {
        Log.i("puk", UsersRepositoryImpl.currentUser.toString())
        return doRequest {
            remoteData.getUserContacts(
                AUTHORIZATION_HEADER + UsersRepositoryImpl.currentUser.accessToken!!,
                UsersRepositoryImpl.currentUser.id.toInt()
            )
        }
    }

    override fun getAllUsers() =
        doRequest { remoteData.getAllUsers(AUTHORIZATION_HEADER + UsersRepositoryImpl.currentUser.accessToken!!) }

    override fun deleteContact(contactId: Int) =
        doRequest {
            remoteData.deleteContact(
                AUTHORIZATION_HEADER + UsersRepositoryImpl.currentUser.accessToken!!,
                UsersRepositoryImpl.currentUser.id.toInt(),
                contactId
            )
        }.also {
            lastDeletedContact = contactId
        }

    override fun addContact(requestBody: AddContactBody) =
        doRequest {
            remoteData.addContact(
                AUTHORIZATION_HEADER + UsersRepositoryImpl.currentUser.accessToken!!,
                UsersRepositoryImpl.currentUser.id.toInt(),
                requestBody
            )
        }

    override fun getContactsToAdd() = flow {
        val userContactsResponse = remoteData.getUserContacts(
            AUTHORIZATION_HEADER + UsersRepositoryImpl.currentUser.accessToken!!,
            UsersRepositoryImpl.currentUser.id.toInt()
        )
        val allContactsResponse =
            remoteData.getAllUsers(AUTHORIZATION_HEADER + UsersRepositoryImpl.currentUser.accessToken!!)
        val resultList = allContactsResponse.data.users?.filter {
            !userContactsResponse.data.contacts?.contains(it)!!
        }
        emit(Result.success(com.example.myapplication.domain.model.Contacts(resultList)))
    }.flowOn(Dispatchers.IO).catch { exception ->
        emit(Result.failure(Exception(exception.localizedMessage)))
    }

    override fun returnDeletedContact(): Flow<Result<GetContactsResponse>>? {
        if (lastDeletedContact != null) {
            return addContact(AddContactBody(lastDeletedContact!!)).also {
                lastDeletedContact = null
            }
        }
        return null
    }

}
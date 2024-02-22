package com.example.myapplication.data.contacts.repository

import com.example.myapplication.data.api.UserService
import com.example.myapplication.data.contacts.room.entity.ContactDbEntity
import com.example.myapplication.data.contacts.room.entity.UserContactsDbEntity
import com.example.myapplication.data.room.AppDatabase
import com.example.myapplication.data.users.repository.UsersRepositoryImpl
import com.example.myapplication.domain.model.AddContactBody
import com.example.myapplication.domain.model.Contacts
import com.example.myapplication.domain.model.Users
import com.example.myapplication.presentation.utils.Constants.AUTHORIZATION_HEADER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    private val remoteData: UserService,
    private val db: AppDatabase
) : ContactsRepository {
    private var lastDeletedContact: Int? = null

    private fun <T> doRequest(
        request: suspend () -> T,
        dbAction: (T) -> Unit,
        onNetworkError: suspend () -> T
    ) = flow {
        request().also { data ->
            dbAction(data)
            emit(Result.success(data))
        }
    }.flowOn(Dispatchers.IO).catch {
        emit(Result.success(onNetworkError()))
    }

    override fun fetch(): Flow<Result<Contacts>> {
        return flow {
            val contactsFromDb =
                db.getUsersDao()
                    .getUserContacts(UsersRepositoryImpl.currentUser.id.toInt()).contacts.map { it.toContact() }
            val contactsFromApi = remoteData.getUserContacts(
                AUTHORIZATION_HEADER + UsersRepositoryImpl.currentUser.accessToken,
                UsersRepositoryImpl.currentUser.id.toInt()
            ).data.contacts
            if (contactsFromApi != null) {
                contactsFromDb.filter { !contactsFromApi.contains(it) }.map {
                    addContact(AddContactBody(it.id))
                }
                contactsFromApi.filter { !contactsFromDb.contains(it) }.map {
                    deleteContact(it.id)
                }
            }
            emit(Result.success(Contacts(contactsFromDb)))
        }.flowOn(Dispatchers.IO).catch {
            emit(
                Result.success(
                    Contacts(db.getUsersDao()
                        .getUserContacts(UsersRepositoryImpl.currentUser.id.toInt()).contacts.map { it.toContact() })
                )
            )
        }
    }

    override fun getUserContacts(): Flow<Result<Contacts>> {
        return doRequest(
            request = {
                remoteData.getUserContacts(
                    AUTHORIZATION_HEADER + UsersRepositoryImpl.currentUser.accessToken!!,
                    UsersRepositoryImpl.currentUser.id.toInt()
                ).data
            },
            dbAction = {
                it.contacts?.let { it1 ->
                    db.getUsersDao().upsertUserContacts(it1.map { contact ->
                        UserContactsDbEntity(
                            UsersRepositoryImpl.currentUser.id.toInt(),
                            contact.id
                        )
                    })
                }
            },
            onNetworkError = {
                Contacts(
                    db.getUsersDao()
                        .getUserContacts(UsersRepositoryImpl.currentUser.id.toInt()).contacts.map {
                            it.toContact()
                        }
                )
            }
        )
    }

    override fun deleteContact(contactId: Int): Flow<Result<Contacts>> {
        lastDeletedContact = contactId
        return doRequest(
            request = {
                remoteData.deleteContact(
                    AUTHORIZATION_HEADER + UsersRepositoryImpl.currentUser.accessToken!!,
                    UsersRepositoryImpl.currentUser.id.toInt(),
                    contactId
                ).data
            },
            dbAction = {
                db.getUsersDao().deleteContact(
                    UserContactsDbEntity(
                        UsersRepositoryImpl.currentUser.id.toInt(),
                        contactId
                    )
                )
            },
            onNetworkError = {
                db.getUsersDao().deleteContact(
                    UserContactsDbEntity(
                        UsersRepositoryImpl.currentUser.id.toInt(),
                        contactId
                    )
                )
                Contacts(
                    db.getUsersDao()
                        .getUserContacts(UsersRepositoryImpl.currentUser.id.toInt()).contacts.map {
                            it.toContact()
                        })
            }
        )
    }

    override fun addContact(requestBody: AddContactBody): Flow<Result<Contacts>> {
        return doRequest(
            request = {
                remoteData.addContact(
                    AUTHORIZATION_HEADER + UsersRepositoryImpl.currentUser.accessToken!!,
                    UsersRepositoryImpl.currentUser.id.toInt(),
                    requestBody
                ).data
            },
            dbAction = {
                db.getUsersDao().upsertUserContacts(
                    UserContactsDbEntity(
                        UsersRepositoryImpl.currentUser.id.toInt(),
                        requestBody.contactId
                    )
                )
            },
            onNetworkError = {
                db.getUsersDao().upsertUserContacts(
                    UserContactsDbEntity(
                        UsersRepositoryImpl.currentUser.id.toInt(),
                        requestBody.contactId
                    )
                )
                Contacts(
                    db.getUsersDao()
                        .getUserContacts(UsersRepositoryImpl.currentUser.id.toInt()).contacts.map { it.toContact() })
            }
        )
    }

    override fun getContactsToAdd() = doRequest(
        request = {
            val userContacts = remoteData.getUserContacts(
                AUTHORIZATION_HEADER + UsersRepositoryImpl.currentUser.accessToken!!,
                UsersRepositoryImpl.currentUser.id.toInt()
            ).data.contacts
            Users(remoteData.getAllUsers(AUTHORIZATION_HEADER + UsersRepositoryImpl.currentUser.accessToken!!).data.users!!.filter {
                !userContacts!!.contains(it)
            })
        },
        dbAction = {
            it.users?.let { it1 ->
                db.getContactsDao()
                    .upsertContacts(it1.map { contact ->
                        ContactDbEntity.createEntity(contact)
                    })
            }
        },
        onNetworkError = {
            val userContacts = db.getUsersDao()
                .getUserContacts(UsersRepositoryImpl.currentUser.id.toInt()).contacts
            Users(db.getContactsDao().getAllContacts().filter {
                !userContacts.contains(
                    it
                )
            }.map { it.toContact() })
        }
    )

    override fun returnDeletedContact(): Flow<Result<Contacts>>? {
        if (lastDeletedContact != null) {
            return addContact(AddContactBody(lastDeletedContact!!)).also {
                lastDeletedContact = null
            }
        }
        return null
    }

}
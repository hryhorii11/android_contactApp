package com.example.myapplication.data.contacts.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.myapplication.data.contacts.room.entity.ContactDbEntity
import com.example.myapplication.data.contacts.room.entity.UserContactsDbEntity
import com.example.myapplication.data.contacts.room.entity.UserContactsTuple
import com.example.myapplication.domain.model.Contacts

@Dao
interface ContactDao {

    @Query("select * from contacts")
    suspend fun getAllContacts(): List<ContactDbEntity>

    @Upsert
    fun upsertContacts(contacts: List<ContactDbEntity>)

    @Insert
    fun addUserContact(contact: UserContactsDbEntity)

}


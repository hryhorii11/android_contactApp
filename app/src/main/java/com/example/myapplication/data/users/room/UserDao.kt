package com.example.myapplication.data.users.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.myapplication.data.contacts.room.entity.UserContactsDbEntity
import com.example.myapplication.data.contacts.room.entity.UserContactsTuple
import com.example.myapplication.data.users.room.entity.UserDbEntity

@Dao
interface UserDao {

    @Upsert
    fun upsertUser(user:UserDbEntity)

    @Transaction
    @Query("select * from users where users.id=:userId")
    fun getUserContacts(userId:Int): UserContactsTuple

    @Query("select * from users where users.id=:userId")
    fun getUser(userId: Int):UserDbEntity
    @Upsert
    fun upsertUserContacts(userContact: UserContactsDbEntity)
    @Upsert
    fun upsertUserContacts(userContacts: List<UserContactsDbEntity>)
    @Delete
    fun deleteContact(userContact: UserContactsDbEntity)
}
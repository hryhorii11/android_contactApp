package com.example.myapplication.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.contacts.room.ContactDao
import com.example.myapplication.data.contacts.room.UserContactsDao
import com.example.myapplication.data.contacts.room.entity.ContactDbEntity
import com.example.myapplication.data.contacts.room.entity.UserContactsDbEntity
import com.example.myapplication.data.users.room.UserDao
import com.example.myapplication.data.users.room.entity.UserDbEntity

@Database(
    version = 1,
    entities = [
        ContactDbEntity::class,
        UserDbEntity::class,
        UserContactsDbEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getContactsDao(): ContactDao
    abstract fun getUsersDao():UserDao
    abstract fun getUserContactsDao():UserContactsDao
}


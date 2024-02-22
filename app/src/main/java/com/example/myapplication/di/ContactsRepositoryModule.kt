package com.example.myapplication.di

import com.example.myapplication.data.api.UserService
import com.example.myapplication.data.contacts.repository.ContactsRepository
import com.example.myapplication.data.contacts.repository.ContactsRepositoryImpl
import com.example.myapplication.data.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ContactsRepositoryModule {
    @Provides
    @Singleton
    fun provideUsersContactsRepository(mainService: UserService, db:AppDatabase): ContactsRepository =
        ContactsRepositoryImpl(mainService,db)

}
package com.example.myapplication.di

import com.example.myapplication.data.api.UserApi
import com.example.myapplication.data.repository.contacts.ContactsRepository
import com.example.myapplication.data.repository.contacts.ContactsRepositoryImpl
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
    fun provideUsersContactsRepository(mainService: UserApi): ContactsRepository=ContactsRepositoryImpl(mainService)

}
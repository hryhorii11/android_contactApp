package com.example.myapplication.di

import com.example.myapplication.data.api.UserApi
import com.example.myapplication.data.repository.users.UsersRepository
import com.example.myapplication.data.repository.users.UsersRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UsersRepositoryModule {
    @Provides
    @Singleton
    fun provideUsersRepository(mainService: UserApi): UsersRepository =
        UsersRepositoryImpl(mainService)
}
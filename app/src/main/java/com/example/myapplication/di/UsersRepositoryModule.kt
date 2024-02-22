package com.example.myapplication.di

import com.example.myapplication.data.api.UserService
import com.example.myapplication.data.datastore.DataStorePreferences
import com.example.myapplication.data.room.AppDatabase
import com.example.myapplication.data.users.repository.UsersRepository
import com.example.myapplication.data.users.repository.UsersRepositoryImpl
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
    fun provideUsersRepository(
        mainService: UserService,
        db: AppDatabase,
        dataStorePreferences: DataStorePreferences
    ): UsersRepository =
        UsersRepositoryImpl(mainService, db,dataStorePreferences)
}
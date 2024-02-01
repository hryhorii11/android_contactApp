//package com.example.myapplication.di
//
//import com.example.myapplication.data.api.UserApi
//import com.example.myapplication.data.remote.UserRemoteData
//import com.example.myapplication.data.remote.UserRemoteDataImpl
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//class RemoteData  {
//
//    @Provides
//    @Singleton
//    fun provideUserRemoteData(mainService:UserApi):UserRemoteData=UserRemoteDataImpl(mainService)
//}
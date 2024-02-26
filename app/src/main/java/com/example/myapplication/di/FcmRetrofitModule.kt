package com.example.myapplication.di


import com.example.myapplication.data.api.FcmService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FcmRetrofitModule {

    @Provides
    @Named("Retrofit2")
    @Singleton
    fun provideRetrofit(client:OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl(FcmService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    @Singleton
    fun provideApi(@Named("Retrofit2") retrofit: Retrofit): FcmService =
        retrofit.create(FcmService::class.java)


}
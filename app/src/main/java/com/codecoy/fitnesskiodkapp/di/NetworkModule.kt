package com.codecoy.fitnesskiodkapp.di

import com.codecoy.fitnesskiodkapp.retrofit.RetrofitAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideApiInterface(): RetrofitAPI = RetrofitAPI.getInstance()

}
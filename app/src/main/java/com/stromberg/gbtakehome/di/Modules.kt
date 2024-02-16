package com.stromberg.gbtakehome.di

import com.stromberg.gbtakehome.interfaces.GuideHelper
import com.stromberg.gbtakehome.GuideHelperImpl
import com.stromberg.gbtakehome.network.GuideService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebServiceModule {
    @Provides
    @Singleton
    fun getGuideWebService(): GuideService =
        Retrofit.Builder()
            .baseUrl(GuideService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GuideService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class GuideModule {
    @Binds
    abstract fun bindGuideHelper(
        guideHelperImpl: GuideHelperImpl
    ): GuideHelper
}
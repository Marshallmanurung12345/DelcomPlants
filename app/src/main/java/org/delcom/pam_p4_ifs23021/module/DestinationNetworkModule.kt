package org.delcom.pam_p4_ifs23021.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.delcom.pam_p4_ifs23021.network.destinations.DestinationApi
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DestinationNetworkModule {
    @Provides
    @Singleton
    fun provideDestinationApi(retrofit: Retrofit): DestinationApi =
        retrofit.create(DestinationApi::class.java)
}
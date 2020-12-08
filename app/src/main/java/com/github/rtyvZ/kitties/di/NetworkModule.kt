package com.github.rtyvZ.kitties.di

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.Strings
import com.github.rtyvZ.kitties.network.NetworkResponseFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    internal fun provideHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    internal fun providesApiService(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        networkResponseFactory: NetworkResponseFactory
    ): Api {
        return Retrofit.Builder()
            .baseUrl(Strings.BASE_URL)
            .client(client)
            .addCallAdapterFactory(networkResponseFactory)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(Api::class.java)
    }

    @Provides
    @Singleton
    internal fun providesGsonClient(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    internal fun providesGson(): Gson {
        val gsonBuilder = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        return gsonBuilder.create()
    }

    @Provides
    internal fun providesNetworkResponseFactory(): NetworkResponseFactory {
        return NetworkResponseFactory()
    }

}
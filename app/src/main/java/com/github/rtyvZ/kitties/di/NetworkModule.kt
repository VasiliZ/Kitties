package com.github.rtyvZ.kitties.di

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.Strings
import com.github.rtyvZ.kitties.common.UserInternalStorageContract
import com.github.rtyvZ.kitties.common.cryptography.Cryptographer
import com.github.rtyvZ.kitties.common.models.UserSession
import com.github.rtyvZ.kitties.network.NetworkResponseFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    internal fun provideHttpClient(
        interceptor: HttpLoggingInterceptor,
        storage: UserInternalStorageContract
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val cryptographer = Cryptographer<String, String>()
                    var key = ""
                    var secret = ""
                    if (storage.hasSession()) {
                        storage.restoreSession()
                        storage.getSession()?.let {
                            key = it.encryptedKey
                            secret = it.secretKey
                        }
                    }
                    val apikey =
                        cryptographer.decrypt(key, secret)
                    val request =
                        chain.request().newBuilder()
                            .addHeader("x-api-key", apikey.toString()).build()
                    return chain.proceed(request)
                }

            })
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
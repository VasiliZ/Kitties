package com.github.rtyvZ.kitties.di

import android.content.Context
import androidx.room.Room
import com.github.rtyvZ.kitties.common.*
import com.github.rtyvZ.kitties.common.cryptography.Cryptographer
import com.github.rtyvZ.kitties.db.CatDatabase
import com.github.rtyvZ.kitties.network.NetworkResponseFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideHttpClient(
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
    fun provideInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    fun providesApiService(
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
    fun providesGson(): Gson {
        val gsonBuilder = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        return gsonBuilder.create()
    }

    @Provides
    fun providesNetworkResponseFactory(): NetworkResponseFactory {
        return NetworkResponseFactory()
    }

    @Provides
    @Singleton
    fun providesDB(@ApplicationContext context: Context): CatDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CatDatabase::class.java,
            Strings.Const.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserStorage(prefs: AppPreference): UserInternalStorageContract {
        return SessionStorage(prefs)
    }

    @Provides
    @PreferenceInfo
    internal fun providePreferenceFileName(): String {
        return Strings.PreferenceConst.AUTH_USER_PREFERENCE
    }

    @Provides
    @Singleton
    fun providePrefs(prefs: AppPreference): PreferenceHelper {
        return prefs
    }

    @Provides
    @Singleton
    fun providesFireBase(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun providesExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }
}
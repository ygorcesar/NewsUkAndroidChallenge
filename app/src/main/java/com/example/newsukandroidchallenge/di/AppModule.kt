package com.example.newsukandroidchallenge.di

import android.content.Context
import android.content.SharedPreferences
import com.example.newsukandroidchallenge.data.api.StackOverflowApi
import com.example.newsukandroidchallenge.data.repository.UserRepositoryImpl
import com.example.newsukandroidchallenge.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://api.stackexchange.com/2.2/"
    private const val PREFS_NAME = "stackoverflow_prefs"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideStackOverflowApi(retrofit: Retrofit): StackOverflowApi {
        return retrofit.create(StackOverflowApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        api: StackOverflowApi,
        prefs: SharedPreferences
    ): UserRepository {
        return UserRepositoryImpl(api, prefs)
    }
}

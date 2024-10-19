package com.example.wingsdatingapp.di

import android.content.Context
import androidx.room.Room
import com.example.wingsdatingapp.api.retrofit.UserService
import com.example.wingsdatingapp.local.storage.room.db.UserDataModelDao
import com.example.wingsdatingapp.local.storage.room.db.UserInfoDatabase
import com.example.wingsdatingapp.local.storage.room.db.UserMatchDao
import com.example.wingsdatingapp.model.UserMatchEntity
import com.example.wingsdatingapp.utils.BASE_URL
import com.example.wingsdatingapp.utils.CLIENT_ID
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideKtorClient(): HttpClient {
        return HttpClient(CIO) {
            install(WebSockets)
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
    }

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ): UserInfoDatabase {
        return Room.databaseBuilder(
            app,
            UserInfoDatabase::class.java,
            "UserInfoDB"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideDao(userInfoDatabase: UserInfoDatabase):UserDataModelDao{
        return userInfoDatabase.userDataModelDao()
    }


    @Singleton
    @Provides
    fun provideUserMatch(userInfoDatabase: UserInfoDatabase):UserMatchDao{
        return userInfoDatabase.userMatch()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
}
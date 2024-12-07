package com.example.wingsdatingapp.ui_screens.dating.screens.settings.hobbies

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideHobbiesDataStore(@ApplicationContext context: Context): HobbiesDataStore {
        return HobbiesDataStore(context)
    }
}

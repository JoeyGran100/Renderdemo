package com.example.wingsdatingapp.ui_screens.dating.screens.settings.hobbies

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// TODO: I created this to store the data locally on the phone, so when user picks a hobbie/hobbies it will remain selected after the user terminates the phone.
// TODO: This is achieved using Datastore.

// Extension function to create a DataStore instance
val Context.dataStore by preferencesDataStore(name = "hobbies_data_store")

class HobbiesDataStore(private val context: Context) {

    // Key to store the selected hobbies
    private val SELECTED_HOBBIES_KEY = stringSetPreferencesKey("selected_hobbies_key")

    // Save selected hobbies in the DataStore
    suspend fun saveSelectedHobbies(hobbies: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_HOBBIES_KEY] = hobbies
        }
    }

    // Retrieve the selected hobbies from DataStore
    val selectedHobbiesFlow: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_HOBBIES_KEY] ?: emptySet()
        }
}

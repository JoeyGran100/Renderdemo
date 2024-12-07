package com.example.wingsdatingapp.ui_screens.dating.screens.settings.hobbies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wingsdatingapp.ui_screens.dating.screens.settings.hobbies.HobbiesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: This works as intended
@HiltViewModel
class HobbiesViewModel @Inject constructor(
    private val hobbiesDataStore: HobbiesDataStore
) : ViewModel() {

    private val _selectedHobbies = MutableStateFlow<List<String>>(emptyList())
    val selectedHobbies: StateFlow<List<String>> = _selectedHobbies.asStateFlow()

    init {
        // Load saved hobbies when the ViewModel is created
        viewModelScope.launch {
            hobbiesDataStore.selectedHobbiesFlow.collect { savedHobbies ->
                _selectedHobbies.value = savedHobbies.toList()
            }
        }
    }

    // Toggle the selection of a hobby and save it in DataStore
    fun toggleHobby(hobbyName: String) {
        val currentHobbies = _selectedHobbies.value.toMutableList()

        if (currentHobbies.contains(hobbyName)) {
            currentHobbies.remove(hobbyName)
        } else {
            currentHobbies.add(hobbyName)
        }

        // Update the StateFlow
        _selectedHobbies.value = currentHobbies

        // Save the updated hobbies to DataStore
        viewModelScope.launch {
            hobbiesDataStore.saveSelectedHobbies(currentHobbies.toSet())
        }
    }
}





/*// Original Code from Umair
class HobbiesViewModel:ViewModel() {
    private val _selectedHobbies = MutableStateFlow<List<String>>(emptyList())
    val selectedHobbies: StateFlow<List<String>> = _selectedHobbies

    // Toggle the selection of an interest based on its name
    fun toggleHobby(hobbyName: String) {
        val currentHobbies = _selectedHobbies.value.toMutableList()

        if (currentHobbies.contains(hobbyName)) {
            // If the hobby is already selected, remove it from the list (deselect it)
            currentHobbies.remove(hobbyName)
        } else {
            // If the hobby is not selected, add it to the list
            currentHobbies.add(hobbyName)
        }

        // Update the StateFlow with the modified list
        _selectedHobbies.value = currentHobbies
    }
}*/

package com.example.wingsdatingapp.ui_screens.dating.screens.settings.hobbies.viewmodel

import androidx.lifecycle.ViewModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.user_interest.model.UserInterestModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
}
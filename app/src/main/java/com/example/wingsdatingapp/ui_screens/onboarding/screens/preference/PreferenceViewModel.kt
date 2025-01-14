package com.example.wingsdatingapp.ui_screens.onboarding.screens.preference

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wingsdatingapp.api.NetworkResult
import com.example.wingsdatingapp.model.UserPreferenceModel
import com.example.wingsdatingapp.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// I created this 2025/01/05
@HiltViewModel
class PreferenceViewModel @Inject constructor(
    private val repo: Repo,
) : ViewModel() {

    private val _userPreferenceDetails = MutableStateFlow<NetworkResult<UserPreferenceModel>>(NetworkResult.Loading)
    val userPreferenceDetails: StateFlow<NetworkResult<UserPreferenceModel>> = _userPreferenceDetails

    fun postUserPreferenceData(userData: UserPreferenceModel, handleResult: (NetworkResult<String>) -> Unit) {
        viewModelScope.launch {
            handleResult(NetworkResult.Loading)
            try {
                val result = repo.postUserPreference(userData)
                if (result.isSuccessful) {
                    handleResult(NetworkResult.Success("User Preference details added successfully"))
                } else {
                    handleResult(NetworkResult.Error("Preference already taken or invalid"))
                }
            } catch (e: Exception) {
                handleResult(NetworkResult.Error("Something went wrong"))
                e.printStackTrace()
            }
        }
    }
}

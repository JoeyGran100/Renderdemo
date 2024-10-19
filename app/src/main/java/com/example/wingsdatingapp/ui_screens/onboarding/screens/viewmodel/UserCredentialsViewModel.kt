package com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wingsdatingapp.api.NetworkResult
import com.example.wingsdatingapp.model.UserAuthModel
import com.example.wingsdatingapp.model.UserDataModel
import com.example.wingsdatingapp.model.UserImageModel
import com.example.wingsdatingapp.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.call.body
import io.ktor.http.contentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class UserCredentialsViewModel @Inject constructor(private val repo: Repo) : ViewModel() {
    private val _postUserImageState = MutableStateFlow<NetworkResult<Any>>(NetworkResult.Loading)
    val postUserImageState: StateFlow<NetworkResult<Any>> = _postUserImageState
    fun postUserCredentials(
        context: Context,
        userAuthModel: UserAuthModel,
        handleResult: (NetworkResult<String>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Show loading state
                handleResult(NetworkResult.Loading)

                // Call your repository function
                val result = repo.postUserCredentials(userAuthModel)

                // Handle success or error based on the result
                if (result.isSuccessful) {
                    handleResult(NetworkResult.Success("User added successfully"))
                } else {
                    handleResult(NetworkResult.Success(""))
                    Toast.makeText(
                        context,
                        "Email already taken or Invalid email format",
                        Toast.LENGTH_SHORT
                    ).show()
//                    handleResult(NetworkResult.Error("Email already taken or Invalid email format"))
                }
            } catch (e: Exception) {
                Log.e("myerror", e.message.toString())
                handleResult(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    fun signInUser(
        userAuthModel: UserAuthModel,
        handleResult: (NetworkResult<String>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                handleResult(NetworkResult.Loading)

                val result = repo.signInUser(userAuthModel)

                if (result.isSuccessful) {
                    handleResult(NetworkResult.Success("User login successfully"))
                } else {
                    handleResult(NetworkResult.Error("Invalid credentials"))
                }
            } catch (e: Exception) {
                handleResult(NetworkResult.Error(e.message.toString()))
                e.printStackTrace()
            }
        }
    }

    fun postUserData(
        context: Context, userData: UserDataModel,
        handleResult: (NetworkResult<String>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                handleResult(NetworkResult.Loading)

                val result = repo.postUserData(userData)
                if (result.isSuccessful) {
                    handleResult(NetworkResult.Success("User details added successfully"))
                    Toast.makeText(context, "User details added successfully", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    handleResult(NetworkResult.Error("Something went wrong"))
                }

            } catch (e: Exception) {
                handleResult(NetworkResult.Error("Something went wrong"))
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
                Log.d("postDataError", e.message.toString())
                e.printStackTrace()
            }
        }
    }

    fun postUserImage(
        file: File,
        email: String,
        onSuccess: (String?) -> Unit,
        onFailure: (String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                repo.postUserImage(file, email, onSuccess, onFailure)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
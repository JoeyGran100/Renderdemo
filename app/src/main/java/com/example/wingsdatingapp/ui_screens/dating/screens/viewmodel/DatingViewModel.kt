package com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wingsdatingapp.api.NetworkResult
import com.example.wingsdatingapp.model.MessageModel
import com.example.wingsdatingapp.model.UserDataModel
import com.example.wingsdatingapp.model.UserImageModel
import com.example.wingsdatingapp.model.UsersModel
import com.example.wingsdatingapp.repo.Repo
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserDetailHiltViewModel
import com.example.wingsdatingapp.utils.selectedItemIndex

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DatingViewModel @Inject constructor(private val repo:Repo,
                                          ):ViewModel() {
    private val _userDetails = MutableStateFlow<NetworkResult<UsersModel>>(NetworkResult.Loading)
    val userDetails: StateFlow<NetworkResult<UsersModel>> = _userDetails

    private val _userImage = MutableLiveData<UserImageModel>()
    val userImage: LiveData<UserImageModel> = _userImage

    private val _chatMessages = MutableLiveData<NetworkResult<List<MessageModel>>>()
    val chatMessages: LiveData<NetworkResult<List<MessageModel>>> get() = _chatMessages

    private val _postUserImageState = MutableStateFlow<NetworkResult<Any>>(NetworkResult.Loading)
    val postUserImageState: StateFlow<NetworkResult<Any>> = _postUserImageState

    private val _messages = MutableStateFlow<List<MessageModel>>(emptyList())
    val messages: StateFlow<List<MessageModel>> = _messages

    init {
        fetchUserDetails()
        getUserImage(selectedItemIndex)
//        getChatsBetweenUsers(
//            emailOne = "umair@gmail.com",
//            emailTwo ="sumaiya@gmail.com"
//        )
    }
fun reloadChat(emailOne:String,emailTwo:String){
    getChatsBetweenUsers(
        emailOne = emailOne,
        emailTwo =emailTwo
    )
}
    private fun fetchUserDetails() {
        viewModelScope.launch {
            _userDetails.value = NetworkResult.Loading
            try {
                val response = repo.getUserDetails()
                if (response.isSuccessful) {
                    response.body()?.let { userData ->
                        _userDetails.value = NetworkResult.Success(userData)
                    } ?: run {
                        _userDetails.value = NetworkResult.Error("Response body is null")
                    }
                } else {
                    _userDetails.value = NetworkResult.Error("Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.d("DatingViewModel", e.message.toString())
                _userDetails.value = NetworkResult.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun retry(){
        fetchUserDetails()
    }
    fun getUserImage(userId: Int) {
        viewModelScope.launch {
            try {
                val response = repo.getUserImage(userId)
                if (response.isSuccessful) {
                    response.body()?.let { userImage ->
                        _userImage.value = userImage
                        Log.d("UserImage", "Successful: ${userImage.imageString}")
                    } ?: run {
                        Log.d("UserImage", "Response body is null")
                    }
                } else {
                    Log.d("UserImage", "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.d("UserImage", "Exception: ${e.message.toString()}")
            }
        }
    }





    fun getChatsBetweenUsers(emailOne: String, emailTwo: String) {
        viewModelScope.launch {
            _chatMessages.value = NetworkResult.Loading // Indicate loading state

            try {
                // Perform network request asynchronously
                val response = async { repo.getUserChats(emailOne, emailTwo) }

                // Await the network response
                val chatResponse = response.await()

                // Check if the response is successful and body is not null
                if (chatResponse.isSuccessful && chatResponse.body() != null) {
                    // Update the state with the fetched messages
                    _chatMessages.value = NetworkResult.Success(chatResponse.body()!!)
                    _messages.value = chatResponse.body()!!
                } else {
                    // Handle error response from the server
                    _chatMessages.value = NetworkResult.Error("Error: ${chatResponse.message()}")
                }
            } catch (e: Exception) {
                // Handle any exceptions that may occur during the network request
                _chatMessages.value = NetworkResult.Error("Exception: ${e.message}")
            }
        }
    }


    fun sendMessage(message: MessageModel) {
            viewModelScope.launch {
                try {
                    repo.sendMessage(message)
                    Log.d("chat_feature", "Success")
                } catch (e: Exception) {
                    Log.d("chat_feature", "Error: ${e.message}")
                    e.printStackTrace()
                }
            }
    }


    fun receiveMessages(userId: Int,onMessageReceive:(MessageModel)->Unit) {
        viewModelScope.launch {
            repo.receiveMessages(userId) { message ->
                Log.d("chat_feature","Message: ${message.toString()}")
             onMessageReceive(message)
            }
        }
    }
}
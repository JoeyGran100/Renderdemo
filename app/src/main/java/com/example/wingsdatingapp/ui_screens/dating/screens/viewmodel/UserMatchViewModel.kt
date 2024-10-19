package com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wingsdatingapp.api.NetworkResult
import com.example.wingsdatingapp.local.storage.room.db.UserMatchDao
import com.example.wingsdatingapp.model.MessageModel
import com.example.wingsdatingapp.model.UserMatchEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserMatchViewModel @Inject constructor(private val userMatchDao: UserMatchDao) : ViewModel() {

    private val _matchUsers = MutableStateFlow<List<UserMatchEntity>>(emptyList())
    val matchUsers: StateFlow<List<UserMatchEntity>> get() = _matchUsers

    init {
        Log.d("UserMatchViewModel", "ViewModel initialized, fetching users")
        getMatchUser()
    }

    fun addUser(user: UserMatchEntity) {
        viewModelScope.launch {
            try {
                Log.d("UserMatchViewModel", "Adding user: $user")
                userMatchDao.addUser(user)
                _matchUsers.value += user
                Log.d("UserMatchViewModel", "User added successfully")
            } catch (e: Exception) {
                Log.e("UserMatchViewModel", "Error adding user: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun deleteUserById(email: String) {
        viewModelScope.launch {
            try {
                Log.d("UserMatchViewModel", "Deleting user with ID: $email")
                userMatchDao.deleteUserByEmail(email)
                _matchUsers.value = _matchUsers.value.filterNot { it.email == email }
                Log.d("UserMatchViewModel", "User deleted successfully")
            } catch (e: Exception) {
                Log.e("UserMatchViewModel", "Error deleting user: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun getMatchUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("UserMatchViewModel", "Fetching all users")
                val users = userMatchDao.getAllUsers() // Fetch the users on the background thread
                _matchUsers.emit(users) // Use emit to update StateFlow
                Log.d("UserMatchViewModel", "Users fetched successfully: ${users.size} users found")
            } catch (e: Exception) {
                Log.e("UserMatchViewModel", "Error fetching users: ${e.message}")
                e.printStackTrace()
            }
        }
    }

}

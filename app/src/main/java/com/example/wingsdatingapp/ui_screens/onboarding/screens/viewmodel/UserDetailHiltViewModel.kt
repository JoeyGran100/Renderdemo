package com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wingsdatingapp.local.storage.room.db.UserDataModelDao
import com.example.wingsdatingapp.model.UserDataModel
import com.example.wingsdatingapp.model.UserImageModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailHiltViewModel @Inject constructor(private val userDataModelDao: UserDataModelDao) :
    ViewModel() {
    private val _loggedInUser = MutableLiveData<UserDataModel?>()
    val loggedInUser: LiveData<UserDataModel?> get() = _loggedInUser
    fun insertUser(user: UserDataModel) {
        viewModelScope.launch {
            try {
                userDataModelDao.insertUser(user)
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
fun deleteUser(){
    viewModelScope.launch {
        try {
            userDataModelDao.deleteAllUsers()
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }
}
    // Update an existing user
    fun updateUser(user: UserDataModel) {
        viewModelScope.launch {
            _loggedInUser.postValue(user)
            userDataModelDao.updateUser(user)
        }
    }

    // Get a user by their ID
    fun getUserById(userId: Int) {
        viewModelScope.launch {
            val user = userDataModelDao.getUserById(userId)
        }
    }

    fun getLoggedInUser() {
        viewModelScope.launch {
            try {
                val user = userDataModelDao.getLoggedInUser()
                _loggedInUser.postValue(user)
            } catch (e: Exception) {
                _loggedInUser.postValue(null) // or handle the exception as needed
            }
        }
    }

}
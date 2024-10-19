package com.example.wingsdatingapp.api

import com.example.wingsdatingapp.model.UserDataModel

sealed class NetworkResult<out T> {
    data object Loading : NetworkResult<Nothing>()
    data class Success<out T>(var data: @UnsafeVariance T) : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
}
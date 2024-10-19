package com.example.wingsdatingapp.model

import kotlinx.serialization.Serializable

@Serializable
data class UsersModel(val users:List<UserDataModel>)
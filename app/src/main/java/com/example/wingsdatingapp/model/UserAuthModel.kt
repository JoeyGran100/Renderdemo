package com.example.wingsdatingapp.model

import kotlinx.serialization.Serializable

@Serializable
data class UserAuthModel(val email:String?=null, val password:String)

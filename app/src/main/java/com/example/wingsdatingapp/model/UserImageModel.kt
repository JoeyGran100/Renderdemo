package com.example.wingsdatingapp.model

import kotlinx.serialization.Serializable

@Serializable
data class UserImageModel(val email:String,val imageString:String,val id:Int?=null,val user_auth_id:Int?=null)

package com.example.wingsdatingapp.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Keep
@Entity(tableName = "UserInfoDB", indices = [Index(value = ["id"], unique = true)])
@Serializable
data class UserDataModel(
    @PrimaryKey
   val id:Int?=null,
    val name:String?=null,
    val email:String?=null,
    val gender:String?=null,
    val hobbies:List<String>?=null,
    val image_url:String?=null,
    val phone_number:String?=null,
    val age:String?=null,
    val bio:String?=null
)

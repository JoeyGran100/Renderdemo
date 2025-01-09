package com.example.wingsdatingapp.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import kotlinx.serialization.Serializable


// I added this 2025/01/05
//@Keep
//@Entity(tableName = "UserPreference", indices = [Index(value = ["id"], unique = true)])
@Serializable
data class UserPreferenceModel (
    val preference:String?=null,
)
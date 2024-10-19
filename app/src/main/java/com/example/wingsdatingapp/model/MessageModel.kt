package com.example.wingsdatingapp.model

import kotlinx.serialization.Serializable

@Serializable
data class MessageModel(
//    val sender_id: Int?,
//    val receiver_id: Int?,
    val receiver_email: String?="",
    val sender_email: String?="",
    val message: String?="",
    val timestamp: String? = null
)

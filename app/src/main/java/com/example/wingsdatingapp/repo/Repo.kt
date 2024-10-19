package com.example.wingsdatingapp.repo

import com.example.wingsdatingapp.api.UserApi
import com.example.wingsdatingapp.model.MessageModel
import com.example.wingsdatingapp.model.UserAuthModel
import com.example.wingsdatingapp.model.UserDataModel
import com.example.wingsdatingapp.model.UserImageModel
import com.example.wingsdatingapp.model.UsersModel
import io.ktor.client.statement.HttpResponse
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class Repo @Inject constructor(private val userApi: UserApi) {

    suspend fun postUserCredentials(data: UserAuthModel): Response<Void> {
        return userApi.postUserCredentials(data)
    }

    suspend fun signInUser(data: UserAuthModel): Response<Void> {
        return userApi.signInUser(data)
    }

    suspend fun postUserData(data: UserDataModel): Response<Void> {
        return userApi.postUserData(data)
    }

    suspend fun sendMessage(message: MessageModel) {
        userApi.sendMessage(message)
    }

    fun receiveMessages(userId: Int, onMessageReceived: (MessageModel) -> Unit) {
        userApi.receiveMessages(userId, onMessageReceived)
    }

    suspend fun getUserDetails(): Response<UsersModel> {
        return userApi.getUserData()
    }

     fun postUserImage(file: File, email: String,onSuccess: (String?) -> Unit,onFailure: (String?) -> Unit) {
         userApi.postUserImage(file,email,onSuccess,onFailure)
    }

    suspend fun getUserImage(userId: Int): Response<UserImageModel> {
        return userApi.getUserImage(userId)
    }
    suspend fun getUserChats(emailOne:String,emailTwo:String):Response<List<MessageModel>> {
        return userApi.getChats(emailOne,emailTwo)
    }
}



//class Repo @Inject constructor(private val userApi: UserApi) {
//    suspend fun postUserCredentials(data: UserAuthModel): HttpResponse =
//        userApi.postUserCredentials(data)
//
//    suspend fun signInUser(data: UserAuthModel): HttpResponse = userApi.signInUser(data)
//
//    suspend fun postUserData(data: UserDataModel): HttpResponse = userApi.postUserData(data)
//
//    suspend fun sendMessage(message: MessageModel) {
//        userApi.sendMessage(message)
//    }
//
//    suspend fun receiveMessages(userId: Int, onMessageReceived: (String) -> Unit) {
//        userApi.receiveMessages(userId, onMessageReceived)
//    }
//
//    suspend fun getUserDetails(): UsersModel {
//        return userApi.getUserData()
//    }
//
//    suspend fun postUserImage(data: UserImageModel): HttpResponse {
//        return userApi.postUserImage(data)
//    }
//
//    suspend fun getUserImage(userId: Int): UserImageModel {
//        return userApi.getUserImage(userId)
//    }
//}
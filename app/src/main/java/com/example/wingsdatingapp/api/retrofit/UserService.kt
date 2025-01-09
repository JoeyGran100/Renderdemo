package com.example.wingsdatingapp.api.retrofit

import androidx.datastore.preferences.protobuf.Timestamp
import com.example.wingsdatingapp.model.MessageModel
import com.example.wingsdatingapp.model.UserAuthModel
import com.example.wingsdatingapp.model.UserDataModel
import com.example.wingsdatingapp.model.UserImageModel
import com.example.wingsdatingapp.model.UserPreferenceModel
import com.example.wingsdatingapp.model.UsersModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDateTime

interface UserService {
    @POST("users")
    suspend fun postUserCredentials(@Body data: UserAuthModel): Response<Void>

    @POST("sign-in")
    suspend fun signInUser(@Body data: UserAuthModel): Response<Void>

    @POST("userData")
    suspend fun postUserData(@Body data: UserDataModel): Response<Void>

    // I added this 2025/01/05
    @POST("preference")
    suspend fun postUserPreference(@Body data: UserPreferenceModel): Response<Void>

    @Multipart
    @POST("/upload_image")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("email") email: RequestBody
    ): Call<ResponseBody>

    @GET("get_image/{userId}")
    suspend fun getUserImage(@Path("userId") userId: Int): Response<UserImageModel>

    @GET("userData")
    suspend fun getUserData(): Response<UsersModel>

    @GET("get_chats")
    suspend fun getChats(
        @Query("email1") senderEmail: String,
        @Query("email2") receiverEmail: String
    ): Response<List<MessageModel>>

    @FormUrlEncoded
    @POST("send_message")
    suspend fun sendMessage(
        @Field("receiver_email") receiverEmail: String,
        @Field("sender_email") senderEmail: String,
        @Field("message") message: String,

        // I added timestamp
        @Field("timestamp") timestamp: String

    ): Response<Void>

//    // I added this
//    @GET("send_message")
//    suspend fun getTimeStamp(
//        @Field("timestamp") time: LocalDateTime
//    )

}
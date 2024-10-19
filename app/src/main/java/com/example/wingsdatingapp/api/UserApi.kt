package com.example.wingsdatingapp.api

import android.service.autofill.UserData
import android.util.Log
import com.example.wingsdatingapp.api.retrofit.UserService
import com.example.wingsdatingapp.api.retrofit.WebSocketManager
import com.example.wingsdatingapp.model.MessageModel
import com.example.wingsdatingapp.model.UserAuthModel
import com.example.wingsdatingapp.model.UserDataModel
import com.example.wingsdatingapp.model.UserImageModel
import com.example.wingsdatingapp.model.UsersModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.navigation.NavigationItem
import com.example.wingsdatingapp.utils.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class UserApi @Inject constructor(private val userService: UserService,
                                  private val webSocketManager: WebSocketManager
) {

    suspend fun postUserCredentials(data: UserAuthModel): Response<Void> {
        return userService.postUserCredentials(data)
    }

    suspend fun signInUser(data: UserAuthModel): Response<Void> {
        return userService.signInUser(data)
    }

    suspend fun postUserData(data: UserDataModel): Response<Void> {
        return userService.postUserData(data)
    }

    fun postUserImage(
        file: File,
        email: String,
        onSuccess: (String?) -> Unit,
        onFailure: (String?) -> Unit
    ) {
        val requestFile = RequestBody.create(
            "image/*".toMediaTypeOrNull(),
            file
        )

        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        val emailBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)

        val call = userService.uploadImage(body, emailBody)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Assuming your server sends the image URL in the response body as JSON
                    val responseString = response.body()?.string()
                    try {
                        val jsonObject = JSONObject(responseString.toString())
                        val imageUrl = jsonObject.optString("image_url") // Adjust based on your API structure

                        // Call the success lambda and pass the image URL
                        onSuccess(imageUrl)

                        Log.d("Upload", "Image upload successful! URL: $imageUrl")
                    } catch (e: JSONException) {
                        // Handle JSON parsing error
                        Log.e("Upload", "Error parsing JSON: ${e.message}")
                        onFailure("Error parsing server response.")
                    }
                } else {
                    // Call the failure lambda and pass the error message
                    Log.e("Upload", "Upload failed: ${response.message()}")
                    onFailure("Upload failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Call the failure lambda and pass the error message
                Log.e("Upload", "Upload error: ${t.message}")
                onFailure(t.message)
            }
        })
    }



    suspend fun getUserImage(userId: Int): Response<UserImageModel> {
        return userService.getUserImage(userId)
    }

    suspend fun getUserData(): Response<UsersModel> {
        return userService.getUserData()
    }

    suspend fun sendMessage(messageModel: MessageModel): Response<Void> {
        return userService.sendMessage(
            receiverEmail = messageModel.receiver_email ?: "",
            senderEmail = messageModel.sender_email ?: "",
            message = messageModel.message.toString()
        )
    }

    fun receiveMessages(userId: Int, onMessageReceived: (MessageModel) -> Unit) {
        webSocketManager.connect(userId, onMessageReceived)
    }
    suspend fun getChats(emailOne: String, emailTwo: String): Response<List<MessageModel>> {
        return userService.getChats(emailOne, emailTwo)
    }
    fun disconnectWebSocket() {
        webSocketManager.disconnect()
    }
}



//class UserApi @Inject constructor(private val client: HttpClient) {
//
//    suspend fun postUserCredentials(data: UserAuthModel): HttpResponse = client.post("$BASE_URL/users") {
//        contentType(ContentType.Application.Json)
//        setBody(data)
//    }.body()
//
//    suspend fun signInUser(data: UserAuthModel):HttpResponse=client.post("$BASE_URL/sign-in"){
//        contentType(ContentType.Application.Json)
//        setBody(data)
//    }.body()
//
//    suspend fun postUserData(data: UserDataModel):HttpResponse=client.post("$BASE_URL/userData"){
//        contentType(ContentType.Application.Json)
//        setBody(data)
//    }.body()
//
//    suspend fun postUserImage(data:UserImageModel):HttpResponse=client.post("$BASE_URL/upload_image"){
//        contentType(ContentType.Application.Json)
//        setBody(data)
//    }.body()
//
//    suspend fun getUserImage(userId:Int):UserImageModel{
//       return try {
//           val response:HttpResponse=client.get("$BASE_URL/get_image/$userId"){
//               contentType(ContentType.Application.Json)
//           }
//           response.body()
//        }
//       catch (e:Exception){
//           e.printStackTrace()
//           throw e
//       }
//        finally {
//            client.close()
//        }
//
//    }
//
//    suspend fun getUserData(): UsersModel {
//        return try {
//            val response: HttpResponse = client.get("$BASE_URL/userData") {
//                contentType(ContentType.Application.Json)
//            }
//
//            // Log the response status code and body
//            println("Response status: ${response.status}")
//            println("Response body: ${response.bodyAsText()}")
//
//            // Deserialize the response body to UsersModel
//            response.body()
//        } catch (e: Exception) {
//            // Handle the exception as needed
//            println("Exception: ${e.message}")
//            // You may want to throw a specific exception or handle it in a way that ensures UsersModel is returned
//            throw e
//        } finally {
//            client.close()
//        }
//    }
//
//    @OptIn(InternalAPI::class)
//    suspend fun sendMessage(messageModel: MessageModel) {
//        val response: HttpResponse = client.post("$BASE_URL/send_message") {
//            body = FormDataContent(Parameters.build {
//                append("sender_id", messageModel.sender_id.toString())
//                append("receiver_id", messageModel.receiver_id.toString())
//                append("message", messageModel.message)
//            })
//        }
//        response.bodyAsText()
//    }
//
//    suspend fun receiveMessages(userId: Int, onMessageReceived: (String) -> Unit) {
//        try {
//            client.webSocket("ws://wingsrender-wtzc.onrender.com/socket") {
//                send(Frame.Text("""{"sender_id": "$userId"}"""))
//
//                incoming.consumeEach { frame ->
//                    if (frame is Frame.Text) {
//                        val message = frame.readText()
//                        onMessageReceived(message)
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            println("Error: ${e.message}")
//            Log.d("receiveMessage", "Error: ${e.message}")
//            e.printStackTrace()
//        }
//    }
//}
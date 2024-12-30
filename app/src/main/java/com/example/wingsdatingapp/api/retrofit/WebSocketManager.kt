package com.example.wingsdatingapp.api.retrofit

import android.util.Log
import com.example.wingsdatingapp.model.MessageModel
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WebSocketManager @Inject constructor() {

    private val client = OkHttpClient.Builder()
        .pingInterval(30, TimeUnit.SECONDS)
        .build()

    private var webSocket: WebSocket? = null
    private val gson = Gson() // Initialize Gson for JSON parsing

    fun connect(userId: Int, onMessageReceived: (MessageModel) -> Unit) {
        val request = Request.Builder()
            .url("ws://pkdozdddeutactuhwpld.supabase.co/socket")
            .build()

        // Old string for Render
//        "ws://flask-render-deployment-u750.onrender.com/socket"

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                val message = """{"sender_id": "$userId"}"""
                webSocket.send(message)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    // Deserialize the received message from JSON to MessageModel
                    val messageModel = gson.fromJson(text, MessageModel::class.java)
                    onMessageReceived(messageModel)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("WebSocket", "Error parsing message: $text")
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                try {
                    val text = bytes.utf8()
                    // Deserialize the message from JSON
                    val messageModel = gson.fromJson(text, MessageModel::class.java)
                    onMessageReceived(messageModel)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("WebSocket", "Error parsing message: ${bytes.utf8()}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                println("Error: ${t.message}")
                t.printStackTrace()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                println("WebSocket closed: $reason")
            }
        })

        client.dispatcher.executorService.shutdown() // Shutdown the client after use
    }

    fun disconnect() {
        webSocket?.close(1000, "Goodbye")
        webSocket = null
    }
}
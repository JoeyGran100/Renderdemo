package com.example.wingsdatingapp

import android.app.Application
import com.example.wingsdatingapp.utils.BASE_URL
import dagger.hilt.android.HiltAndroidApp
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

@HiltAndroidApp
class MyApplication: Application() {
    lateinit var mSocket: Socket
    override fun onCreate() {
        super.onCreate()
        try {
            mSocket = IO.socket(BASE_URL)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        mSocket.connect()
    }
}
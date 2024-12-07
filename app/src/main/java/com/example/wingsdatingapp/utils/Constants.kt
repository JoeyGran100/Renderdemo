package com.example.wingsdatingapp.utils

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

const val BASE_URL="https://wingittest-flask-app-demo.onrender.com"

// Old wingsRender
//const val BASE_URL="https://wingsrender-wtzc.onrender.com"

const val CLIENT_ID="933681236830-cmvcaa43o8s9tm0b0t5qr18it4fpmnit.apps.googleusercontent.com"

// Old GoogleSignIn
//const val CLIENT_ID="933681236830-cmvcaa43o8s9tm0b0t5qr18it4fpmnit.apps.googleusercontent.com"


fun getGoogleSignInClient(context: Context): GoogleSignInClient {
    val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(CLIENT_ID)
        .build()

    return GoogleSignIn.getClient(context, signInOptions)
}
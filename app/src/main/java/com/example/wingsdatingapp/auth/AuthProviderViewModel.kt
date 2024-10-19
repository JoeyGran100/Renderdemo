package com.example.wingsdatingapp.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wingsdatingapp.utils.getGoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


class AuthProviderViewModel ():ViewModel() {
    fun googleSignInClient(context: Context):GoogleSignInClient{
     return getGoogleSignInClient(context)
    }
}
package com.example.wingsdatingapp.local.storage

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(context: Context) {
    companion object {
        private const val PREF_NAME = "my_shared_pref"
        private const val CURRENT_USER = "CURRENT_USER_STATE"
        private const val SIGN_IN_GOOGLE="SGN_IN_GOOGLE"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveCurrentUserState(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(CURRENT_USER, value)
        editor.apply()
    }

    fun getCurrentUserState(): Boolean {
        return sharedPreferences.getBoolean(CURRENT_USER, false) // default value is false
    }
    fun saveSignInWithGoogle(value: Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean(SIGN_IN_GOOGLE, value)
        editor.apply()
    }
    fun isSignInGoogle():Boolean{
       return sharedPreferences.getBoolean(SIGN_IN_GOOGLE,false)
    }
}
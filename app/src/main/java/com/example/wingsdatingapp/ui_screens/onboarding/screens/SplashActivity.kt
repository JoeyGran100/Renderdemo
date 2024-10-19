package com.example.wingsdatingapp.ui_screens.onboarding.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.local.storage.SharedPrefManager
import com.example.wingsdatingapp.ui_screens.MainActivity
import com.example.wingsdatingapp.ui_screens.dating.screens.DatingMainActivity
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange
import com.example.wingsdatingapp.ui_screens.ui.theme.WingsDatingAppTheme
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WingsDatingAppTheme {
SplashLayout()
            }
            NavigateToMainActivityAfterDelay()
        }
    }
    @Composable
    private fun NavigateToMainActivityAfterDelay() {
        LaunchedEffect(key1 = Unit) {
            delay(4000) // Delay for 4 seconds
            if(SharedPrefManager(this@SplashActivity).getCurrentUserState()){
                startActivity(Intent(this@SplashActivity, DatingMainActivity::class.java))
            }
            else{
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
            finish()
        }
    }

    @Composable
    private fun SplashLayout(){
        val textFont = FontFamily(
            Font(R.font.aerotow, FontWeight.Normal, FontStyle.Normal)
        )
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Orange)){
            Text(text = "WingIt", color = Color.White,
                fontSize = 46.sp,
                modifier = Modifier.align(Alignment.Center), fontFamily = textFont)
        }
    }
}


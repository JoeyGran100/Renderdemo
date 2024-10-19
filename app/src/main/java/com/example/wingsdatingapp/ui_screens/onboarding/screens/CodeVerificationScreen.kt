package com.example.wingsdatingapp.ui_screens.onboarding.screens

import android.graphics.Paint.Align
import android.inputmethodservice.Keyboard
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.ui.theme.LightTextColor
import com.example.wingsdatingapp.ui.theme.PinBarColor
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange
import com.ozcanalasalvar.otp_view.compose.OtpView
import kotlinx.coroutines.delay

object CodeVerificationScreen {
    @Composable
    fun CodeVerificationLayout(navController: NavController) {
        var timerPin by remember { mutableIntStateOf(3*60) }

        LaunchedEffect(key1 = timerPin) {
            if(timerPin>0){
                delay(1000L)
                timerPin-=1
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_btn), contentDescription = "",
                modifier = Modifier.padding(top = 36.dp, bottom = 36.dp)
            )

            Text(
                text = "Verification Code",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = LightTextColor
            )
            Text(
                text = "We have sent the verification code to your phone number.",
                fontSize = 14.sp,
                color = LightTextColor,
                fontWeight = FontWeight.Light
            )
            OtpView(
                value = "",
                digits = 4,
                textColor = Color.Black,
                activeColor = Color.Transparent,
                passiveColor = PinBarColor,
                fontSize = 22.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default,
                onTextChange = {value,completed->

                },
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 90.dp)
                .weight(1f), contentAlignment = Alignment.BottomCenter){
                Column {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp), horizontalArrangement = Arrangement.Center) {
                        Text(text = "Didn't receive the code?", color = LightTextColor, fontWeight = FontWeight.Medium,
                            fontSize = 15.sp)
                        Text(text ="Resend", color = Orange, fontSize = 15.sp,fontWeight = FontWeight.Medium)
                    }
                    Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                        Orange)) {
                        Text(text = "Submit", color = Color.White, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
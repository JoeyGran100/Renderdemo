package com.example.wingsdatingapp.ui_screens.onboarding.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.ui.theme.LightTextColor
import com.example.wingsdatingapp.ui_screens.onboarding.screens.navigation.NavigationItem
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange

object ForgotPasswordScreen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ForgotPasswordLayout(navController: NavController) {
        val phoneNumber = remember { mutableStateOf("") }

        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_btn), contentDescription = "",
                modifier = Modifier.padding(top = 36.dp, bottom = 36.dp)
            )
            Text(
                text = "Forgot Password",
                fontSize = 28.sp,
                color = LightTextColor,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Provide your phone number for which you want to reset your password",
                fontSize = 14.sp,
                color = LightTextColor,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(top = 4.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_forgot_password),
                contentDescription = "",
                alignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth().padding(32.dp)
            )
            Text(
                text = "Phone Number",
                fontSize = 16.sp,
                color = LightTextColor,
                fontWeight = FontWeight.Medium
            )

            OutlinedTextField(
                value = phoneNumber.value,
                onValueChange = { phoneNumber.value = it },
                label = { Text(text = "Phone Number") },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .background(
                        Color(0xFFE6E6E6),
                        shape = RoundedCornerShape(10.dp)
                    ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                )
            )

            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                Button(onClick = { navController.navigate(NavigationItem.CodeVerification.route) }, modifier = Modifier
                    .fillMaxWidth().padding(bottom = 90.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .align(Alignment.BottomCenter), colors = ButtonDefaults.buttonColors(Orange)) {
                    Text(text = "Send", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}
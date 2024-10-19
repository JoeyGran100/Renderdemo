package com.example.wingsdatingapp.ui_screens.onboarding.screens

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.ui.theme.LightTextColor
import com.example.wingsdatingapp.ui_screens.onboarding.screens.navigation.NavigationItem
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange

object SelectGenderScreen {

    @Composable
    fun SelectGenderLayout(navController: NavController) {
        var selectedGender by remember { mutableStateOf("") }
        val activity = LocalContext.current as? Activity

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            if (activity != null) {
                BackButton(activity)
            }

            HeaderText()

            GenderSelectionBox(selectedGender, modifier = Modifier.weight(1f)) { gender ->
                selectedGender = gender
            }
ContinueBtn(modifier = Modifier.weight(1f), selectedGender = selectedGender, navController = navController) {

}
        }
    }

    @Composable
    fun BackButton(activity:Activity) {
        Image(
            painter = painterResource(id = R.drawable.ic_back_btn),
            contentDescription = "",
            modifier = Modifier.padding(vertical = 36.dp).clickable(indication = null, interactionSource = remember {
                MutableInteractionSource()
            }) {
               activity.finish()
            }
        )
    }

    @Composable
    fun HeaderText() {
        Text(
            text = "I am a",
            fontSize = 28.sp,
            color = LightTextColor,
            fontWeight = FontWeight.Bold
        )
    }

    @Composable
    fun GenderSelectionBox(selectedGender: String, modifier: Modifier,onGenderSelected: (String) -> Unit) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GenderOptionBox("Women", selectedGender == "Women") {
                    onGenderSelected("Women")
                }

                GenderOptionBox("Men", selectedGender == "Men") {
                    onGenderSelected("Men")
                }
            }
        }
    }

    @Composable
    fun GenderOptionBox(gender: String, isSelected: Boolean, onClick: () -> Unit) {
        val backgroundColor = if (isSelected) Orange else Color.White
        val textColor = if (isSelected) Color.White else Color.Black
        val tickColor = if (isSelected) Color.White else Color(0xFFADAFBB)

        Box(
            modifier = Modifier
                .height(60.dp)
                .width(295.dp)
                .padding(bottom = 10.dp)
                .background(backgroundColor, shape = RoundedCornerShape(15.dp))
                .border(1.dp, Color(0xFFE8E6EA), shape = RoundedCornerShape(15.dp))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = gender,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp),
                color = textColor
            )
            Image(
                painter = painterResource(id = R.drawable.ic_tick),
                contentDescription = "",
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.TopEnd),
                colorFilter = ColorFilter.tint(tickColor)
            )
        }
    }
    @Composable
    private fun ContinueBtn(modifier: Modifier,selectedGender: String, navController: NavController, onGenderNotSelected: () -> Unit){
       val context= LocalContext.current
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 90.dp), contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    if(selectedGender.isNotEmpty()){
                        navController.navigate("${NavigationItem.UserDetails.route}/$selectedGender")
                    }
                    else{
                        Toast.makeText(context,"Please select your gender to continue",Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    Orange
                )
            ) {
                Text(
                    text = "Continue",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
package com.example.wingsdatingapp.ui_screens.dating.screens.personality

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.ui.theme.LightTextColor
import com.example.wingsdatingapp.ui_screens.dating.screens.DatingMainActivity
import com.example.wingsdatingapp.ui_screens.dating.screens.navigation.DatingNavigationItem
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.MainViewModel
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange

// TODO: This screen is no longer in use. Personalities are moved to hobbies screen as an option instead.
// TODO: This can be DELETED!

/*
@Composable
fun PersonalityScreenLayout(navController: NavController, mainViewModel: MainViewModel) {

    val context = LocalContext.current

    mainViewModel.hideBottomBar()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Image(painter = painterResource(id = R.drawable.ic_back_btn), contentDescription = "",
            modifier = Modifier
                .padding(top = 36.dp)
                .clickable {
                    navController.popBackStack()
                })
        Text(
            text = "Personality Test",
            fontSize = 28.sp,
            color = Color.Black,
            modifier = Modifier.padding(top = 32.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "You are about to take our personality test. If you already know your personality type, you can skip the test and choose manually.",
            fontSize = 16.sp,
            color = LightTextColor,
            fontWeight = FontWeight.Light
        )
        Image(
            painter = painterResource(id = R.drawable.ic_personality_test), contentDescription = "",
            modifier = Modifier
                .padding(top = 60.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navController.navigate(DatingNavigationItem.PersonalityTestScreen.route) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                Orange
            )
        ) {
            Text(
                text = "Take a test",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }
        Button(
            onClick = { navController.navigate(DatingNavigationItem.PersonalityTestScreen.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .border(1.dp, Orange, shape = RoundedCornerShape(24.dp)),
            colors = ButtonDefaults.buttonColors(
                Color.White
            ),
        ) {
            Text(
                text = "Choose Manually",
                fontSize = 16.sp,
                color = Orange,
            )
        }


    }
}*/

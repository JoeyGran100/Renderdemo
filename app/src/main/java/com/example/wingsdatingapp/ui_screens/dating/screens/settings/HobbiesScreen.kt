package com.example.wingsdatingapp.ui_screens.dating.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.MainViewModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserDetailHiltViewModel
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange

@Composable
fun HobbiesLayout(
    navController: NavController,
    mainViewModel: MainViewModel,
    userDetailHiltViewModel: UserDetailHiltViewModel
) {
//    mainViewModel.hideBottomBar()
    val currentUser = userDetailHiltViewModel.loggedInUser.value
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 36.dp)) {

        Image(
            painter = painterResource(id = R.drawable.ic_back_btn), contentDescription = "",
            modifier = Modifier
                .clickable(
                    onClick = { navController.popBackStack() },
                    indication = null,
                    interactionSource = remember {
                        MutableInteractionSource()
                    })
                .padding(bottom = 16.dp),
        )
        LazyColumn {
            currentUser?.hobbies?.size?.let {
                items(it) { index ->
                    val userHobbyItem by remember { mutableStateOf(currentUser.hobbies[index]) }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp)
                            .background(Orange, RoundedCornerShape(16.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = userHobbyItem,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

}
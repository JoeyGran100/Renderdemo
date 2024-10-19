package com.example.wingsdatingapp.ui_screens.dating.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.api.NetworkResult
import com.example.wingsdatingapp.model.UserMatchEntity
import com.example.wingsdatingapp.ui.theme.LightTextColor
import com.example.wingsdatingapp.ui.theme.PinBarColor
import com.example.wingsdatingapp.ui_screens.dating.screens.navigation.DatingNavigationItem
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.DatingViewModel
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.MainViewModel
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.UserMatchViewModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserDetailHiltViewModel
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange
import com.example.wingsdatingapp.utils.DatingAnim
import com.example.wingsdatingapp.utils.LoadingAnimDialog
import com.example.wingsdatingapp.utils.selectedUserImage
import com.example.wingsdatingapp.utils.selectedUserMatch
import com.example.wingsdatingapp.utils.stringToBitmap

@Composable
fun MatchProfileDetailLayout(
    navController: NavController,
    mainViewModel: MainViewModel,
    userMatchViewModel: UserMatchViewModel
) {

    val userData = selectedUserMatch.value
    val image = selectedUserImage.value?.imageString
    Log.d("userImage", " Image: $image")
val context= LocalContext.current
    var animIsVisible by remember { mutableStateOf(false) }
    mainViewModel.hideBottomBar()

    Box(modifier = Modifier.fillMaxSize()) {

//        DatingAnim()
        AsyncImage(
            model = userData?.image_url,
            contentDescription = "",
            modifier = Modifier
                .height(319.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            contentScale = ContentScale.FillBounds
        )
        LoadingAnimDialog(isVisible = animIsVisible, modifier = Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(630.dp)
                .align(Alignment.BottomCenter)
                .padding(top = 160.dp)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(
                        topEnd = 24.dp,
                        topStart = 24.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 0.dp
                    )
                )
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = userData?.name.toString(),
                fontSize = 26.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 36.dp)
            )
            Text(
                text = "Interest",
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .align(Alignment.Start)
            )
            userData?.hobbies?.let { HobbiesLayout(hobbiesList = it) }

            Text(
                text = "About us",
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .align(Alignment.Start)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(bottom = 10.dp)
                    .border(1.dp, PinBarColor, RoundedCornerShape(10.dp))
            ) {
                Text(
                    text = userData?.bio.toString(),
                    fontSize = 15.sp,
                    color = LightTextColor,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                )
            }
//            Button(onClick = {
//                navController.navigate(DatingNavigationItem.ChatScreen.route)
//            }, colors = ButtonDefaults.buttonColors(Orange), modifier = Modifier.fillMaxWidth()) {
//                Text(text = "Message", color = Color.White, fontWeight = FontWeight.Bold)
//            }
            Box(modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.ic_star), contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                        .background(Color(0xffFFB431), CircleShape)
                        .clip(CircleShape)
                        .padding(10.dp)
                        .clickable(indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                userMatchViewModel.addUser(
                                    UserMatchEntity(
                                        name = userData?.name,
                                        email = userData?.email,
                                        gender = userData?.gender,
                                        hobbies = userData?.hobbies,
                                        image_url = userData?.image_url,
                                        phone_number = userData?.phone_number,
                                        age = userData?.age,
                                        bio = userData?.bio
                                    )
                                )
                                Toast.makeText(context,"User added to Match screen",Toast.LENGTH_SHORT).show()
                            })
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_heart_filled), contentDescription = "",
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterEnd)
                        .background(Color(0xffFF5069), CircleShape)
                        .clip(CircleShape)
                        .padding(10.dp)
                        .clickable(indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                userMatchViewModel.addUser(
                                    UserMatchEntity(
                                        name = userData?.name,
                                        email = userData?.email,
                                        gender = userData?.gender,
                                        hobbies = userData?.hobbies,
                                        image_url = userData?.image_url,
                                        phone_number = userData?.phone_number,
                                        age = userData?.age,
                                        bio = userData?.bio
                                    )
                                )
                                navController.navigate(DatingNavigationItem.ChatScreen.route)
                            })
                )
                Image(painter = painterResource(id = R.drawable.ic_cross), contentDescription = "",
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterStart)
                        .background(Color.Transparent, CircleShape)
                        .border(2.dp,Color.Black, CircleShape)
                        .clip(CircleShape)
                        .padding(10.dp)
                        .clickable(indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
userMatchViewModel.deleteUserById(userData?.email.toString())
                                Toast.makeText(context,"User Removed from Match",Toast.LENGTH_SHORT).show()
                            })
                )

            }

        }
    }
}

@Composable
private fun HobbiesLayout(hobbiesList: List<String>) {

    LazyRow(modifier = Modifier.padding(bottom = 12.dp, end = 12.dp)) {
        items(hobbiesList.size) { index ->
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(end = 16.dp)
                    .background(Orange, shape = RoundedCornerShape(50.dp))
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(50.dp))
                    .padding(12.dp)
            ) {
//                Row(verticalAlignment = Alignment.CenterVertically,) {
                Text(
                    text = hobbiesList[index],
                    fontSize = 15.sp,
                    color = Color.White
                )
//                }
            }
        }
    }
}
package com.example.wingsdatingapp.ui_screens.dating.screens.settings

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.local.storage.SharedPrefManager
import com.example.wingsdatingapp.ui.theme.LightTextColor
import com.example.wingsdatingapp.ui_screens.dating.screens.DatingMainActivity
import com.example.wingsdatingapp.ui_screens.dating.screens.navigation.DatingNavigationItem
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.MainViewModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.SplashActivity
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserDetailHiltViewModel
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange

@Composable
fun AccountDetailLayout(userDetailHiltViewModel: UserDetailHiltViewModel,mainViewModel: MainViewModel) {
    val loginUser= userDetailHiltViewModel.loggedInUser.value
    val userImage= if(loginUser?.gender=="Men") R.drawable.ic_men else R.drawable.ic_women

//    mainViewModel.hideBottomBar()

    Column(modifier = Modifier) {
        // Top section with orange background and profile image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
        ) {
            // Orange background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Orange)
            )
            // Profile title
            Text(
                text = "Account Details",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .padding(start = 16.dp, top = 50.dp)
            )
            // Profile image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(userImage),
                    contentDescription = "",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(120.dp)
                        .background(Color.Black)
                        .align(Alignment.BottomCenter),
                    contentScale = ContentScale.FillBounds
                )
            }
        }

        // Profile info section
        Row(modifier = Modifier.padding(16.dp)) {
            ProfileInfoField(
                label = "Full Name",
                value = loginUser?.name.toString(),
                iconResId = R.drawable.ic_profile,
                width = 204.dp
            )
            Spacer(modifier = Modifier.width(16.dp)) // Add space between columns
            ProfileInfoField(
                label = "Age",
                value = loginUser?.age.toString(),
                iconResId = R.drawable.ic_profile,
                width = 137.dp
            )
        }
        loginUser?.hobbies?.let { HobbiesLayout(it) }
        PhoneNoLayout(loginUser?.phone_number.toString())
        EmailLayout(loginUser?.email.toString())
        BioLayout(loginUser?.bio.toString())
    }
}

@Composable
private fun PhoneNoLayout(phoneNo:String){
    Text(text = "Phone Number",
        color = Color.Black,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(bottom = 4.dp, start = 16.dp, end = 16.dp))

    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(start = 16.dp, bottom = 16.dp)
            .background(Color.White)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,) {

            Image(
                painter = painterResource(id = R.drawable.ic_phone),
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = phoneNo,
                fontSize = 15.sp,
                color = LightTextColor,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun EmailLayout(email:String){
    Text(text = "Email Address",
        color = Color.Black,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(bottom = 4.dp, start = 16.dp))

    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
            .background(Color.White)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,) {

            Image(
                painter = painterResource(id = R.drawable.ic_email),
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = email,
                fontSize = 15.sp,
                color = LightTextColor,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun HobbiesLayout(hobbiesList:List<String>){
    Text(text = "Hobbies",
        color = Color.Black,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(bottom = 4.dp, start = 16.dp))

    LazyRow(modifier = Modifier.padding(bottom = 12.dp, end = 12.dp)) {
        items(hobbiesList.size){index->
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(start = 16.dp)
                    .background(Color.White)
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,) {

                    Text(
                        text = hobbiesList[index],
                        fontSize = 15.sp,
                        color = LightTextColor
                    )
                }
            }
        }
    }
//    Row(modifier = Modifier.padding(bottom = 12.dp)) {
//
//
//    }
}
@Composable
private fun BioLayout(bio:String){
    Text(text = "Bio",
        color = Color.Black,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(bottom = 4.dp, start = 16.dp))
    Box(
        modifier = Modifier
            .height(185.dp)
            .fillMaxWidth()
            .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
            .background(Color.White)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,) {

            Text(
                text =bio,
                fontSize = 15.sp,
                color = LightTextColor
            )
        }
    }
}
@Composable
fun ProfileInfoField(
    label: String,
    value: String,
    iconResId: Int,
    width: Dp
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .width(width)
    ) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .background(Color.White)
                .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = value,
                    fontSize = 15.sp,
                    color = LightTextColor,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
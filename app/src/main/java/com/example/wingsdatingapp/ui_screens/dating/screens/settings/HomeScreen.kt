package com.example.wingsdatingapp.ui_screens.dating.screens.settings

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.local.storage.SharedPrefManager
import com.example.wingsdatingapp.ui.theme.LightTextColor
import com.example.wingsdatingapp.ui_screens.dating.screens.DatingMainActivity
import com.example.wingsdatingapp.ui_screens.dating.screens.navigation.DatingNavigationItem
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.MainViewModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.SplashActivity
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserDetailHiltViewModel

import com.example.wingsdatingapp.ui_screens.ui.theme.Orange

object HomeScreen {
    @Composable
    fun HomeScreenLayout(
        navController: NavController,
        mainViewModel: MainViewModel,
        userDetailHiltViewModel: UserDetailHiltViewModel
    ) {
        val context = LocalContext.current
        val loginUser = userDetailHiltViewModel.loggedInUser.value
        val userImage = if (loginUser?.gender == "Men") R.drawable.ic_men else R.drawable.ic_women
        mainViewModel.showBottomBar()

        Column(modifier = Modifier
            .fillMaxSize()
        ) {
            // Top section with orange background and profile image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color.White)
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
                    text = "Settings",
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
                    AsyncImage(
                        model = loginUser?.image_url?:userImage,
                        contentDescription = "",
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Black)
                            .size(130.dp)
                            .border(5.dp, Color.White, CircleShape)
                            .align(Alignment.BottomCenter),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }

            SettingItemsLayout(name = "Premium Members", color = LightTextColor, iconRes = R.drawable.ic_profile_settings) {

                navController.navigate(DatingNavigationItem.PremiumMembersScreen.route)
            }

            SettingItemsLayout(name = "Hobbies", color = LightTextColor, iconRes = R.drawable.ic_hobbies) {
                navController.navigate(DatingNavigationItem.HobbiesScreen.route)
            }

            SettingItemsLayout(name = "Matches", color = LightTextColor, iconRes = R.drawable.ic_matches) {
                navController.navigate(DatingNavigationItem.MatchesScreen.route)
            }

            SettingItemsLayout(name = "Logout", color = Color(0xFFFF0000), iconRes = R.drawable.ic_logout) {
                userDetailHiltViewModel.deleteUser()
                SharedPrefManager(context).saveCurrentUserState(false)

                if (context is DatingMainActivity) {
                    context.finish()
                    context.startActivity(Intent(context, SplashActivity::class.java))
                }
            }
        }
    }

    @Composable
    private fun SettingItemsLayout(name: String, color: Color, iconRes: Int, onClick: (String) -> Unit) {
        Box(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .background(
                color = if (name == "Logout") Color.White else Color.White,
                shape = RoundedCornerShape(4.dp)
            )
            .border(1.dp, Color(0xFFE6E6E6), RoundedCornerShape(4.dp))
            .clickable { onClick(name) }
            .padding(10.dp)) {
            Image(
                painter = painterResource(id = iconRes), contentDescription = "",
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Text(
                text = name,
                fontWeight = if (name == "Logout") FontWeight.Bold else FontWeight.Normal,
                color = color,
                modifier = Modifier
                    .padding(start = 32.dp)
                    .align(Alignment.CenterStart)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_forwrad_arrow),
                contentDescription = "",
                modifier = Modifier.align(Alignment.CenterEnd),
                colorFilter = ColorFilter.tint(
                    if (name == "Logout") Color(0xFFFF0000) else Color.Black
                )
            )
        }
    }


}
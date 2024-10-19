package com.example.wingsdatingapp.ui_screens.onboarding.screens.user_interest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.api.NetworkResult
import com.example.wingsdatingapp.model.UserDataModel
import com.example.wingsdatingapp.ui.theme.LightTextColor
import com.example.wingsdatingapp.ui_screens.dating.screens.DatingMainActivity
import com.example.wingsdatingapp.ui_screens.onboarding.screens.user_interest.model.UserInterestModel
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserCredentialsViewModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserDetailHiltViewModel
import com.example.wingsdatingapp.utils.LoadingAnimDialog
import com.example.wingsdatingapp.utils.bitmapToString
import com.example.wingsdatingapp.utils.currentUserImageUrl
import com.example.wingsdatingapp.utils.uriToBitmap
import com.example.wingsdatingapp.utils.userEmail
import com.example.wingsdatingapp.utils.userPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object UserInterestScreen {

    @Composable
    fun UserInterestLayout(
        navController: NavController,
        userDetailHiltViewModel: UserDetailHiltViewModel,
        userCredentialsViewModel: UserCredentialsViewModel
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val gender = navBackStackEntry?.arguments?.getString("gender")
        val name = navBackStackEntry?.arguments?.getString("name")
        val age = navBackStackEntry?.arguments?.getString("age")
        val phoneNo = navBackStackEntry?.arguments?.getString("phoneNo")
        val bio = navBackStackEntry?.arguments?.getString("bio")


        val selectedItemList = remember { mutableStateListOf<String>() }
        var showLoadingAnim by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
//        var profileImageAsBitmap by remember { mutableStateOf<Bitmap?>(null) }
        val context = LocalContext.current

        Log.d("userPhoto","user photo: ${userPhoto.value}")


        LoadingAnimDialog(isVisible = showLoadingAnim, modifier = Modifier.fillMaxSize())

        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_btn),
                contentDescription = "",
                modifier =
                Modifier
                    .padding(top = 36.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )

            Text(
                text = "Your Interests",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = LightTextColor,
                modifier = Modifier.padding(top = 36.dp)
            )
            Text(
                text = "Select a few of your interests and let everyone know what you’re passionate about.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                color = LightTextColor,
                modifier = Modifier.padding(bottom = 36.dp)
            )

            LazyVerticalGrid(columns = GridCells.FixedSize(size = 143.dp)) {
                items(itemList().size) { index ->
                    val currentItem = itemList()[index]
                    var isSelected by remember {
                        mutableStateOf(currentItem.isSelected)
                    }
                    Row(
                        modifier = Modifier
                            .padding(end = 12.dp, bottom = 12.dp)
                            .background(
                                if (isSelected) Orange else Color.White,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .border(
                                BorderStroke(1.dp, Color(0xFFE8E6EA)),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .padding(12.dp)
                            .clickable {

                                if (!isSelected) {
                                    if (selectedItemList.size <= 3) {
                                        selectedItemList.add(currentItem.itemName)
                                        isSelected = true
                                    } else {
                                        Toast
                                            .makeText(
                                                context,
                                                "Cannot select more then 4 interests.",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                } else {
                                    selectedItemList.remove(currentItem.itemName)
                                    isSelected = false
                                }


                            }, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = currentItem.resId),
                            contentDescription = ""
                        )
                        Text(
                            text = currentItem.itemName,
                            fontSize = 14.sp,
                            color = if (isSelected) Color.White else Color.Black,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .padding(bottom = 90.dp)
                    .fillMaxWidth()
                    .weight(1f), contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = {

                        scope.launch {
                            val userData = UserDataModel(
                                name = name,
                                email = userEmail.value.toString(),
                                gender = gender,
                                hobbies = selectedItemList,
                                image_url = currentUserImageUrl,
                                phone_number = phoneNo,
                                age = age,
                                bio = bio
                            )
                            userCredentialsViewModel.postUserData(context, userData){result->
                                when(result){
                                    NetworkResult.Loading->{
                                        showLoadingAnim=true
                                    }

                                    is NetworkResult.Error ->{
                                        showLoadingAnim=false
                                    }
                                    is NetworkResult.Success -> {

                                        showLoadingAnim=false
                                        userDetailHiltViewModel.insertUser(userData)
                                        context.startActivity(Intent(context,DatingMainActivity::class.java))
                                        if (context is Activity) {
                                            context.finish()
                                        }
                                    }
                                }
                            }
                            Log.d("dataModel", userData.toString())
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        Orange
                    )
                ) {
                    Text(
                        text = "Send",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
//                Button(onClick = { launcher.launch("image/*") }) {
//                    Text(text = "Taka photo")
//                }
            }
        }
    }


    private fun itemList(): List<UserInterestModel> {
        val itemList = mutableListOf<UserInterestModel>()

        itemList.add(UserInterestModel("Photography", R.drawable.ic_camera, false))
        itemList.add(UserInterestModel("Shopping", R.drawable.ic_shopping, false))
        itemList.add(UserInterestModel("Karooke", R.drawable.ic_voice, false))
        itemList.add(UserInterestModel("Yoga", R.drawable.ic_yoga, false))
        itemList.add(UserInterestModel("Cooking", R.drawable.ic_cooking, false))
        itemList.add(UserInterestModel("Tennis", R.drawable.ic_tennis, false))
        itemList.add(UserInterestModel("Running", R.drawable.ic_running, false))
        itemList.add(UserInterestModel("Swimming", R.drawable.ic_swimming, false))
        itemList.add(UserInterestModel("Art", R.drawable.ic_art, false))
        itemList.add(UserInterestModel("Travelling", R.drawable.ic_travelling, false))
        itemList.add(UserInterestModel("Extreme", R.drawable.ic_extreme, false))
        itemList.add(UserInterestModel("Music", R.drawable.ic_music, false))
        itemList.add(UserInterestModel("Running", R.drawable.ic_drink, false))
        itemList.add(UserInterestModel("Swimming", R.drawable.ic_video_game, false))
        return itemList
    }
}
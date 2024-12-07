package com.example.wingsdatingapp.ui_screens.dating.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.model.UserMatchEntity
import com.example.wingsdatingapp.ui.theme.LightTextColor
import com.example.wingsdatingapp.ui_screens.dating.screens.navigation.DatingNavigationItem
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.MainViewModel
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.UserMatchViewModel
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange
import com.example.wingsdatingapp.ui_screens.ui.theme.awesomeBlueColor
import com.example.wingsdatingapp.ui_screens.ui.theme.bottomSheetBarColor
import com.example.wingsdatingapp.ui_screens.ui.theme.textColorOfBio
import com.example.wingsdatingapp.utils.LoadingAnimDialog
import com.example.wingsdatingapp.utils.selectedUserImage
import com.example.wingsdatingapp.utils.selectedUserMatch


/*@Preview(showBackground = true)
@Composable
fun MatchProfilePreview(modifier: Modifier = Modifier) {


    MatchProfileDetailLayout(
        navController = rememberNavController(),
        mainViewModel = MainViewModel(),
        userMatchViewModel = UserMatchViewModel(userMatchDao = FakeUserDao)
    )
}

object FakeUserDao : UserMatchDao {
    override suspend fun addUser(user: UserMatchEntity) {
        UserMatchEntity(
            id = 1,
            name = "Julia",
            email = "julia@gmail.com",
            gender = "Female",
            hobbies = null,
            image_url = "",
            phone_number = "070 888 999",
            age = "25",
            bio = "I am a female name Julia"

        )

    }

    override suspend fun deleteUser(user: UserMatchEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUserByEmail(userEmail: String) {
        TODO("Not yet implemented")
    }

    override fun getAllUsers(): List<UserMatchEntity> {
        return listOf(

            UserMatchEntity(
                id = 1,
                name = "Julia",
                email = "julia@gmail.com",
                gender = "Female",
                hobbies = null,
                image_url = "",
                phone_number = "070 888 999",
                age = "25",
                bio = "I am a female name Julia"

            )
        )
    }
}*/


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchProfileDetailLayout(
    navController: NavController,
    mainViewModel: MainViewModel,
    userMatchViewModel: UserMatchViewModel
) {

    mainViewModel.hideBottomBar()

    // State to control the visibility of the bottom sheet
    var openBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // Open the bottom sheet to 65% when `openBottomSheet` is set to true
    LaunchedEffect(openBottomSheet) {
        if (openBottomSheet) {
            bottomSheetState.show() // Opens the sheet in the expanded state
        }
    }

    val userData = selectedUserMatch.value
    val image = selectedUserImage.value?.imageString
    Log.d("userImage", " Image: $image")
    val context = LocalContext.current
    var animIsVisible by remember { mutableStateOf(false) }

    mainViewModel.hideBottomBar()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            // Define references for components
            val (
                profileImage, nameText, locationText,
                dislikeIcon, superLikeIcon, likeIcon, floatingButton
            ) = createRefs()

            // Create a horizontal chain for like/dislike icons
            createHorizontalChain(
                dislikeIcon,
                superLikeIcon,
                likeIcon,
                chainStyle = ChainStyle.Spread
            )

            // Create guidelines for positioning
            val guideBottom = createGuidelineFromBottom(88.dp)
            val guideRightQuarter = createGuidelineFromTop(2.5f / 5f)

            // User profile image
//            AsyncImage(
//                model = userData?.image_url,
//                contentDescription = "",
//                Modifier.constrainAs(profileImage) {
//                    top.linkTo(parent.top)
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                    bottom.linkTo(parent.bottom)
//                }
//            )

            UserMatchImage(
                model = R.drawable.ic_women_testpicture_4,
                modifier = Modifier.constrainAs(profileImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            )

            LoadingAnimDialog(isVisible = animIsVisible, modifier = Modifier.fillMaxSize())

            // Name and Age
            NameAndAge(
                username = userData?.name.toString(),
                userAge = userData?.age.toString(),
                verifiedImage = R.drawable.verified_user_1, // Pass the verified image resource
                modifier = Modifier.constrainAs(nameText) {
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(superLikeIcon.top, 24.dp)
                },
            )

            // Like button
            FloatingActionButton(
                modifier = Modifier
                    .size(65.dp)
                    .constrainAs(likeIcon) {
                        bottom.linkTo(guideBottom)
                    },
                containerColor = Color(0xffFF5069),
                contentColor = Color.White,
                shape = CircleShape,

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

                    // Navigates the user to Chat screen
                    navController.navigate(DatingNavigationItem.ChatScreen.route)

                    Toast
                        .makeText(
                            context,
                            "User added to Match screen",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_heart_filled),
                    contentDescription = "Like",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }

            // Save for later button -> User can decide later to accept or reject the user in the matches screen
            FloatingActionButton(
                modifier = Modifier
                    .size(65.dp)
                    .constrainAs(superLikeIcon) {
                        bottom.linkTo(guideBottom)
                    },
                containerColor = Color(0xffFFB431),
                contentColor = Color.White,
                shape = CircleShape,

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
                    Toast
                        .makeText(
                            context,
                            "User added to Match screen",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                },

                ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = "Super Like",
                    modifier = Modifier.size(35.dp),
                    tint = Color.White
                )
            }

            // Reject button -> Rejects the user
            FloatingActionButton(
                modifier = Modifier
                    .size(65.dp)
                    .constrainAs(dislikeIcon) {
                        bottom.linkTo(guideBottom)
                    },
                containerColor = Color.White,
                contentColor = Color.Red,
                shape = CircleShape,

                onClick = {

                    userMatchViewModel.deleteUserById(userData?.email.toString())
                    Toast
                        .makeText(
                            context,
                            "User Removed from Match",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                },


                ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cross),
                    contentDescription = "Dislike",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Red
                )
            }

            // New Floating Action Button at 3/4 down on the right side
            // Opens Modal sheet
            FloatingActionButton(
                onClick = { openBottomSheet = true },
                modifier = Modifier
                    .size(65.dp)
                    .constrainAs(floatingButton) {
                        top.linkTo(guideRightQuarter)
                        end.linkTo(parent.end, margin = 16.dp)
                    },
                containerColor = awesomeBlueColor,
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Open Modal",
                    modifier = Modifier.size(35.dp),
                    tint = Color.White
                )
            }
        }

        // Bottom Sheet Content
        if (openBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { openBottomSheet = false },
                sheetState = bottomSheetState,
                containerColor = bottomSheetBarColor,
            ) {

                // Bottom sheet content

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.65f) // Adjusts the height to 80% of the screen height
                        .padding(top = 10.dp)
                    ,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)

                    ) {

                        Text(
                            text = "Interest",
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(top = 10.dp, bottom = 10.dp)
                                .align(Alignment.Start)
                        )

                        // Here is where the hobbies list goes to display for the user

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
                                .fillMaxSize()
                                .padding(bottom = 10.dp)
                                .border(1.dp, LightTextColor, RoundedCornerShape(10.dp))
                        ) {
                            // Bio information
                            Text(
                                text = userData?.bio.toString(),
                                fontSize = 15.sp,
                                color = textColorOfBio,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(12.dp)
                            )
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun NameAndAge(
    username: String,
    userAge: String,
    verifiedImage: Int, // Add a parameter for the verified logo
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically // Aligns items vertically in the center
    ) {
        Text(
            text = username,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )


        Spacer(modifier = Modifier.width(4.dp)) // Space between username and age

        Text(
            text = ", $userAge",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.width(10.dp)) // Space between age and verified logo

        Image(
            painter = painterResource(id = verifiedImage),
            contentDescription = "Verified User",
            modifier = Modifier.size(35.dp) // Adjust the size of the logo as necessary
        )
    }
}

@Composable
fun UserMatchImage(
    model: Any?,
    modifier: Modifier
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        // Main Image
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = model,
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
        )
        // Gradient Overlay
        GradientOverlay()
    }
}

@Composable
private fun GradientOverlay() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.5f)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent, // Start of the gradient (top)
                        Color.Black // End of the gradient (bottom)
                    )
                )
            )
    )
}


/*@Composable
fun MatchProfileDetailLayout(
    navController: NavController,
    mainViewModel: MainViewModel,
    userMatchViewModel: UserMatchViewModel
) {

    val userData = selectedUserMatch.value
    val image = selectedUserImage.value?.imageString
    Log.d("userImage", " Image: $image")
    val context = LocalContext.current
    var animIsVisible by remember { mutableStateOf(false) }

    mainViewModel.hideBottomBar()

    Box(modifier = Modifier.fillMaxSize()) {

//        DatingAnim()
        AsyncImage(
            model = R.drawable.ic_women_profpic_ai,
            contentDescription = "",
            modifier = Modifier
                .height(319.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            contentScale = ContentScale.FillBounds
        )

*//*        AsyncImage(
            model = userData?.image_url,
            contentDescription = "",
            modifier = Modifier
                .height(319.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            contentScale = ContentScale.FillBounds
        )*//*

        LoadingAnimDialog(isVisible = animIsVisible, modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp)
                .align(Alignment.BottomCenter)
                .padding(top = 110.dp)
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

            Row(
                modifier = Modifier
                    .padding(top = 26.dp) // Padding for the entire row
                    .align(Alignment.CenterHorizontally), // Center alignment
                verticalAlignment = Alignment.CenterVertically // Center text vertically with the logo
            ) {

                Text(
                    text = "${userData?.name.toString()}, ${userData?.age.toString() }", // Combine name and age
                    fontSize = 26.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(8.dp)) // Space between the logo and the text

                Image(
                    painter = painterResource(id = R.drawable.verified_user_1), // Replace with your logo resource
                    contentDescription = "User Logo",
                    modifier = Modifier.size(25.dp) // Adjust size as necessary
                )

            }

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
            Box(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .fillMaxWidth(),

                contentAlignment = Alignment.Center
            ) {

                // Represents the Star button
                FloatingActionButton(
                    modifier = Modifier
                        .size(75.dp)
                        .align(Alignment.Center),
                    containerColor = Color(0xffFFB431),
                    contentColor = Color.White,
                    shape = CircleShape,
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

                        Toast
                            .makeText(
                                context,
                                "User added to Match screen",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_star),
                        contentDescription = "Like user to save in matches screen and decide later",
                        modifier = Modifier.size(33.dp)
                    )
                }


                // Represents the Heart button
                FloatingActionButton(
                    modifier = Modifier
                        .size(68.dp)
                        .align(Alignment.CenterEnd),
                    containerColor = Color(0xffFF5069),
                    contentColor = Color.White,
                    shape = CircleShape,
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

                        Toast
                            .makeText(
                                context,
                                "User added to Match screen",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_heart_filled),
                        contentDescription = "Accept user",
                        modifier = Modifier.size(30.dp)
                    )
                }


                // Represents the Reject/ Cross button
                FloatingActionButton(
                    modifier = Modifier
                        .size(68.dp) // Custom size for FAB
                        .border(
                            2.dp,
                            Color.Red,
                            CircleShape
                        ) // Red circular border with 4dp thickness
                        .align(Alignment.CenterStart), // Align within parent if necessary
                    containerColor = Color.White, // Make FAB background transparent
                    contentColor = Color.Red, // Set icon color to red
                    shape = CircleShape,

                    onClick = {
                        // Action when button is clicked
                        userMatchViewModel.deleteUserById(userData?.email.toString())
                        Toast
                            .makeText(
                                context,
                                "User Removed from Match",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_cross),
                        contentDescription = "Remove user",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }

        }
    }
}*/





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
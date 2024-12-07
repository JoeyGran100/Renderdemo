package com.example.wingsdatingapp.ui_screens.dating.screens.settings

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.api.NetworkResult
import com.example.wingsdatingapp.model.UserDataModel
import com.example.wingsdatingapp.model.UserMatchEntity
import com.example.wingsdatingapp.ui.theme.LightTextColor
import com.example.wingsdatingapp.ui_screens.dating.screens.navigation.DatingNavigationItem
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.DatingViewModel
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.MainViewModel
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.UserMatchViewModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserDetailHiltViewModel
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange
import com.example.wingsdatingapp.ui_screens.ui.theme.matchesLightGreyColor
import com.example.wingsdatingapp.utils.updateSelectedUserMatch
import kotlinx.coroutines.launch

@Composable
fun UserMatchLayout(
    navController: NavController,
    userListViewModel: DatingViewModel,
    userDetailHiltViewModel: UserDetailHiltViewModel,
    userMatchViewModel: UserMatchViewModel,
    mainViewModel: MainViewModel
) {
    val userList = userListViewModel.userDetails.collectAsState()
    var isAnimVisible by remember { mutableStateOf(false) }

    // Collect the user match data as a state, so recomposition happens when the list changes
    val userMatched by userMatchViewModel.matchUsers.collectAsState()

    // Hides the buttonAppBar
    mainViewModel.hideBottomBar()

    Log.d("matchdata", userMatched.toString())

    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 36.dp)) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp), verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_back_btn),
                contentDescription = "",
                modifier = Modifier
                    .clickable(
                        onClick = { navController.popBackStack() },
                        indication = null, // Removes the click effect
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )

            Spacer(Modifier.width(25.dp))

            Text(
                text = "Matches", color = Color.Black, fontSize = 24.sp,
                modifier = Modifier.padding(start = 16.dp), fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }


        if (userMatched.isNotEmpty()) {
            LazyColumn {
                items(userMatched.size) { index ->
                    val currentItem = userMatched[index]
                    val name = currentItem.name
                    val age = currentItem.age
                    val userImage =
                        if (currentItem.gender == "Men") R.drawable.ic_men else R.drawable.ic_women

                    UserProfileItem(
                        name = name.toString(),
                        age = age.toString(),
                        profileImage = userImage,
                        obClickItem = {
                            // Handle profile item click
                        },
                        onAccepted = {
                            updateSelectedUserMatch(
                                UserDataModel(
                                    name = currentItem.name,
                                    email = currentItem.email,
                                    gender = currentItem.gender,
                                    hobbies = currentItem.hobbies,
                                    image_url = currentItem.image_url,
                                    phone_number = currentItem.phone_number,
                                    age = currentItem.age,
                                    bio = currentItem.bio
                                )
                            )
                            navController.navigate(DatingNavigationItem.ChatScreen.route)
                        },
                        onRejected = {
                            // Remove the user from the list by calling delete and updating the ViewModel
                            userMatchViewModel.deleteUserById(currentItem.email.toString())
                        }
                    )
                }
            }

        } else {

            NoDataLayout()

        }

        when (val result = userList.value) {
            is NetworkResult.Error -> {
                isAnimVisible = false
//                NoDataLayout()
            }

            NetworkResult.Loading -> {
                isAnimVisible = true
            }

            is NetworkResult.Success -> {
                val userMatch = findUserMatch(
                    userList = result.data.users,
                    userDetailHiltViewModel = userDetailHiltViewModel
                )
                if (userMatch.isEmpty()) {

                    // TODO: Previously it was NoDataLayout here but it was still showing even if users was added to the code!

                } else {
                    if (userMatched.isNotEmpty()) {
                        Button(
                            onClick = {
                                userMatch.forEach { model ->
                                    userMatchViewModel.addUser(
                                        UserMatchEntity(
                                            name = model.name,
                                            email = model.email,
                                            gender = model.gender,
                                            hobbies = model.hobbies,
                                            phone_number = model.phone_number,
                                            age = model.age,
                                            bio = model.bio
                                        )
                                    )
                                }
                            },
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                Orange
                            )
                        ) {
                            Text(
                                text = "Find Perfect Match",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                isAnimVisible = false
            }
        }
    }
}


@Composable
private fun NoDataLayout() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 130.dp), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.ic_no_data), contentDescription = "")
        Text(
            text = "No Added Users!",
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = Color.Black,
            modifier = Modifier.padding(top = 30.dp)
        )
        Text(
            text = "No users are added yet, but all the people you will meet & like will be added here! :D Just WingIt!",
            textAlign = TextAlign.Center,
            color = LightTextColor,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )

    }
}


// I created a more modern UI for Matches screen. It functions well as the previous UI. Made 2024/10/28.
@Composable
private fun UserProfileItem(
    name: String, age: String, profileImage: Int, obClickItem: () -> Unit,
    onAccepted: () -> Unit, onRejected: () -> Unit
) {
    val localContext = LocalContext.current

    Column(modifier = Modifier.padding(vertical = 10.dp)) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)  // Reduced height for compact look
                .background(Color.White)
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .clickable { obClickItem() }
                .padding(8.dp)  // Reduced inner padding
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Profile picture
                Image(
                    painter = painterResource(id = profileImage),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(80.dp)  // Smaller size for compact look
                        .clip(CircleShape)
                        .border(2.dp, color = Color.LightGray, shape = CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(6.dp))  // Reduced spacer width

                // Column for Name, Age, "New Request" text, and Buttons
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp), // Compact spacing
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .fillMaxWidth()
                        .height(100.dp)  // Adjusted height for compact look
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) { // Inner column spacing
                        // Name and Age
                        Text(
                            text = "$name, $age",
                            fontSize = 16.sp,  // Reduced font size
                            color = LightTextColor,
                            fontWeight = FontWeight.SemiBold
                        )

                        // "New Request" text below the name
                        Text(text = "New friend request", fontSize = 14.sp, color = Color.Gray)
                        Text(text = "4 min ago", fontSize = 12.sp, color = Color.LightGray)
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp), // Reduced space between buttons
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                Toast.makeText(localContext, "Decline", Toast.LENGTH_SHORT).show()
                                onRejected()
                            },
                            colors = ButtonDefaults.buttonColors(Color.White),
                            border = BorderStroke(1.dp, Color.Red),
                            shape = RoundedCornerShape(8.dp),  // Slightly rounded corners
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Decline",
                                fontSize = 14.sp,  // Reduced font size
                                color = Color.Red
                            )
                        }

                        Button(
                            onClick = {
                                Toast.makeText(localContext, "Accepted", Toast.LENGTH_SHORT).show()
                                onAccepted()
                            },
                            colors = ButtonDefaults.buttonColors(Orange),
                            shape = RoundedCornerShape(8.dp),  // Slightly rounded corners
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Accept",
                                fontSize = 14.sp,  // Reduced font size
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }


}


// Original made by Umair. I changed this out to a more modern version of UI.
/*@Composable
private fun UserProfileItem(
    name: String, age: String, profileImage: Int, obClickItem: () -> Unit,
    onAccepted: () -> Unit, onRejected: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()  // Ensures the box takes the full width
            .height(80.dp)  // Increase height here (adjust value as per requirement)
            .background(Color.White)
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)) // Light gray border
            .clip(RoundedCornerShape(12.dp))
            .clickable { obClickItem() }
            .padding(5.dp), // Padding inside the border

        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth() // Make the Row take up the full width
        ) {
            Image(
                painter = painterResource(id = profileImage),
                contentDescription = "",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(3.dp, color = Color.LightGray, shape = CircleShape)
                ,
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .weight(1f), // This will push the buttons to the end
                verticalArrangement = Arrangement.Center
            ) {

                // I removed "name:" from this to make it look better
                Text(text = name, fontSize = 14.sp, color = LightTextColor)
                Text(text = "Age: $age Yrs", fontSize = 14.sp, color = LightTextColor)
            }
            Text(
                text = "Accept", color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .background(Color(0xffA5EF6C), RectangleShape)
                    .padding(6.dp)
                    .clickable(indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { onAccepted() })
            )
            Text(
                text = "Reject", color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .background(Color(0xffFFA8A8), RectangleShape)
                    .padding(6.dp)
                    .clickable(indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { onRejected() })
            )
        }

    }
}*/

@Composable
fun findUserMatch(
    userList: List<UserDataModel>,
    userDetailHiltViewModel: UserDetailHiltViewModel
): List<UserDataModel> {
    val scope = rememberCoroutineScope()

    val perfectMatchList = remember { mutableStateListOf<UserDataModel>() }
    LaunchedEffect(Unit) {
        scope.launch {

            val currentUser = userDetailHiltViewModel.loggedInUser.value
            val idFilteredList = userList.filter { list ->
                list.email != currentUser?.email
            }
            val genderFilteredList =
                if (currentUser?.gender == "Men") idFilteredList.filter { it.gender == "Women" } else idFilteredList.filter { it.gender == "Men" }
            val hobbyFilteredList = genderFilteredList.filter { otherUser ->
                otherUser.hobbies?.any { currentUser?.hobbies?.contains(it) == true } == true
            }

            // Add matched users to perfectMatchList
            perfectMatchList.addAll(hobbyFilteredList)
        }
    }
    Log.d("PerfectMatch", perfectMatchList.toList().toString())
    return perfectMatchList
}
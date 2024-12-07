package com.example.wingsdatingapp.ui_screens.dating.screens


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.api.NetworkResult
import com.example.wingsdatingapp.model.UserDataModel
import com.example.wingsdatingapp.ui.theme.LightTextColor
import com.example.wingsdatingapp.ui_screens.dating.screens.navigation.DatingNavigationItem
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.DatingViewModel
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.MainViewModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserDetailHiltViewModel
import com.example.wingsdatingapp.utils.LoadingAnim
import com.example.wingsdatingapp.utils.selectedItemIndex
import com.example.wingsdatingapp.utils.updateSelectedUserMatch
import com.example.wingsdatingapp.utils.updateUserPhoto


// This is the modified but still original code created by Umar, which displays users on the database and lets you add them to matches screen
object SearchScreen {

    @Composable
    fun SearchScreenLayout(
        userListViewModel: DatingViewModel,
        userDetailHiltViewModel: UserDetailHiltViewModel,
        navController: NavController,
        mainViewModel: MainViewModel
    ) {

        var loadingAnim by remember { mutableStateOf(true) }
        val context = LocalContext.current
        val userList = userListViewModel.userDetails.collectAsState()
        mainViewModel.showBottomBar()


        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {

            when (val result = userList.value) {
                is NetworkResult.Loading -> {
                    loadingAnim = true
                    LoadingAnim(isVisible = loadingAnim)
                }

                is NetworkResult.Error -> {
                    loadingAnim = false
//                    Text(text = result.message)

                    // Debug log
                    Log.d("errorInflatingList", "Error: ${result.message}")
                    Toast.makeText(
                        context,
                        "Something went wrong please try again...",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Success -> {
                    loadingAnim = false

                    // Filter the list to exclude the logged-in user
                    val activeUsers = result.data.users.filter { user ->
                        user.email != userDetailHiltViewModel.loggedInUser.value?.email
                    }
                    Log.d(
                        "activeUser",
                        "Active user: ${userDetailHiltViewModel.loggedInUser.value?.id}"
                    )

                    UserDetailsItem(
                        userList = activeUsers,
                        navController,
                        userListViewModel,
                        userDetailHiltViewModel
                    )

                }

            }
        }
    }


    @Composable
    fun UserDetailsItem(
        userList: List<UserDataModel>,
        navController: NavController,
        datingViewModel: DatingViewModel,
        userDetailHiltViewModel: UserDetailHiltViewModel
    ) {

        LazyColumn {
            items(userList.size) { index ->
                val currentItem = userList[index]
                val name = currentItem.name
                val age = currentItem.age
                currentItem.id?.let { datingViewModel.getUserImage(it) }


                val userImage =
                    if (currentItem.gender == "Men") R.drawable.ic_men else R.drawable.ic_women

                UserProfileItem(
                    name = name.toString(),
                    age = age.toString(),
                    profileImage = userImage
                ) {

                    updateSelectedUserMatch(currentItem)
                    selectedItemIndex = index
                    datingViewModel.userImage.value?.let { updateUserPhoto(it) }

                    navController.navigate(DatingNavigationItem.MatchingDetailProfile.route)
                }

            }
        }
    }

    // This is the profile of users with name that pops-up in the search field list
    @Composable
    private fun UserProfileItem(
        name: String,
        age: String,
        profileImage: Int,
        obClickItem: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .background(Color.White)
                .padding(5.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { obClickItem() },
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically // Center the content vertically
            ) {
                Image(
                    painter = painterResource(id = profileImage),
                    contentDescription = "",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )
                Column(
                    modifier = Modifier.padding(start = 12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Name: $name", fontSize = 14.sp, color = LightTextColor)
                    Text(text = "Age: $age", fontSize = 14.sp, color = LightTextColor)
                }
            }
        }
    }

}


/*object HomeScreen {

    @OptIn(ExperimentalPermissionsApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun HomeScreenLayout(
        mainViewModel: MainViewModel,
        mainNavControllerToLocation: NavController,
        permissionState: MultiplePermissionsState

    ) {

        mainViewModel.showBottomBar()

        // State to keep track of which image is selected
        var selectedChoice by remember { mutableStateOf("") }

        // Sample images (replace these with your actual images)
        val image1 =
            painterResource(id = R.drawable.ic_friends)  // Replace with actual image resource
        val image2 =
            painterResource(id = R.drawable.ic_dating)  // Replace with actual image resource

        Scaffold(Modifier.fillMaxSize(),

            // Bottom bar
            bottomBar = {

                mainViewModel.showBottomBar
            },

            // Main content
            content = {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(start = 16.dp, end = 16.dp, top = 26.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Start your journey!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 36.dp, bottom = 10.dp)
                    )

                    Text(
                        text = "Make friends or find a partner for life wherever you are and whenever you want! It begins with you!",
                        fontSize = 16.sp,
                        color = LightTextColor,
                        fontWeight = FontWeight.Light
                    )

                    Spacer(modifier = Modifier.height(55.dp))
                    Text(
                        text = "What are we looking for today?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black,
                    )

                    Spacer(modifier = Modifier.height(55.dp))


                    // Row to display the images, meaning Friends and Soulmate images, side by side

                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(55.dp)
                    ) {
                        // First Image with Text
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            RoundImageWithBorder2(
                                image = image1,
                                choice = "image1",
                                isSelected = selectedChoice == "image1",
                                onClick = {
                                    selectedChoice = "image1"
                                }  // Set the first image as selected when clicked
                            )

                            Spacer(modifier = Modifier.height(8.dp))  // Space between image and Text

                            // First Text below the image
                            Text(
                                text = "Friends",  // Static text for the first image
                                color = if (selectedChoice == "image1") Color.Black else Color.LightGray,  // Change color based on selection
                                fontWeight = if (selectedChoice == "image1") FontWeight.Bold else FontWeight.Normal,  // Bold if selected, normal if not
                                style = MaterialTheme.typography.bodyLarge,  // Adjust text style as needed
                                modifier = Modifier.padding(4.dp)
                            )
                        }

                        // Second Image with Text
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            RoundImageWithBorder2(
                                image = image2,
                                choice = "image2",
                                isSelected = selectedChoice == "image2",
                                onClick = {
                                    selectedChoice = "image2"
                                }  // Set the second image as selected when clicked
                            )

                            Spacer(modifier = Modifier.height(8.dp))  // Space between image and Text

                            // Second Text below the image
                            Text(
                                text = "Soulmate",  // Static text for the first image
                                color = if (selectedChoice == "image2") Color.Black else Color.LightGray,  // Change color based on selection
                                fontWeight = if (selectedChoice == "image2") FontWeight.Bold else FontWeight.Normal,  // Bold if selected, normal if not
                                style = MaterialTheme.typography.bodyLarge,  // Adjust text style as needed
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(60.dp))
                    SearchBottom(
                        navControllerToLocation = mainNavControllerToLocation,
                        selectedChoice = selectedChoice,
                        permissionState = permissionState
                    )

                }
            }
        )

    }

    @Composable
    fun RoundImageWithBorder2(
        image: Painter,
        choice: String,
        isSelected: Boolean,
        borderWidth: Dp = 6.dp,
        surfaceSize: Dp = 90.dp,
        imagePadding: Dp = 12.dp,
        onClick: (String) -> Unit
    ) {
        // Determine border color based on selection
        val borderColor = if (isSelected) Orange else Color.LightGray

        Surface(
            modifier = Modifier
                .size(surfaceSize)
                .clip(CircleShape)
                .border(BorderStroke(borderWidth, borderColor), CircleShape)
                .clickable(onClick = { onClick.invoke(choice) }),
            color = Color.White
        ) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(imagePadding)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @SuppressLint("SuspiciousIndentation")
    @Composable
    fun SearchBottom(
        navControllerToLocation: NavController,
        selectedChoice: String,
        permissionState: MultiplePermissionsState
    ) {

        val paddingEnd = 45.dp
        val paddingStart = 45.dp
        val paddingBottom = 40.dp

        val localContext = LocalContext.current

        Button(
            onClick = {
                if (selectedChoice.isNotEmpty()) {

                    if (permissionState.allPermissionsGranted) {

                        navControllerToLocation.navigate(DatingNavigationItem.LocationScreen.route)

                    } else {

                        permissionState.launchMultiplePermissionRequest()
                    }

                } else {
                    // If the choice is empty
                    Toast.makeText(localContext, "You need to select 1 choice to continue", Toast.LENGTH_SHORT).show()
                }


            },
            modifier = Modifier
                .fillMaxWidth()
                .size(80.dp)
                .padding(start = paddingStart, end = paddingEnd, bottom = paddingBottom),
            shape = RoundedCornerShape(30),
            colors = ButtonColors(
                contentColor = Color.White,
                containerColor = Orange,
                disabledContentColor = Color.Gray,
                disabledContainerColor = Color.Gray
            )

        ) {
            Text(text = "Search", fontWeight = FontWeight.Bold)
        }
    }

}*/



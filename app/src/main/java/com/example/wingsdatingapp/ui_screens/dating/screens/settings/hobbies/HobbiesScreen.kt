package com.example.wingsdatingapp.ui_screens.dating.screens.settings.hobbies

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.api.NetworkResult
import com.example.wingsdatingapp.model.UserDataModel
import com.example.wingsdatingapp.ui_screens.dating.screens.navigation.DatingNavigationItem
import com.example.wingsdatingapp.ui_screens.dating.screens.settings.hobbies.viewmodel.HobbiesViewModel
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.MainViewModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.user_interest.model.UserInterestMainModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.user_interest.model.UserInterestModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserCredentialsViewModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserDetailHiltViewModel
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange
import com.example.wingsdatingapp.ui_screens.ui.theme.OrangeX
import com.example.wingsdatingapp.utils.LoadingAnimDialog
import com.example.wingsdatingapp.utils.selectedHobbies
import com.example.wingsdatingapp.utils.selectedHobbiesCategory


// TODO: This is my own version of hobbiesLayoutScreen for a more sleek design :D
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HobbiesLayout(
    navController: NavController,
    mainViewModel: MainViewModel,
    userDetailHiltViewModel: UserDetailHiltViewModel,
    hobbiesViewModel: HobbiesViewModel,
    userCredentialsViewModel: UserCredentialsViewModel
) {
    // Hides the bottomAppBar
    mainViewModel.hideBottomBar()

    val currentUser = userDetailHiltViewModel.loggedInUser.value
    val selectedHobbiesItem = hobbiesViewModel.selectedHobbies
    val context = LocalContext.current
    var animIsVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 46.dp)) {

        LoadingAnimDialog(isVisible = animIsVisible, modifier = Modifier.fillMaxSize())

        Row(
            verticalAlignment = Alignment.CenterVertically, // Align back button and title vertically in the center
            modifier = Modifier
                .fillMaxWidth() // Make the row take the full width
                .padding(bottom = 16.dp)
        ) {
            // Back button image
            Image(
                painter = painterResource(id = R.drawable.ic_back_btn),
                contentDescription = "Back button",
                modifier = Modifier
                    .clickable(
                        onClick = { navController.popBackStack() },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .size(44.dp) // Adjust the size of the back button if needed
            )

            // Title text next to the back button
            Spacer(modifier = Modifier.width(20.dp)) // Add space between the back button and title
            Text(
                text = "Hobbies", // Replace with your title
                fontSize = 35.sp,              // Set font size for the title
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(start = 8.dp)     // Optional padding for the title
            )
        }


        Spacer(Modifier.height(20.dp))


        // Display categories dynamically using the hobbyCategories list
        FlowRow(
            modifier = Modifier.fillMaxSize(),
            maxItemsInEachRow = 3,
            verticalArrangement = Arrangement.spacedBy(16.dp), // Spacing between rows
            horizontalArrangement = Arrangement.SpaceEvenly // Equal spacing between items
        ) {
            // Loop through the list using indices
            categorizedItemList().forEachIndexed { index, userHobbyItem ->
                // Define each category item using index
                CategoryItem(
                    imageRes = userHobbyItem.imageRes,
                    categoryName = userHobbyItem.categoryName
                ) {
                    selectedHobbies = userHobbyItem.list
                    selectedHobbiesCategory = userHobbyItem.categoryName
                    navController.navigate(DatingNavigationItem.SelectedHobbiesScreen.route)
                }
            }

            Button(
                onClick = {
                    if (selectedHobbiesItem.value.isNotEmpty()) {
                        val userData = UserDataModel(
                            id = currentUser?.id,
                            name = currentUser?.name,
                            email = currentUser?.email,
                            gender = currentUser?.gender,
                            hobbies = selectedHobbiesItem.value,
                            image_url = currentUser?.image_url,
                            phone_number = currentUser?.phone_number,
                            age = currentUser?.age,
                            bio = currentUser?.bio
                        )
                        userCredentialsViewModel.postUserData(context, userData) { result ->
                            when (result) {
                                NetworkResult.Loading -> {
                                    animIsVisible = true
                                }

                                is NetworkResult.Error -> {
                                    animIsVisible = false
                                }

                                is NetworkResult.Success -> {
                                    animIsVisible = false
                                    userDetailHiltViewModel.updateUser(userData)

                                    Toast.makeText(
                                        context,
                                        "Hobbies updated successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Please select some hobbies", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Orange)
            ) {
                Text(text = "Update Hobbies", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }


    }
}

// This contains the shape, size etc of the circle shaped options inside hobbies screen
@Composable
fun CategoryItem(imageRes: Int, categoryName: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(100.dp) // Fix the width to ensure uniform spacing
            .wrapContentHeight() // Allow height to adjust dynamically
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .size(70.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        onClick()
                    }), // Icon size remains fixed
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, color = Color.LightGray),

            ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = categoryName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxSize(),
                colorFilter = ColorFilter.tint(OrangeX)
            )
        }

        Spacer(modifier = Modifier.height(8.dp)) // Add spacing between Card and Text

        Text(
            text = categoryName,
            modifier = Modifier.padding(horizontal = 4.dp),
            style = TextStyle(
                color = Color.Black,
                fontSize = 12.sp
            ),
            fontWeight = FontWeight.W400,
            maxLines = 1, // Restrict the text to 2 lines
            overflow = TextOverflow.Ellipsis, // Truncate if text exceeds 2 lines
            textAlign = TextAlign.Center // Center align the text
        )
    }
}


private fun categorizedItemList(): List<UserInterestMainModel> {

    val exerciseAndSports = UserInterestMainModel(
        categoryName = "Exercise",
        imageRes = R.drawable.ic_weights, // Replace with appropriate category image
        list = listOf(
            UserInterestModel("Gym", R.drawable.ic_gym, false),
            UserInterestModel("Walking", R.drawable.ic_walking, false),
            UserInterestModel("Tennis", R.drawable.ic_tennis, false),
            UserInterestModel("Running", R.drawable.ic_running, false),
            UserInterestModel("Swimming", R.drawable.ic_swimming, false),
            UserInterestModel("Yoga", R.drawable.ic_yoga, false),
            UserInterestModel("Marathon", R.drawable.ic_marathon, false),
            UserInterestModel("Biking", R.drawable.ic_biking, false),
            UserInterestModel("Meditation", R.drawable.ic_meditation, false)

        )
    )

    val creativeArts = UserInterestMainModel(
        categoryName = "Games",
        imageRes = R.drawable.ic_gamescategories, // Replace with appropriate image
        list = listOf(
            UserInterestModel("FPS", R.drawable.ic_aim, false),
            UserInterestModel("MMO", R.drawable.ic_chess, false),
            UserInterestModel("RTS", R.drawable.ic_chess, false),
            UserInterestModel("MOBA", R.drawable.ic_moba, false),
            UserInterestModel("RPG", R.drawable.ic_rpg, false),
            UserInterestModel("Sandbox", R.drawable.ic_sanbox, false),
            UserInterestModel("Survival", R.drawable.ic_survival, false),
            UserInterestModel("Sport", R.drawable.ic_sports, false),
            UserInterestModel("Platformer", R.drawable.ic_platformer, false),


            )
    )


    val personalities = UserInterestMainModel(
        categoryName = "Personality",
        imageRes = R.drawable.ic_personalitypuzzle, // Replace with appropriate image
        list = listOf(

            UserInterestModel("Openness", R.drawable.ic_open, false),
            UserInterestModel("Conscientiousness", R.drawable.ic_cons, false),
            UserInterestModel("Extroversion", R.drawable.ic_extra, false),
            UserInterestModel("Agreeableness", R.drawable.ic_agree, false),
            UserInterestModel("Neuroticism", R.drawable.ic_neuro, false),


            )
    )

    val searchingFor = UserInterestMainModel(
        categoryName = "Searching for",
        imageRes = R.drawable.ic_searchfor, // Replace with appropriate image
        list = listOf(
            UserInterestModel("Monogamy", R.drawable.ic_searchfor, false),
            UserInterestModel("Open relation", R.drawable.ic_searchfor, false),
            UserInterestModel("Polyamory", R.drawable.ic_searchfor, false),
            UserInterestModel("Open to explore", R.drawable.ic_searchfor, false),

            )
    )

    val relationship = UserInterestMainModel(
        categoryName = "Relationship",
        imageRes = R.drawable.ic_relations, // Replace with appropriate image
        list = listOf(
            UserInterestModel("Serious relationship", R.drawable.ic_relations, false),
            UserInterestModel("Open for casual", R.drawable.ic_relations, false),
            UserInterestModel("Casual, open for serious relationship", R.drawable.ic_relations, false),
            UserInterestModel("Casual", R.drawable.ic_relations, false),
            UserInterestModel("New friends", R.drawable.ic_relations, false),
            UserInterestModel("Not sure yet", R.drawable.ic_relations, false)

            )
    )

    val familyPlanning = UserInterestMainModel(
        categoryName = "Family",
        imageRes = R.drawable.ic_baby_carriage, // Replace with appropriate image
        list = listOf(
            UserInterestModel("I want children", R.drawable.ic_baby_carriage, false),
            UserInterestModel("I don't want children", R.drawable.ic_baby_carriage, false),
            UserInterestModel("I have children and I want more", R.drawable.ic_baby_carriage, false),
            UserInterestModel("I have children and I don't want more", R.drawable.ic_baby_carriage, false),
            UserInterestModel("Haven't decided yet", R.drawable.ic_baby_carriage, false),

        )
    )

    val alcoholConsumption = UserInterestMainModel(
        categoryName = "Drinking",
        imageRes = R.drawable.ic_drink_alkohol, // Replace with appropriate image
        list = listOf(
            UserInterestModel("I don't drink", R.drawable.ic_drink_alkohol, false),
            UserInterestModel("Sober", R.drawable.ic_drink_alkohol, false),
            UserInterestModel("Social on weekends", R.drawable.ic_drink_alkohol, false),
            UserInterestModel("On special occasions", R.drawable.ic_drink_alkohol, false),
            UserInterestModel("Thinking of becoming sober", R.drawable.ic_drink_alkohol, false),
            UserInterestModel("Several days during the week", R.drawable.ic_drink_alkohol, false),

            )
    )


    val shoppingAndLifestyle = UserInterestMainModel(
        categoryName = "Miscellaneous",
        imageRes = R.drawable.ic_miscellaneous, // Replace with appropriate image
        list = listOf(
            UserInterestModel("Shopping", R.drawable.ic_shopping, false),
            UserInterestModel("Cooking", R.drawable.ic_cooking, false),
            UserInterestModel("Take Photo", R.drawable.ic_camera, false),
            UserInterestModel("Listen to Music", R.drawable.ic_music, false),

            )
    )



    return listOf(
        exerciseAndSports,
        creativeArts,
        personalities,
        shoppingAndLifestyle,
        searchingFor,
        relationship,
        familyPlanning,
        alcoholConsumption
    )
}


// TODO: This is the original code created by Umair
/*
@Composable
*/
/*fun HobbiesLayout(
    navController: NavController,
    mainViewModel: MainViewModel,
    userDetailHiltViewModel: UserDetailHiltViewModel,
    hobbiesViewModel: HobbiesViewModel,
    userCredentialsViewModel: UserCredentialsViewModel
) {
    // Hides the bottomAppBar
    mainViewModel.hideBottomBar()


    val currentUser = userDetailHiltViewModel.loggedInUser.value
    val selectedHobbiesItem = hobbiesViewModel.selectedHobbies
    val context = LocalContext.current
    var animIsVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 36.dp)) {
        LoadingAnimDialog(isVisible = animIsVisible, modifier = Modifier.fillMaxSize())

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
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

            Spacer(modifier = Modifier.width(20.dp))

            Text(text = "Hobbies", fontWeight = FontWeight.Bold, fontSize = 30.sp)
        }

        LazyVerticalGrid(columns = GridCells.Adaptive(114.dp)) {
            items(categorizedItemList().size) { index ->
                val userHobbyItem by remember { mutableStateOf(categorizedItemList()[index]) }
//                    Box(
//                        modifier = Modifier
//                            .padding(top = 10.dp, bottom = 10.dp)
//                            .background(Orange, RoundedCornerShape(16.dp))
//                            .padding(10.dp)
//                    ) {
//                        Text(
//                            text = userHobbyItem,
//                            color = Color.White,
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Medium
//                        )
//                    }

                HobbiesCategoryLayout(
                    imageRes = userHobbyItem.imageRes,
                    categoryName = userHobbyItem.categoryName
                ) {
                    selectedHobbies = userHobbyItem.list
                    selectedHobbiesCategory = userHobbyItem.categoryName
                    navController.navigate(DatingNavigationItem.SelectedHobbiesScreen.route)
                }
            }
        }
        Button(
            onClick = {
                if (selectedHobbiesItem.value.isNotEmpty()) {
                    val userData = UserDataModel(
                        id = currentUser?.id,
                        name = currentUser?.name,
                        email = currentUser?.email,
                        gender = currentUser?.gender,
                        hobbies = selectedHobbiesItem.value,
                        image_url = currentUser?.image_url,
                        phone_number = currentUser?.phone_number,
                        age = currentUser?.age,
                        bio = currentUser?.bio
                    )
                    userCredentialsViewModel.postUserData(context, userData) { result ->
                        when (result) {
                            NetworkResult.Loading -> {
                                animIsVisible = true
                            }

                            is NetworkResult.Error -> {
                                animIsVisible = false
                            }

                            is NetworkResult.Success -> {
                                animIsVisible = false
                                userDetailHiltViewModel.updateUser(userData)

                                Toast.makeText(
                                    context,
                                    "Hobbies updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "Please select some hobbies", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Orange)
        ) {
            Text(text = "Update Hobbies", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}*/


/*@Composable
private fun HobbiesCategoryLayout(imageRes: Int, categoryName: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xffFFE9D9), RoundedCornerShape(16.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    onClick()
                })
    ) {
        Image(
            painter = painterResource(id = imageRes), contentDescription = "",
            modifier = Modifier
                .height(78.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)), contentScale = ContentScale.FillBounds
        )

        Text(
            text = categoryName,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}*/

/*
private fun categorizedItemList(): List<UserInterestMainModel> {

    val exerciseAndSports = UserInterestMainModel(
        categoryName = "Exercise",
        imageRes = R.drawable.ic_weights, // Replace with appropriate category image
        list = listOf(
            UserInterestModel("Gym", R.drawable.ic_gym, false),
            UserInterestModel("Walking", R.drawable.ic_walking, false),
            UserInterestModel("Tennis", R.drawable.ic_tennis, false),
            UserInterestModel("Running", R.drawable.ic_running, false),
            UserInterestModel("Swimming", R.drawable.ic_swimming, false),
            UserInterestModel("Yoga", R.drawable.ic_yoga, false),
            UserInterestModel("Marathon", R.drawable.ic_marathon, false),
            UserInterestModel("Biking", R.drawable.ic_biking, false),
            UserInterestModel("Meditation", R.drawable.ic_meditation, false)

        )
    )

    val creativeArts = UserInterestMainModel(
        categoryName = "Games",
        imageRes = R.drawable.ic_weights, // Replace with appropriate image
        list = listOf(
            UserInterestModel("FPS", R.drawable.ic_aim, false),
            UserInterestModel("MMO", R.drawable.ic_chess, false),
            UserInterestModel("RTS", R.drawable.ic_chess, false),
            UserInterestModel("MOBA", R.drawable.ic_moba, false),
            UserInterestModel("RPG", R.drawable.ic_rpg, false),
            UserInterestModel("Sandbox", R.drawable.ic_sanbox, false),
            UserInterestModel("Survival", R.drawable.ic_survival, false),
            UserInterestModel("Sport", R.drawable.ic_sports, false),
            UserInterestModel("Platformer", R.drawable.ic_platformer, false),


            )
    )

    val travelAndAdventure = UserInterestMainModel(
        categoryName = "Zodiac Signs",
        imageRes = R.drawable.ic_weights, // Replace with appropriate image
        list = listOf(
            UserInterestModel("Aries", R.drawable.ic_aries, false),
            UserInterestModel("Cancer", R.drawable.ic_cancer, false),
            UserInterestModel("Libra", R.drawable.ic_libra, false),
            UserInterestModel("Capricorn", R.drawable.ic_capricon, false),
            UserInterestModel("Taurus", R.drawable.ic_taurus, false),
            UserInterestModel("Leo", R.drawable.ic_leo, false),

            UserInterestModel("Scorpio", R.drawable.ic_scorpio, false),
            UserInterestModel("Aquarius", R.drawable.ic_aquarius, false),
            UserInterestModel("Gemini", R.drawable.ic_gemini, false),
            UserInterestModel("Virgo", R.drawable.ic_virgo, false),
            UserInterestModel("Sagittarius", R.drawable.ic_sagittarius, false),
            UserInterestModel("Pisces", R.drawable.ic_pisces, false),

            )
    )

    val shoppingAndLifestyle = UserInterestMainModel(
        categoryName = "Miscellaneous",
        imageRes = R.drawable.ic_weights, // Replace with appropriate image
        list = listOf(
            UserInterestModel("Shopping", R.drawable.ic_shopping, false),
            UserInterestModel("Cooking", R.drawable.ic_cooking, false),
            UserInterestModel("Drinking", R.drawable.ic_drink, false),
            UserInterestModel("Take Photo", R.drawable.ic_camera, false),
            UserInterestModel("Listen to Music", R.drawable.ic_music, false),
            UserInterestModel("Drinking", R.drawable.ic_drink, false),
            UserInterestModel("Take Photo", R.drawable.ic_camera, false),

            )
    )

    val miscellaneous = UserInterestMainModel(
        categoryName = "Miscellaneous",
        imageRes = R.drawable.ic_weights, // Replace with appropriate image
        list = listOf(
            UserInterestModel("Running", R.drawable.ic_drink, false),
            UserInterestModel("Swimming", R.drawable.ic_video_game, false)
        )
    )

    return listOf(
        exerciseAndSports,
        creativeArts,
        travelAndAdventure,
        shoppingAndLifestyle,
        miscellaneous
    )
}*/

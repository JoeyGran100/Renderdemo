package com.example.wingsdatingapp.ui_screens.dating.screens.settings.hobbies

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.wingsdatingapp.utils.LoadingAnimDialog
import com.example.wingsdatingapp.utils.selectedHobbies
import com.example.wingsdatingapp.utils.selectedHobbiesCategory

@OptIn(ExperimentalMaterial3Api::class)
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
val selectedHobbiesItem=hobbiesViewModel.selectedHobbies
    val context= LocalContext.current
    var animIsVisible by remember{ mutableStateOf(false) }

    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 36.dp)) {
        LoadingAnimDialog(isVisible = animIsVisible, modifier = Modifier.fillMaxSize())

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
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

                HobbiesCategoryLayout(imageRes = userHobbyItem.imageRes, categoryName = userHobbyItem.categoryName){
                    selectedHobbies=userHobbyItem.list
                    selectedHobbiesCategory=userHobbyItem.categoryName
navController.navigate(DatingNavigationItem.SelectedHobbiesScreen.route)
                }
            }
        }
        Button(onClick = {
            if(selectedHobbiesItem.value.isNotEmpty()){
                val userData=UserDataModel(id = currentUser?.id,name = currentUser?.name, email = currentUser?.email,
                    gender = currentUser?.gender, hobbies = selectedHobbiesItem.value, image_url = currentUser?.image_url,
                    phone_number = currentUser?.phone_number, age = currentUser?.age, bio = currentUser?.bio)
              userCredentialsViewModel.postUserData(context,userData){result->
                  when(result){
                      NetworkResult.Loading->{
                         animIsVisible=true
                      }

                      is NetworkResult.Error ->{
                          animIsVisible=false
                      }
                      is NetworkResult.Success -> {
                          animIsVisible=false
                          userDetailHiltViewModel.updateUser(userData)
                          
                          Toast.makeText(context,"Hobbies updated successfully",Toast.LENGTH_SHORT).show()
                      }
                  }
              }
            }
            else{
                Toast.makeText(context,"Please select some hobbies",Toast.LENGTH_SHORT).show()
            }
        },
             modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Orange)) {
            Text(text = "Update Hobbies",color=Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun HobbiesCategoryLayout(imageRes: Int, categoryName: String,onClick:()->Unit) {
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
                .clip(RoundedCornerShape(16.dp))
            , contentScale = ContentScale.FillBounds
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
}

private fun categorizedItemList(): List<UserInterestMainModel> {
    val exerciseAndSports = UserInterestMainModel(
        categoryName = "Exercise",
        imageRes = R.drawable.ic_exercise_category, // Replace with appropriate category image
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
        imageRes = R.drawable.ic_arts_category, // Replace with appropriate image
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
        imageRes = R.drawable.ic_all_zodiacs_signs1, // Replace with appropriate image
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
        imageRes = R.drawable.ic_miscellaneous, // Replace with appropriate image
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
        imageRes = R.drawable.ic_travel_caegory, // Replace with appropriate image
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
}


package com.example.wingsdatingapp.ui_screens.onboarding.screens.preference


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.wingsdatingapp.model.UserPreferenceModel
import com.example.wingsdatingapp.ui.theme.LightTextColor
import com.example.wingsdatingapp.ui_screens.dating.screens.ToolTipBoxScreen
import com.example.wingsdatingapp.ui_screens.onboarding.screens.UserDetailsScreen.UserInfo
import com.example.wingsdatingapp.ui_screens.onboarding.screens.navigation.NavigationItem
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserCredentialsViewModel
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange
import com.example.wingsdatingapp.utils.LoadingAnimDialog


object SelectPreferenceScreen {

    @Composable
    fun UserPreferenceDetailsLayout(
        navController: NavController,
        userPreferenceViewModel: PreferenceViewModel,
    ) {

        val scope = rememberCoroutineScope()
        var showLoadingAnim by remember { mutableStateOf(false) }

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val gender = navBackStackEntry?.arguments?.getString("gender")
        val context = LocalContext.current

        // I added this 2025/01/05
        var preference by remember { mutableStateOf("") }

        var isAnimVisible by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxSize()
        ) {
            LoadingAnimDialog(isVisible = isAnimVisible, modifier = Modifier.fillMaxSize())

            Image(
                painter = painterResource(id = R.drawable.ic_back_btn),
                contentDescription = "",
                modifier = Modifier
                    .padding(top = 32.dp, bottom = 32.dp)
                    .clickable(indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        navController.popBackStack()
                    }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(), // Ensure the LazyColumn takes full height
                verticalArrangement = Arrangement.spacedBy(8.dp) // Add spacing between items if needed
            ) {


                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, // Align items vertically
                        modifier = Modifier
                            .fillMaxWidth() // Take up full width of the parent
                            .padding(bottom = 14.dp) // Apply bottom padding
                    ) {
                        // Text on the left
                        Text(
                            text = "Tell us about Your Preferences",
                            fontSize = 28.sp,
                            color = LightTextColor,
                            fontWeight = FontWeight.Bold
                        )

                        // Spacer to push the logo to the far right
                        Spacer(modifier = Modifier.weight(1f))

                        // Logo on the right
                        ToolTipBoxScreen(
                            title = "User Preferences",
                            btnTitle = "Dismiss",
                            bioText = "Don't worry, you can always edit this information later in profilescreen!"
                        )
                    }

                }

                // I added this 2025/01/05
                item {
                    UserInfo(
                        initialUserData = preference,
                        label = "Preference",
                        onValueChange = { preference = it }
                    )
                }


                item {

/*                    Button(
                        onClick = {
                            isAnimVisible = true

                            if (preference.isNotEmpty()) {

                                // I added this 2025/01/05
                                val userPreference = UserPreferenceModel(
                                    preference = preference
                                )

                                // I added this 2025/01/05
                                userPreferenceViewModel.postUserPreferenceData( context, userPreference
                                ) { result ->

                                    when(result) {

                                        NetworkResult.Loading -> {
                                            showLoadingAnim=true

                                        }

                                        is NetworkResult.Error ->{
                                            showLoadingAnim=false
                                        }

                                        is NetworkResult.Success -> {

                                            showLoadingAnim=false

                                        }


                                    }

                                    navController.navigate("${NavigationItem.UserDetails.route}/${gender}/${preference}")


                                }


                            } else {
                                Toast.makeText(
                                    context,
                                    "Please fill all the required fields",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }







                        },
                        colors = ButtonDefaults.buttonColors(Orange),
                        modifier = Modifier
                            .fillMaxWidth()
                        ,
                        shape = RoundedCornerShape(30),

                        ) {
                        Text(
                            text = "Continue",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }*/

                    Button(
                        onClick = {
                            isAnimVisible = true

                            if (gender?.isNotEmpty() == true && preference.isNotEmpty()

                            ) {

                                // I added this 2025/01/05
                                userPreferenceViewModel.postUserPreferenceData(
                                    context, UserPreferenceModel(preference = preference)
                                ) { result ->

                                    when (result) {

                                        is NetworkResult.Success -> {

                                            isAnimVisible = false
                                            showLoadingAnim = false

                                            Toast.makeText(
                                                context,
                                                "Preference successfully added!",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            navController.navigate("${NavigationItem.UserDetails.route}/${gender}/${preference}")


                                        }

                                        NetworkResult.Loading -> {
                                            showLoadingAnim = true

                                        }

                                        is NetworkResult.Error -> {
                                            showLoadingAnim = false

                                            Toast.makeText(
                                                context,
                                                "Error, Preference not added! Try again!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }


                                    }



                                }


                            } else {
                                Toast.makeText(
                                    context,
                                    "Please fill all the required fields",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        },
                        colors = ButtonDefaults.buttonColors(Orange),
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(30),

                        ) {
                        Text(
                            text = "Continue",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

            }


        }


    }

    @Composable
    private fun ContinueBtn(
        modifier: Modifier,
        selectedPreference: String,
        navController: NavController,
        onGenderNotSelected: () -> Unit
    ) {
        val context = LocalContext.current
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 90.dp, start = 10.dp, end = 10.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    if (selectedPreference.isNotEmpty()) {
                        navController.navigate("${NavigationItem.UserDetails.route}/{gender}/$selectedPreference")
                    } else {
                        Toast.makeText(
                            context,
                            "Please select your gender to continue",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.17f),
                colors = ButtonDefaults.buttonColors(
                    Orange
                ),
                shape = RoundedCornerShape(30),

                ) {
                Text(
                    text = "Continue",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

}

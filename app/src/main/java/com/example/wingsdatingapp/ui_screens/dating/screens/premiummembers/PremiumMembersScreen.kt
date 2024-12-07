package com.example.wingsdatingapp.ui_screens.dating.screens.premiummembers

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.MainViewModel


object PremiumScreen {

    @Composable
    fun PremiumMembersDetailLayout(
        mainViewModel: MainViewModel,
        navController: NavController,
    ) {


        mainViewModel.hideBottomBar()

        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 36.dp)) {

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
                    maxLines = 1,
                    text = "Premium Screen", // Replace with your title
                    fontSize = 28.sp,              // Set font size for the title
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(start = 8.dp)     // Optional padding for the title
                )
            }

            Text("Hello User! You have arrived at the Premium Members Screen. This screen is under work and does not function at the moment but will in the future. Feel free to check this out in the future you beautiful bastard! :) ")

        }
    }

}


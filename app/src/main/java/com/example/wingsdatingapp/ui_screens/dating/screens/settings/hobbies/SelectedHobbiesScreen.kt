package com.example.wingsdatingapp.ui_screens.dating.screens.settings.hobbies

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.ui_screens.dating.screens.settings.hobbies.viewmodel.HobbiesViewModel
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange
import com.example.wingsdatingapp.utils.selectedHobbies
import com.example.wingsdatingapp.utils.selectedHobbiesCategory

@Composable
fun SelectedHobbiesLayout(navController: NavController,hobbiesViewModel: HobbiesViewModel){
//    val selectedItemList = remember { mutableStateListOf<String>() }
    val selectedItems by hobbiesViewModel.selectedHobbies.collectAsState()
    val context= LocalContext.current
    Log.d("hobbieslist","Selected Hobbies $selectedItems")

    Column (modifier = Modifier
        .padding(start = 16.dp, end = 16.dp)
        .fillMaxSize(), ){
        Row(modifier = Modifier.padding(bottom = 36.dp), verticalAlignment = Alignment.CenterVertically){
            Image(
                painter = painterResource(id = R.drawable.ic_back_btn),
                contentDescription = "",
                modifier =
                Modifier
                    .padding(top = 36.dp)
                    .clickable {
                        navController.popBackStack()
                    }
                    .align(Alignment.CenterVertically)
            )
            Text(text = selectedHobbiesCategory.toString(), fontWeight = FontWeight.Bold, fontSize = 24.sp,
                modifier = Modifier
                    .padding(start = 24.dp)
                    .align(Alignment.CenterVertically))
        }

        LazyVerticalGrid(columns = GridCells.FixedSize(size = 143.dp)) {
            items(selectedHobbies.size) { index ->
                val currentItem = selectedHobbies[index]
                var isSelected by remember { mutableStateOf(selectedItems.contains(currentItem.itemName)) }
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
                                if (selectedItems.size <= 10) {
                                    hobbiesViewModel.toggleHobby(currentItem.itemName)
                                    isSelected = true
                                } else {
                                    Toast
                                        .makeText(
                                            context,
                                            "Cannot select more then 10 interests.",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            } else {
                                hobbiesViewModel.toggleHobby(currentItem.itemName)
                                isSelected = false
                            }


                        }, verticalAlignment = Alignment.CenterVertically
                ) {

                    // This changes the colors of the images from default color (pink-ish) to white color when user selects it
                    Image(
                        painter = painterResource(id = currentItem.resId),
                        contentDescription = "",
                        colorFilter = if (isSelected) ColorFilter.tint(Color.White) else null, // Apply white tint when selected
                        modifier = Modifier.size(24.dp) // Adjust size if needed
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
    }


}
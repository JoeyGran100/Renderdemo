package com.example.wingsdatingapp.ui_screens.dating.screens.personality

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.ui_screens.dating.screens.navigation.DatingNavigationItem
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange

@Composable
fun TakePersonalityTestLayout(navController: NavController) {
    var selectedPersonalityType by remember { mutableStateOf("") }
val context= LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Row(modifier = Modifier.padding(top = 32.dp, bottom = 67.dp)) {
            Image(painter = painterResource(id = R.drawable.ic_back_btn), contentDescription = "",
                modifier = Modifier.clickable(
                    onClick = {navController.popBackStack()},
                    indication = null, // Removes the click effect
                    interactionSource = remember { MutableInteractionSource() }
                ))
            Text(
                text = "How would you define yourself?",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 32.dp)
            )
        }
        OptionsLayout(Modifier.align(Alignment.CenterHorizontally),resId = R.drawable.ic_opt_a, text = "Openness",selectedPersonalityType){item->
            selectedPersonalityType=item
        }
        OptionsLayout(Modifier.align(Alignment.CenterHorizontally),resId = R.drawable.ic_opt_b, text = "Conscientiousness",selectedPersonalityType){item->
            selectedPersonalityType=item
        }
        OptionsLayout(Modifier.align(Alignment.CenterHorizontally),resId = R.drawable.ic_opt_c, text = "Extraversion",selectedPersonalityType){item->
            selectedPersonalityType=item
        }
        OptionsLayout(Modifier.align(Alignment.CenterHorizontally),resId = R.drawable.ic_opt_d, text = "Agreeableness",selectedPersonalityType){item->
            selectedPersonalityType= item
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            Toast.makeText(context,"Personality has been saved",Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
            Orange)) {
            Text(
                text = "Choose Personality",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }
    }
}

@Composable
private fun OptionsLayout(modifier: Modifier,resId: Int, text: String,selectedType:String,onClick:(String)->Unit,) {
   val isSelected= text==selectedType

    Box(
        modifier = modifier
            .width(268.dp)
            .height(56.dp)
            .padding(bottom = 17.dp).background(if(isSelected) Orange else Color.White,
                shape = RoundedCornerShape(12.dp))
            .clickable(
                onClick = { onClick(text) },
        indication = null, // Removes the click effect
        interactionSource = remember { MutableInteractionSource() }
    )
            .border(width = 1.dp, color = Orange, shape = RoundedCornerShape(12.dp))
    ) {
        Image(
            painter = painterResource(id = resId), contentDescription = "",
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterStart)
        )
        Text(
            text = text,
            color = if(isSelected) Color.White else Orange,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 44.dp)
        )
    }
}

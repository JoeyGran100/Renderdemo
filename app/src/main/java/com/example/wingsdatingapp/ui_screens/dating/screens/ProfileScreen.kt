package com.example.wingsdatingapp.ui_screens.dating.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.api.NetworkResult
import com.example.wingsdatingapp.model.UserDataModel
import com.example.wingsdatingapp.ui.theme.LightTextColor
import com.example.wingsdatingapp.ui_screens.onboarding.screens.UserDetailsScreen.saveUriToFile
import com.example.wingsdatingapp.ui_screens.onboarding.screens.navigation.NavigationItem
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserCredentialsViewModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserDetailHiltViewModel
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange
import com.example.wingsdatingapp.utils.currentUserImageUrl
import com.example.wingsdatingapp.utils.userEmail
import kotlin.math.log

object ProfileScreen {
    @Composable
    fun ProfileScreenLayout(userDetailHiltViewModel: UserDetailHiltViewModel,userCredentialsViewModel: UserCredentialsViewModel) {
        val loginUser = userDetailHiltViewModel.loggedInUser.value
        var name = remember{ mutableStateOf("") }
        var age = remember{ mutableStateOf("") }
        var phoneNo = remember{ mutableStateOf("") }
        var email = remember{ mutableStateOf("") }
        var bio = remember{ mutableStateOf("") }
        var showLoadingAnim by remember{ mutableStateOf(false) }
        var isEditable=remember{ mutableStateOf(false) }
        var editBtnText= remember{ mutableStateOf("Edit Profile") }
        val context= LocalContext.current
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
        var userImage by remember{ mutableStateOf(loginUser?.image_url) }
        val imagePlaceholder=if(loginUser?.gender=="Men") R.drawable.ic_men else R.drawable.ic_women

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let { it ->
                selectedImageUri = it
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 190.dp) // increasing the padding at the bottom making the button more visible and does not overlap with custom bottomNavBar
        ) {
            item {
                // Top section with orange background and profile image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                ) {
                    // Orange background
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(Orange)
                    )
                    // Profile title
                    Text(
                        text = "My Profile",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 50.dp)
                    )
                    // Profile image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                           model = if(selectedImageUri==null)userImage else selectedImageUri?:imagePlaceholder,
                            contentDescription = "",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(5.dp, Color.White, CircleShape)
                                .size(130.dp)
                                .background(Color.Black)
                                .align(Alignment.BottomCenter)
                                .clickable(indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        if(isEditable.value) launcher.launch("image/*")
                                    }),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
            }

            item {
                // Profile info section
                Row(modifier = Modifier.padding(16.dp)) {
                    ProfileInfoField(
                        label = "Full Name",
                        value = loginUser?.name.toString(),
                        iconResId = R.drawable.ic_profile_settings,
                        width = 204.dp,
                        name,
                        isEditable
                    )
                    Spacer(modifier = Modifier.width(16.dp)) // Add space between columns
                    ProfileInfoField(
                        label = "Age",
                        value = loginUser?.age.toString(),
                        iconResId = R.drawable.ic_profile,
                        width = 137.dp,
                        age,
                        isEditable
                    )
                }
            }

            item {
                // Additional profile info
                loginUser?.hobbies?.let { HobbiesLayout(it) }
                PhoneNoLayout(loginUser?.phone_number.toString(), phoneNo, isEditable)
                EmailLayout(loginUser?.email.toString(), email, isEditable)
                BioLayout(loginUser?.bio.toString(), bio, isEditable)
                Button(
                    onClick = {
                        val updatedUser=UserDataModel(
                            id = loginUser?.id,
                            name=name.value.toString(),
                            email = email.value.toString(),
                            gender = loginUser?.gender.toString(),
                            hobbies = loginUser?.hobbies,
                            image_url = userImage,
                            phone_number = phoneNo.value.toString(),
                            age = age.value.toString(),
                            bio = bio.value.toString()
                        )

                        if (editBtnText.value == "Edit Profile") {
                            editBtnText.value = "Save"
                            isEditable.value = true
                        } else {
                            editBtnText.value = "Edit Profile"
                            if(selectedImageUri!=null){
                                val file = selectedImageUri?.let { saveUriToFile(context, it) }

                                // Now post the image
                                file?.let {
                                    userCredentialsViewModel.postUserImage(it,
                                        loginUser?.email.toString(),
                                        onSuccess = { image ->

                                            if (image != null) {
                                                userImage = image
                                                Toast.makeText(
                                                    context,
                                                    "Image Uploaded to server",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        },
                                        onFailure = { exception ->

                                            Toast.makeText(
                                                context,
                                                "Error Uploading: ${exception.toString()}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        })
                                }
                            }

                            userCredentialsViewModel.postUserData(context, updatedUser){result->
                                showLoadingAnim = when(result){
                                    NetworkResult.Loading->{
                                        true
                                    }

                                    is NetworkResult.Error ->{
                                        false
                                    }

                                    is NetworkResult.Success -> {
                                        false
                                    }
                                }
                            }

                            userDetailHiltViewModel.updateUser(updatedUser)
                            isEditable.value = false
                            Log.d("loginuser","Active user ${loginUser.toString()}")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Orange),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp, start = 16.dp, bottom = 36.dp)
                ) {
                    Text(text = editBtnText.value, color = Color.White)
                }
            }
        }
    }
private fun uploadImageToServer(){

}
    @Composable
    private fun PhoneNoLayout(phoneNo: String,phoneMutableState:MutableState<String>,isEditable:MutableState<Boolean>) {
        LaunchedEffect(key1 = phoneNo) {
            phoneMutableState.value = phoneNo
        }
        Text(
            text = "Phone Number",
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp, start = 16.dp, end = 16.dp)
        )
/*
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
                .background(Color.White)
                .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = painterResource(id = R.drawable.ic_phone),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = phoneNo,
                    fontSize = 15.sp,
                    color = LightTextColor,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }*/

        OutlinedTextField(
            value = phoneMutableState.value,
            onValueChange = { phoneMutableState.value = it },
            enabled = isEditable.value,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone, // Replace with appropriate date icon
                    contentDescription = "Birth Date Icon"
                )
            }
            ,
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(12.dp)
                ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Black,
                disabledContainerColor = Color.White,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun EmailLayout(email: String,emailMutableState:MutableState<String>,isEditable:MutableState<Boolean>) {

        LaunchedEffect(key1 = email) {
            emailMutableState.value=email
        }
        Text(
            text = "Email Address",
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp, start = 16.dp)
        )

//        Box(
//            modifier = Modifier
//                .wrapContentHeight()
//                .fillMaxWidth()
//                .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
//                .background(Color.White)
//                .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
//                .padding(12.dp)
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//
//                Image(
//                    painter = painterResource(id = R.drawable.ic_email),
//                    contentDescription = "",
//                    modifier = Modifier.size(24.dp)
//                )
//                Text(
//                    text = email,
//                    fontSize = 15.sp,
//                    color = LightTextColor,
//                    modifier = Modifier.padding(start = 8.dp)
//                )
//            }
//        }
        OutlinedTextField(
            value = emailMutableState.value,
            enabled = isEditable.value,
            onValueChange = { emailMutableState.value = it },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email, // Replace with appropriate date icon
                    contentDescription = "Birth Date Icon"
                )
            }
            ,
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(12.dp)
                ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Black,
                disabledContainerColor = Color.White,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }

    @Composable
    private fun HobbiesLayout(hobbiesList: List<String>) {
        Text(
            text = "Hobbies",
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp, start = 16.dp)
        )

        LazyRow(modifier = Modifier.padding(bottom = 12.dp, end = 12.dp)) {
            items(hobbiesList.size) { index ->
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(start = 16.dp)
                        .background(Color.White)
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            text = hobbiesList[index],
                            fontSize = 15.sp,
                            color = LightTextColor
                        )
                    }
                }
            }
        }
//    Row(modifier = Modifier.padding(bottom = 12.dp)) {
//
//
//    }
    }

    @Composable
    private fun BioLayout(bio: String,bioMutableState:MutableState<String>,isEditable:MutableState<Boolean>) {

        LaunchedEffect(key1 = bio) {
            bioMutableState.value=bio
        }
        Text(
            text = "Bio",
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp, start = 16.dp)
        )
        OutlinedTextField(
            value = bioMutableState.value,
            onValueChange = { bioMutableState.value= it },
            enabled = isEditable.value,
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .height(185.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(12.dp)
                )
     ,       keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Black,
                disabledContainerColor = Color.White,
                disabledIndicatorColor = Color.Transparent
            )
        )
//        Box(
//            modifier = Modifier
//                .height(185.dp)
//                .fillMaxWidth()
//                .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
//                .background(Color.White)
//                .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
//                .padding(12.dp)
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//
//                Text(
//                    text = bio,
//                    fontSize = 15.sp,
//                    color = LightTextColor,
//                    maxLines = 3
//                )
//            }
//        }
    }

    @Composable
    fun ProfileInfoField(
        label: String,
        value: String,
        iconResId: Int,
        width: Dp,
        ageMutableState:MutableState<String>,isEditable:MutableState<Boolean>
    ) {
        LaunchedEffect(key1 = value) {
            ageMutableState.value=value
        }

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .width(width)
        ) {
            Text(
                text = label,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = ageMutableState.value,
                onValueChange = { ageMutableState.value = it },
                enabled = isEditable.value,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle, // Replace with appropriate date icon
                        contentDescription = "Birth Date Icon"
                    )
                }
                ,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(12.dp)
                    ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if(label=="Age") KeyboardType.Number else KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledTextColor = Color.Black,
                    disabledContainerColor = Color.White,
                    disabledIndicatorColor = Color.Transparent
                )
            )
           /* Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .background(Color.White)
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = iconResId),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = value,
                        fontSize = 15.sp,
                        color = LightTextColor,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }*/
        }
    }


}
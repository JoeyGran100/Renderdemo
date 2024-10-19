package com.example.wingsdatingapp.ui_screens.onboarding.screens

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.api.NetworkResult
import com.example.wingsdatingapp.model.UserImageModel
import com.example.wingsdatingapp.ui.theme.LightTextColor
import com.example.wingsdatingapp.ui_screens.onboarding.screens.navigation.NavigationItem
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserCredentialsViewModel
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange
import com.example.wingsdatingapp.utils.LoadingAnimDialog
import com.example.wingsdatingapp.utils.bitmapToString
import com.example.wingsdatingapp.utils.currentUserImageUrl
import com.example.wingsdatingapp.utils.updateUserPhoto
import com.example.wingsdatingapp.utils.uriToBitmap
import com.example.wingsdatingapp.utils.userEmail
import com.example.wingsdatingapp.utils.userPhoto
import java.io.File
import java.io.FileOutputStream

object UserDetailsScreen {
    @Composable
    fun UserDetailsLayout(
        navController: NavController,
        userCredentialsViewModel: UserCredentialsViewModel
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val gender = navBackStackEntry?.arguments?.getString("gender")
        val context = LocalContext.current

        var name by remember { mutableStateOf("") }
        var age by remember { mutableStateOf("") }
        var phoneNo by remember { mutableStateOf("") }
        var bio by remember { mutableStateOf("") }
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
        var isAnimVisible by remember { mutableStateOf(false) }

//        var profile_image by remember { mutableStateOf("") }
        val imagePlaceholder = if (gender == "Men") R.drawable.ic_men else R.drawable.ic_women
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let { it ->
                selectedImageUri = it
            }
        }
Column(modifier = Modifier
    .padding(start = 16.dp, end = 16.dp)
    .fillMaxSize()) {
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
            Text(
                text = "Tell us about yourself",
                fontSize = 28.sp,
                color = LightTextColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 14.dp)
            )
        }
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = selectedImageUri ?: imagePlaceholder,
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(10.dp)
                        .background(Color.Transparent, RoundedCornerShape(12.dp))
                        .border(0.5.dp, Color.Black, RoundedCornerShape(12.dp))
                        .padding(3.dp)
                        .clickable(indication = null, onClick = {
                            launcher.launch("image/*")
                        }, interactionSource = remember { MutableInteractionSource() }),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
        item {
            UserInfo(
                initialUserData = name,
                label = "Full name",
                onValueChange = { name = it }
            )
        }
        item {
            UserInfo(
                initialUserData = age,
                label = "Age",
                onValueChange = { age = it }
            )
        }
        item {
            UserInfo(
                initialUserData = phoneNo,
                label = "Phone Number",
                onValueChange = { phoneNo = it }
            )
        }
        item {
            UserInfo(
                initialUserData = bio,
                label = "Bio",
                onValueChange = { bio = it }
            )
        }
        item {

            Button(
                onClick = {
                    isAnimVisible = true
                    if (gender?.isNotEmpty() == true && name.isNotEmpty()
                        && age.isNotEmpty() && phoneNo.isNotEmpty() && bio.isNotEmpty()
                    ) {
                        val file = selectedImageUri?.let { saveUriToFile(context, it) }
                        file?.let {
                            userCredentialsViewModel.postUserImage(it,
                                userEmail.value.toString(),
                                onSuccess = { image ->
                                    isAnimVisible = false
                                    if (image != null) {
                                        currentUserImageUrl = image
                                        Toast.makeText(
                                            context,
                                            "Image Uploaded to server",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.navigate("${NavigationItem.UserInterset.route}/${gender}/${name}/${age}/${phoneNo}/${bio}")
                                    }
                                },
                                onFailure = { exception ->
                                    isAnimVisible = false
                                    Toast.makeText(
                                        context,
                                        "Error Uploading: ${exception.toString()}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate("${NavigationItem.UserInterset.route}/${gender}/${name}/${age}/${phoneNo}/${bio}")
                                })
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
                    .padding(bottom = 90.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Continue",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UserInfo(initialUserData: String, label: String, onValueChange: (String) -> Unit) {
        var userData by remember { mutableStateOf(initialUserData) }

        OutlinedTextField(
            value = userData,
            onValueChange = {
                userData = it
                onValueChange(it)
            },
            label = { Text(text = label) },
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth()
                .height(if (label == "Bio") 180.dp else Dp.Unspecified) // Conditional height
                .background(
                    Color(0xFFE6E6E6),
                    shape = RoundedCornerShape(10.dp)
                ),
            keyboardOptions = KeyboardOptions(
                keyboardType = when (label) {
                    "Age", "Phone Number" -> KeyboardType.Number
                    else -> KeyboardType.Text
                },
                imeAction = ImeAction.Next
            ),
            singleLine = label != "Bio", // Allow multiple lines if label is "Bio"
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )
    }

    fun saveUriToFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "temp_image_file${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}

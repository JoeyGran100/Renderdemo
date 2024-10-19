package com.example.wingsdatingapp.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.model.UserDataModel
import com.example.wingsdatingapp.model.UserImageModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.user_interest.model.UserInterestModel
import java.io.ByteArrayOutputStream
import java.io.IOException

private val _emailLiveData = MutableLiveData<String>()
val userEmail: LiveData<String> get() = _emailLiveData

fun updateUserEmail(newString: String) {
    _emailLiveData.value = newString
}

private val _photoLiveData = MutableLiveData<String>()
val userPhoto: LiveData<String> get() = _photoLiveData

fun updateUserPhoto(newString: String) {
    _photoLiveData.value = newString
}

private val _selectedUserMatch = MutableLiveData<UserDataModel?>(null)
val selectedUserMatch: LiveData<UserDataModel?> = _selectedUserMatch

fun updateSelectedUserMatch(selectedUserMatch: UserDataModel) {
    _selectedUserMatch.value = selectedUserMatch
}

private val _selectedUserImage=MutableLiveData<UserImageModel?>(null)
val selectedUserImage:LiveData<UserImageModel?> = _selectedUserImage

fun updateUserPhoto(newUserImage:UserImageModel){
    _selectedUserImage.value=newUserImage
}
var selectedHobbiesCategory:String=""

var selectedHobbies:List<UserInterestModel> = mutableListOf()

var selectedItemIndex:Int=1

var currentUserImageUrl=""

//SELECTED ITEMS FOR HOBBIES LIST
var selectedHobbiesList:MutableList<String> = mutableListOf()


@Composable
fun LoadingAnimDialog(
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        Dialog(onDismissRequest = {}) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimationView()
            }
        }
    }
}

@Composable
fun LoadingAnim(isVisible: Boolean){
    if (isVisible) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimationView()
        }
    }

}
@Composable
fun DatingAnim(){
    Box(
        modifier = Modifier
            .wrapContentSize()
            .fillMaxWidth()
            .background(Color.Transparent),
        contentAlignment = Alignment.TopCenter
    ) {
        DatingAnimView()
    }
}

@Composable
private fun LottieAnimationView() {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("loading_anim.json"))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition,
        progress,
        modifier = Modifier
            .size(50.dp)
            .background(Color.Transparent)
    )
}

@Composable
private fun DatingAnimView(){
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("dating_anim.json"))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition,
        progress,
        modifier = Modifier
            .fillMaxWidth().height(250.dp)
            .background(Color.Transparent)
    )
}

// Function to convert Bitmap to String
fun bitmapToString(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

// Function to convert String to Bitmap
fun stringToBitmap(encodedString: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        null
    }
}

 fun uriToBitmap(context: Activity, uri: Uri): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

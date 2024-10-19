package com.example.wingsdatingapp.ui_screens.onboarding.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.api.NetworkResult
import com.example.wingsdatingapp.auth.AuthProviderViewModel
import com.example.wingsdatingapp.auth.AuthResultContract
import com.example.wingsdatingapp.local.storage.SharedPrefManager
import com.example.wingsdatingapp.model.UserAuthModel
import com.example.wingsdatingapp.ui.theme.LightTextColor
import com.example.wingsdatingapp.ui_screens.dating.screens.DatingMainActivity
import com.example.wingsdatingapp.ui_screens.onboarding.screens.navigation.NavigationItem
import com.example.wingsdatingapp.ui_screens.ui.theme.DividerLineColor
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange
import com.example.wingsdatingapp.utils.LoadingAnimDialog
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserCredentialsViewModel
import com.example.wingsdatingapp.utils.updateUserEmail
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

object SignUpScreen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SignUpLayout(navController: NavController, viewModel: UserCredentialsViewModel,authProviderViewModel: AuthProviderViewModel) {
        val context = LocalContext.current
        val emailState = remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var showLoadingAnim by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        val signInRequestCode = 1
        var showPassword by remember { mutableStateOf(value = false) }

        val authResultLauncher =
            rememberLauncherForActivityResult(contract = AuthResultContract(authProviderViewModel.googleSignInClient(context))) {
                try {
                    val account = it?.getResult(ApiException::class.java)
                    if (account == null) {
//                        onError()
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show()
                    } else {
                        coroutineScope.launch {
//                            onGoogleSignInCompleted(account.idToken!!)
                            updateUserEmail(account.email.toString())
                            SharedPrefManager(context).saveSignInWithGoogle(true)
                            navController.navigate(NavigationItem.SelectGender.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                            Toast.makeText(context,"Successfully login",Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: ApiException) {
                    Toast.makeText(context,"Error occured",Toast.LENGTH_SHORT).show()
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Orange)
            ) {
                LoadingAnimDialog(isVisible = showLoadingAnim, modifier = Modifier.fillMaxSize())
                Text(
                    text = "Signup to Your Account",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 92.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            Color.White, shape = RoundedCornerShape(
                                topStart = 40.dp,
                                topEnd = 40.dp,
                                bottomEnd = 0.dp,
                                bottomStart = 0.dp
                            )
                        )
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Text(
                        text = "Please enter the credentials below to signup to your account.",
                        fontSize = 14.sp,
                        color = LightTextColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp, bottom = 24.dp),
                        textAlign = TextAlign.Start
                    )

                    //TEXT FIELDS
                    OutlinedTextField(
                        value = emailState.value,
                        onValueChange = { emailState.value = it },
                        label = { Text(text = "Email") },
                        modifier = Modifier
                            .padding(bottom = 15.dp)
                            .fillMaxWidth()
                            .background(
                                Color(0xFFE6E6E6),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        )
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFFE6E6E6),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        value = password,
                        onValueChange = { newText ->
                            password = newText
                        },
                        label = {
                            Text(text = "Password")
                        },
                        visualTransformation = if (showPassword) {

                            VisualTransformation.None

                        } else {

                            PasswordVisualTransformation()

                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        ),
                        trailingIcon = {
                            if (showPassword) {
                                IconButton(onClick = { showPassword = false }) {
                                    Icon(
                                        imageVector = Icons.Filled.Visibility,
                                        contentDescription = "hide_password"
                                    )
                                }
                            } else {
                                IconButton(
                                    onClick = { showPassword = true }) {
                                    Icon(
                                        imageVector = Icons.Filled.VisibilityOff,
                                        contentDescription = "hide_password"
                                    )
                                }
                            }
                        }
                    )

                    Button(
                        onClick = {
                            viewModel.postUserCredentials(context,UserAuthModel(emailState.value,password)){ result->
                                when (result) {
                                    is NetworkResult.Loading -> {
                                        // Show loading UI, if any
                                        showLoadingAnim=true
                                    }
                                    is NetworkResult.Success -> {
                                        // Show success message
                                        showLoadingAnim=false
                                        SharedPrefManager(context).saveSignInWithGoogle(false)
                                        Toast.makeText(context, "User added successfully", Toast.LENGTH_SHORT).show()
                                        navController.navigate(NavigationItem.SignIn.route)
                                    }
                                    is NetworkResult.Error -> {
                                        // Show error message
                                        showLoadingAnim=false
                                        Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }, colors = ButtonDefaults.buttonColors(Orange),
                        modifier = Modifier
                            .padding(top = 64.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Sign up", color = Color.White, fontSize = 16.sp)
                    }
                    DividerLine()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(), horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(end = 20.dp)
                                .size(50.dp)
                                .clickable {authResultLauncher.launch(signInRequestCode)   }
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_facebook),
                            contentDescription = "",
                            modifier =
                            Modifier.size(50.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = "Already have an account",
                            textDecoration = TextDecoration.Underline,
                            fontSize = 15.sp,
                            color = Orange,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 90.dp)
                                .align(Alignment.BottomCenter)
                                .clickable {
                                    navController.navigate(NavigationItem.SignIn.route)
                                }
                        )
                    }
                }
        }

    }

    @Composable
    private fun DividerLine() {
        Row(
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .width(84.dp)
                    .height(2.dp)
                    .background(DividerLineColor)
            )
            Text(
                text = "OR CONTINUE USING", color = Orange, fontSize = 14.sp,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp)
            )
            HorizontalDivider(
                modifier = Modifier
                    .width(84.dp)
                    .height(2.dp)
                    .background(DividerLineColor)
            )
        }
    }
    @Composable
    fun ButtonGoogleSignIn(
        onGoogleSignInCompleted: (String) -> Unit,
        onError: () -> Unit,
    ) {
        val coroutineScope = rememberCoroutineScope()
        val signInRequestCode = 1

        Button(
            onClick = { },
            modifier = Modifier
                .width(300.dp)
                .height(45.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google icon",
                    tint = Color.Unspecified,
                )
                Text(
                    text = "Access using Google",
                    color = Color.Black,
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
    }


}
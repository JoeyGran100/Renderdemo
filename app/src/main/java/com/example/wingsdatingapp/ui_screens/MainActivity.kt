package com.example.wingsdatingapp.ui_screens

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wingsdatingapp.auth.AuthProviderViewModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.navigation.NavigationItem
import com.example.wingsdatingapp.ui_screens.onboarding.screens.CodeVerificationScreen
import com.example.wingsdatingapp.ui_screens.onboarding.screens.ForgotPasswordScreen
import com.example.wingsdatingapp.ui_screens.onboarding.screens.SelectGenderScreen
import com.example.wingsdatingapp.ui_screens.onboarding.screens.SignInScreen
import com.example.wingsdatingapp.ui_screens.onboarding.screens.SignUpScreen
import com.example.wingsdatingapp.ui_screens.onboarding.screens.UserDetailsScreen
import com.example.wingsdatingapp.ui_screens.onboarding.screens.user_interest.UserInterestScreen
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserCredentialsViewModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserDetailHiltViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var userCredentialsViewModel: UserCredentialsViewModel
    private lateinit var userDetailHiltViewModel: UserDetailHiltViewModel
    private lateinit var authProviderViewModel: AuthProviderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        userCredentialsViewModel = ViewModelProvider(this)[UserCredentialsViewModel::class.java]
        userDetailHiltViewModel = ViewModelProvider(this)[UserDetailHiltViewModel::class.java]
        authProviderViewModel = ViewModelProvider(this)[AuthProviderViewModel::class.java]

        setContent {
            Navigation(navController = rememberNavController())
        }
    }

    @Composable
    private fun Navigation(navController: NavHostController) {

        NavHost(
            navController = navController,
            startDestination = NavigationItem.SignUp.route
        ) {

            composable(NavigationItem.SignUp.route) {
                SignUpScreen.SignUpLayout(
                    navController = navController,
                    viewModel = userCredentialsViewModel, authProviderViewModel
                )
            }
            composable(NavigationItem.SignIn.route) {
                SignInScreen.SignInLayout(
                    navController = navController,
                    userCredentialsViewModel,
                    authProviderViewModel
                )
            }
            composable(NavigationItem.ForgotPasswordScreen.route) {
                ForgotPasswordScreen.ForgotPasswordLayout(navController)
            }
            composable(NavigationItem.CodeVerification.route) {
                CodeVerificationScreen.CodeVerificationLayout(navController)
            }
            composable(NavigationItem.SelectGender.route) {
                SelectGenderScreen.SelectGenderLayout(navController)
            }
            composable(route = "${NavigationItem.UserDetails.route}/{gender}",
                arguments = listOf(
                    navArgument("gender") { type = NavType.StringType }
                )) {
                UserDetailsScreen.UserDetailsLayout(navController, userCredentialsViewModel)
            }
            composable(
                route = "${NavigationItem.UserInterset.route}/{gender}/{name}/{age}/{phoneNo}/{bio}",
                arguments = listOf(
                    navArgument("gender") { type = NavType.StringType },
                    navArgument("name") { type = NavType.StringType },
                    navArgument("age") { type = NavType.StringType },
                    navArgument("phoneNo") { type = NavType.StringType },
                    navArgument("bio") { type = NavType.StringType }
                )
            ) {
                UserInterestScreen.UserInterestLayout(
                    navController,
                    userDetailHiltViewModel,
                    userCredentialsViewModel,
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    // This does not function properly. This code was supposed to hide the default bottom app part for a smoother screen!
    private fun enableImmersiveMode() {
        // Ensure layout fits full-screen
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Use WindowInsetsController for Android 11+
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Fallback for older versions using the deprecated systemUiVisibility flags
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
        }
    }
}



package com.example.wingsdatingapp.ui_screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wingsdatingapp.auth.AuthProviderViewModel
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.DatingViewModel
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
}



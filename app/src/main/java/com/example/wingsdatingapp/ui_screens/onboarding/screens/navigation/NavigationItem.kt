package com.example.wingsdatingapp.ui_screens.onboarding.screens.navigation

sealed class NavigationItem(val route:String) {
    data object SignIn: NavigationItem(Screens.SignIn.name)
    data object SignUp: NavigationItem(Screens.SignUp.name)
    data object Home: NavigationItem(Screens.Home.name)
    data object ForgotPasswordScreen: NavigationItem(Screens.ForgotPassword.name)
    data object CodeVerification: NavigationItem(Screens.CodeVerification.name)
    data object SelectGender: NavigationItem(Screens.SelectGender.name)
    data object UserDetails: NavigationItem(Screens.UserDetails.name)
    data object UserInterset: NavigationItem(Screens.UserInterest.name)
}
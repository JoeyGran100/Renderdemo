package com.example.wingsdatingapp.ui_screens.dating.screens.navigation

sealed class DatingNavigationItem (val route:String){
    data object MatchingDetailProfile:DatingNavigationItem(DatingScreens.MatchingProfileDetails.name)
    data object HomeScreen:DatingNavigationItem(DatingScreens.HomeScreen.name)
    data object ProfileScreen:DatingNavigationItem(DatingScreens.ProfileScreen.name)
    data object SettingScreen:DatingNavigationItem(DatingScreens.SettingScreen.name)
    data object PersonalityTestInitialScreen:DatingNavigationItem(DatingScreens.PersonalityTestInitialScreen.name)
    data object PersonalityTestScreen:DatingNavigationItem(DatingScreens.PersonalityTestScreen.name)
    data object AccountDetailScreen:DatingNavigationItem(DatingScreens.AccountDetailScreen.name)
    data object HobbiesScreen:DatingNavigationItem(DatingScreens.HobbiesScreen.name)
    data object MatchesScreen:DatingNavigationItem(DatingScreens.MatchesScreen.name)
    data object ChatScreen:DatingNavigationItem(DatingScreens.ChatScreen.name)
    data object SelectedHobbiesScreen:DatingNavigationItem(DatingScreens.SelectedHobbiesScreen.name)
}
package com.example.wingsdatingapp.ui_screens.dating.screens.navigation

sealed class DatingNavigationItem (val route:String){
    data object MatchingDetailProfile:DatingNavigationItem(DatingScreens.MatchingProfileDetails.name)
    data object HomeScreen:DatingNavigationItem(DatingScreens.HomeScreen.name)
    data object ProfileScreen:DatingNavigationItem(DatingScreens.ProfileScreen.name)
    data object SettingScreen:DatingNavigationItem(DatingScreens.SettingScreen.name)
    data object MainSystemSettingsScreen:DatingNavigationItem(DatingScreens.MainDetailSettingsScreen.name)
    data object HobbiesScreen:DatingNavigationItem(DatingScreens.HobbiesScreen.name)
    data object MatchesScreen:DatingNavigationItem(DatingScreens.MatchesScreen.name)
    data object ChatScreen:DatingNavigationItem(DatingScreens.ChatScreen.name)
    data object SelectedHobbiesScreen:DatingNavigationItem(DatingScreens.SelectedHobbiesScreen.name)

    // I added this
    data object LocationScreen: DatingNavigationItem(DatingScreens.LocationScreen.name)
    data object PremiumMembersScreen: DatingNavigationItem(DatingScreens.PremiumScreen.name)

}
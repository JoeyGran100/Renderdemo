package com.example.wingsdatingapp.ui_screens.dating.screens

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wingsdatingapp.MyApplication
import com.example.wingsdatingapp.local.storage.SharedPrefManager
import com.example.wingsdatingapp.ui_screens.dating.screens.bottomnav.ButtonData
import com.example.wingsdatingapp.ui_screens.dating.screens.bottomnav.animatedNavigationBar
import com.example.wingsdatingapp.ui_screens.dating.screens.googlesearch.GoogleMapLayout
import com.example.wingsdatingapp.ui_screens.dating.screens.navigation.DatingNavigationItem
import com.example.wingsdatingapp.ui_screens.dating.screens.premiummembers.PremiumScreen
import com.example.wingsdatingapp.ui_screens.dating.screens.settings.SettingsDetailLayout
import com.example.wingsdatingapp.ui_screens.dating.screens.settings.HomeScreen
import com.example.wingsdatingapp.ui_screens.dating.screens.settings.UserMatchLayout
import com.example.wingsdatingapp.ui_screens.dating.screens.settings.hobbies.SelectedHobbiesLayout
import com.example.wingsdatingapp.ui_screens.dating.screens.settings.hobbies.viewmodel.HobbiesViewModel
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.DatingViewModel
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.MainViewModel
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.UserMatchViewModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserCredentialsViewModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserDetailHiltViewModel
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket

@AndroidEntryPoint
class DatingMainActivity : ComponentActivity() {
    private lateinit var datingViewModel: DatingViewModel
    private lateinit var socket: Socket
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userDetailHiltViewModel: UserDetailHiltViewModel
    private lateinit var userMatchViewModel: UserMatchViewModel
    private lateinit var userCredentialsViewModel: UserCredentialsViewModel
    private lateinit var hobbiesViewModel: HobbiesViewModel

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // This removes the default bottomAppBar and top bar with battery, internet icons etc.
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For API 30+ (Android 11 and above)
            windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())

        } else {
            // For older API levels (below 30)
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }


        val app = application as MyApplication
        socket = app.mSocket
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        userCredentialsViewModel = ViewModelProvider(this)[UserCredentialsViewModel::class.java]
        userDetailHiltViewModel = ViewModelProvider(this)[UserDetailHiltViewModel::class.java]
        datingViewModel = ViewModelProvider(this)[DatingViewModel::class.java]
        userMatchViewModel = ViewModelProvider(this)[UserMatchViewModel::class.java]

        hobbiesViewModel = ViewModelProvider(this)[HobbiesViewModel::class.java]

        userDetailHiltViewModel.getLoggedInUser()

        SharedPrefManager(this).saveCurrentUserState(true)


        setContent {
            val showBottomBar by mainViewModel.showBottomBar.collectAsState()
            var selectedItem by remember { mutableIntStateOf(0) }
            val navController = rememberNavController()


            val buttons = listOf(
                ButtonData("Search", Icons.Default.MyLocation),
                ButtonData("Profile", Icons.Default.Person),
                ButtonData("Home", Icons.Default.Home),
            )

            Scaffold(
                modifier = Modifier
//                    .padding(bottom = 45.dp) // This adjusts the bottomAppBar to a higher position
                    .fillMaxSize(),
                bottomBar = {
                    if (showBottomBar) {
                        selectedItem = animatedNavigationBar(
                            buttons = buttons,
                            barColor = Orange,
                            circleColor = Orange,
                            selectedColor = Color.White,
                            unselectedColor = Color.Black,
                        )
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Navigation(navController = navController)
                }
            }

// Handle Navigation Separately
            LaunchedEffect(selectedItem) {
                when (selectedItem) {
                    0 -> navController.navigate(DatingNavigationItem.HomeScreen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }

                    1 -> navController.navigate(DatingNavigationItem.ProfileScreen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }

                    2 -> navController.navigate(DatingNavigationItem.SettingScreen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }


        }
    }


    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun Navigation(navController: NavHostController) {

        // Permission state
        val permissionState = rememberMultiplePermissionsState(
            listOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        )

        var isDarkMode by remember { mutableStateOf(false) }

        NavHost(
            navController = navController,
            startDestination = DatingNavigationItem.HomeScreen.route
        ) {

            composable(DatingNavigationItem.HomeScreen.route) {
//                HomeScreen.HomeScreenLayout(mainViewModel, navController, permissionState)
                SearchScreen.SearchScreenLayout(
                    datingViewModel,
                    userDetailHiltViewModel,
                    navController,
                    mainViewModel
                )
            }
            composable(DatingNavigationItem.ProfileScreen.route) {
                ProfileScreen.ProfileScreenLayout(
                    userDetailHiltViewModel,
                    userCredentialsViewModel = userCredentialsViewModel
                )
            }
            composable(DatingNavigationItem.SettingScreen.route) {
                HomeScreen.HomeScreenLayout(
                    navController,
                    mainViewModel,
                    userDetailHiltViewModel
                )
            }
            composable(DatingNavigationItem.MatchingDetailProfile.route) {
                MatchProfileDetailLayout(navController, mainViewModel, userMatchViewModel)
            }
            composable(DatingNavigationItem.PremiumMembersScreen.route) {

                PremiumScreen.PremiumMembersDetailLayout(
                    mainViewModel,
                    navController,
                )

            }

            composable(DatingNavigationItem.ChatScreen.route) {
                ChatLayout(datingViewModel, userDetailHiltViewModel)
            }
            composable(DatingNavigationItem.SelectedHobbiesScreen.route) {
                SelectedHobbiesLayout(navController = navController, hobbiesViewModel)
            }
            composable(DatingNavigationItem.MainSystemSettingsScreen.route) {


                // TODO: Create a settingScreen here
                SettingsDetailLayout(mainViewModel)


            }
            composable(DatingNavigationItem.HobbiesScreen.route) {
                com.example.wingsdatingapp.ui_screens.dating.screens.settings.hobbies.HobbiesLayout(
                    navController,
                    mainViewModel,
                    userDetailHiltViewModel,
                    hobbiesViewModel,
                    userCredentialsViewModel
                )
            }
            composable(DatingNavigationItem.MatchesScreen.route) {

                UserMatchLayout(
                    navController = navController,
                    userListViewModel = datingViewModel,
                    userDetailHiltViewModel = userDetailHiltViewModel,
                    userMatchViewModel = userMatchViewModel,
                    mainViewModel = mainViewModel
                )
            }

            // This navigates the user to locationScreen

            composable(DatingNavigationItem.LocationScreen.route) {

                GoogleMapLayout(
                    mainViewModel = mainViewModel,
                    navController = navController,
                    permissionState = permissionState
                )
            }


        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewMatchProfileDetailLayout() {
//        MatchProfileDetailLayout()
    }
}

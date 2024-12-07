package com.example.wingsdatingapp.ui_screens.dating.screens.googlesearch

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.ui.theme.LightTextColor
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.MainViewModel
import com.example.wingsdatingapp.ui_screens.ui.theme.OrangeX
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GoogleMapLayout(
    mainViewModel: MainViewModel,
    navController: NavController,
    permissionState: MultiplePermissionsState

) {

    mainViewModel.hideBottomBar()

    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.custom_pulsing_lightblue
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,

    )

    var locationFetched by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var lat by remember { mutableDoubleStateOf(0.0) }
    var lng by remember { mutableDoubleStateOf(0.0) }
    var distanceInKm by remember { mutableDoubleStateOf(0.0) }
    var showDistance by remember { mutableStateOf(false) }


    // This takes the closest NAME of the place and displays it next to kilometer sign
    var closestLocationName by remember { mutableStateOf("") }
    var distanceToClosestLocation by remember { mutableDoubleStateOf(0.0) }

    val coroutineScope = rememberCoroutineScope()


    // Random list of mock addresses
    val addresses = listOf(
        Address("Bahria Town Lahore", LatLng(31.356779, 74.219391)),  // Bahria Town Lahore
        Address("Defence Lahore", LatLng(31.486622, 74.358747)),     // Defence Lahore
        Address("Islamabad", LatLng(33.684420, 73.047885)),          // Islamabad
        Address("Stockholm", LatLng(59.329323, 18.068581)),   // Stockholm, Sweden
        Address("Ö-vik", LatLng(63.29091, 18.71525)),   // Övik, Sweden
        Address("Ramsele", LatLng( 63.538418, 16.469857))   // Ramsele


    )

    LaunchedEffect(Unit) {
        delay(8000)
        showDistance = true

        coroutineScope.launch(Dispatchers.Main) {

            // I added this
            // Calculate the closest location and distance
            val closestAddress = findClosestAddress(lat, lng, addresses)
            closestAddress?.let {
                closestLocationName = it.name
                distanceToClosestLocation =
                    haversineDistance(lat, lng, it.latLng.latitude, it.latLng.longitude)
            }
        }

    }

    if (permissionState.allPermissionsGranted) {
        // Listen for location updates using your reusable method
        val fusedLocationClient =
            remember { LocationServices.getFusedLocationProviderClient(context) }

        LaunchedEffect(Unit) {

            coroutineScope.launch(Dispatchers.Main) {

                requestLocationUpdates(fusedLocationClient) { location ->
                    lat = location.latitude
                    lng = location.longitude
                    locationFetched = true
                }
            }

        }

        if (locationFetched) {
            val closestAddress = findClosestAddress(lat, lng, addresses)

            closestAddress?.let {
                // Calculate distance between current location and closest address
                val result = FloatArray(1)
                Location.distanceBetween(
                    lat, lng,  // Current location
                    it.latLng.latitude, it.latLng.longitude,  // Destination location
                    result
                )
                distanceInKm = (result[0] / 1000).toDouble()  // Convert to kilometers

                // This Displays the LocationScreen with name of location and distance of location in kilometers
                MapScreen(
                    name = closestLocationName,
                    distanceInKm = distanceInKm,
                    navController = navController
                )
            }
        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(start = 16.dp, end = 16.dp, top = 36.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // Define a common size modifier
                    val commonModifier = Modifier
                        .height(44.dp)  // Set a fixed height
                        .padding(horizontal = 8.dp)

                    // Text with common height
                    Text(
                        text = "Searching location...",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Serif,
                        color = Color.Black,
                        modifier = commonModifier,
                    )
                }

                // Lottie pulsating animation
                LottieAnimation(
                    composition = preloaderLottieComposition,
                    progress = { preloaderProgress },
                    modifier = Modifier.size(900.dp)
                )
            }

        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Location permission required")
            Button(onClick = { permissionState.launchMultiplePermissionRequest() }) {
                Text("Grant Permission")
            }
        }
    }
}

// Reusable function to request location updates
fun requestLocationUpdates(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationUpdate: (Location) -> Unit
) {
    // Use the builder pattern to create LocationRequest for newer versions
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
        .setMinUpdateIntervalMillis(2000L) // Fastest update interval
        .build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation ?: return
            onLocationUpdate(location)
        }
    }

    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )
}

// Original code
@SuppressLint("NewApi")
@Composable
fun MapScreen(
    name: String,
    distanceInKm: Double,
    navController: NavController,

    ) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 46.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically, // Align back button and title vertically in the center
            modifier = Modifier
                .fillMaxWidth() // Make the row take the full width
                .padding(bottom = 16.dp)
        ) {
            // Back button image
            Image(
                painter = painterResource(id = R.drawable.ic_back_btn),
                contentDescription = "Back button",
                modifier = Modifier
                    .clickable(
                        onClick = { navController.popBackStack() },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .size(44.dp)
            )

            // Title text next to the back button
            Spacer(modifier = Modifier.width(30.dp))
            Text(
                text = "Location Set!",
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(start = 8.dp),
                fontWeight = FontWeight.SemiBold,
            )

        }

        Spacer(Modifier.height(15.dp))
        Text(
            text = "Click On the following location. It will open in google maps. Go to said location and open the app again. Once open, it will start matchingmaking you with users. What are you waiting for, let the games begin!. Just WingIt!",
            fontSize = 16.sp,

            color = LightTextColor,
            fontWeight = FontWeight.Light
        )

        Spacer(Modifier.height(30.dp))


        val preloaderLottieComposition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                R.raw.ic_orange_phone_maps
            )
        )

        val preloaderProgress by animateLottieCompositionAsState(
            preloaderLottieComposition,
            iterations = LottieConstants.IterateForever,
            isPlaying = true
        )

        Box(
            modifier = Modifier
                .size(300.dp)
        ) {

            Column {

                Text(
                    text = buildAnnotatedString {
                        append("Closest meeting-point is: ")

                        // Style for the name
                        withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Bold)) {
                            append(name)
                        }

                        append(", ")

                        // Style for the distance in km
                        withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Bold)) {
                            append("%.2f km".format(distanceInKm))
                        }

                        append(" away from you!")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 21.sp,
                    color = Color.Black,
                )

                Spacer(Modifier.height(10.dp))

                // This is the lottie animation with a phone inside the Box!
                LottieAnimation(
                    composition = preloaderLottieComposition,
                    progress = { preloaderProgress },
                    modifier = Modifier.fillMaxSize()

                )
            }
        }

        Spacer(Modifier.height(30.dp))

        OpenGoogleMapsButton(name)

    }
}

// Haversine formula to calculate distance between two LatLng points
fun haversineDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
    val R = 6371e3 // Radius of the Earth in meters
    val phi1 = lat1 * Math.PI / 180 // φ, λ in radians
    val phi2 = lat2 * Math.PI / 180
    val deltaPhi = (lat2 - lat1) * Math.PI / 180
    val deltaLambda = (lng2 - lng1) * Math.PI / 180

    val a = sin(deltaPhi / 2) * sin(deltaPhi / 2) +
            cos(phi1) * cos(phi2) *
            sin(deltaLambda / 2) * sin(deltaLambda / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return R * c // Distance in meters
}

// Function to find the closest address
fun findClosestAddress(currentLat: Double, currentLng: Double, addresses: List<Address>): Address? {
    var closestAddress: Address? = null
    var shortestDistance = Double.MAX_VALUE

    for (address in addresses) {
        val distance = haversineDistance(
            currentLat,
            currentLng,
            address.latLng.latitude,
            address.latLng.longitude
        )
        if (distance < shortestDistance) {
            shortestDistance = distance
            closestAddress = address
        }
    }

    return closestAddress
}


@Composable
fun OpenGoogleMapsButton(address: String? = null) {
    val context = LocalContext.current

    Button(
        onClick = {
            val gmmIntentUri = when {

                // This takes the closest address to the user, creates a route and displays it to the user with default google maps
                address != null -> {

                    // Otherwise, use the address as a query
                    Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$address")
                }

                else -> return@Button // If neither is provided, do nothing
            }

            // Create an intent with action type ACTION_VIEW
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                // Specify Google Maps package to handle the intent
                setPackage("com.google.android.apps.maps")
            }

            // Check if there's an activity to handle the intent
            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)
            } else {
                // Optional: Show a message to the user if Google Maps is not available
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .size(93.dp)
            .padding(start = 40.dp, end = 40.dp, bottom = 40.dp),
        shape = RoundedCornerShape(30),
        colors = ButtonColors(
            contentColor = Color.White,
            containerColor = OrangeX,
            disabledContentColor = Color.Gray,
            disabledContainerColor = Color.Gray
        )

    ) {
        Text(text = "Go To Location", fontWeight = FontWeight.Bold)
    }
}
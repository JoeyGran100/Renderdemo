package com.example.wingsdatingapp.ui_screens.dating.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.Send
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.api.NetworkResult
import com.example.wingsdatingapp.model.MessageModel
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.DatingViewModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserDetailHiltViewModel
import com.example.wingsdatingapp.utils.LoadingAnim
import com.example.wingsdatingapp.utils.selectedUserMatch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wingsdatingapp.ui_screens.ui.theme.ChatBubbleColor2
import com.example.wingsdatingapp.ui_screens.ui.theme.OnlineButtonColor
import com.example.wingsdatingapp.ui_screens.ui.theme.OrangeSendButton
import com.example.wingsdatingapp.ui_screens.ui.theme.OrangeX
import com.example.wingsdatingapp.ui_screens.ui.theme.ReportButtonColor



// TODO: OBSERVE - THIS CLASS WAS NOT OPEN BEFORE. IT WAS JUST A CLASS -> CHANGE THIS BACK ONCE SCREEN IS FINISHED FOR IT TO BE BACK IN ITS ORIGINAL FORM!!!
private lateinit var userDetailHiltViewModel: UserDetailHiltViewModel
private lateinit var datingViewModel: DatingViewModel

@Preview(showBackground = true)
@Composable
fun PreviewOfChatLayout() {

//    ChatLayout(
//        userDetailHiltViewModel = userDetailHiltViewModel,
//        datingViewModel = datingViewModel,
//        navController = rememberNavController()
//    )

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatLayout(navController: NavController, datingViewModel: DatingViewModel, userDetailHiltViewModel: UserDetailHiltViewModel) {

    var context = LocalContext.current
    // State to track the expanded/collapsed state of the toolbar
    var isCollapsed by remember { mutableStateOf(false) }

    // Focus state for the OutlinedTextField
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

//    val scope = rememberCoroutineScope()

//    var message by remember { mutableStateOf("") }

    var messages = datingViewModel.messages.collectAsState().value

    // State to handle the input text for OutlinedTextField
    var inputText by remember { mutableStateOf("") }

    val currentActiveUser = userDetailHiltViewModel.loggedInUser.value

    val selectedUser = selectedUserMatch.value
    var showLoadingAnim by remember { mutableStateOf(false) }

    val messageModel = MessageModel(
        receiver_email = currentActiveUser?.email ?: "",
        sender_email = selectedUser?.email ?: "",
        message = inputText,

        // TODO: I re-activated these commands
        receiver_id = currentActiveUser?.id ?: 0,
        sender_id = selectedUser?.id ?: 0,
        timestamp = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.getDefault()).format(Date()) // Set current time

//                timestamp = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.getDefault()).format(Date()) // Set current time


//        receiver_id = 2,
//        sender_id = 1,
    )

    // Load the image resource
    val imageBitmap = ImageBitmap.imageResource(
        id = R.drawable.ic_blue_background
    )

    LaunchedEffect(Unit) {
        datingViewModel.getChatsBetweenUsers(
            currentActiveUser?.email.toString(),
            selectedUser?.email.toString()
        )
    }

// TODO: I CHANGED THIS FROM: Check chatgpt why I did it!!
//    result.data += messageModel -> messages


    Scaffold(

        // Action for topAppBar Only. Body is below!!

        topBar = {

            TopAppBar(
                title = {

                },
                expandedHeight = 0.dp,
                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent), // Transparent background for pattern
                modifier = Modifier
                    .height(130.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White
                            ) // Optional gradient blend
                        )
                    ),
                actions = {

                    Box(Modifier.fillMaxSize()) {

                        PatternedTopAppBar1000(
                            name = selectedUser?.name.toString(),
                            image = painterResource(id = if (selectedUser?.gender == "Men") R.drawable.ic_men else R.drawable.ic_women),
                            availability = "Today kl: 14:22",
                            sizeOfCircle = 50.dp,
                            btnSize = 40.dp,
                            toolbarHeight = 130.dp,
                            imageBitmap = imageBitmap,
                            modifier = Modifier,
                            onBackPressed = {

                                navController.popBackStack()
                            }
                        )

                    }

                }
            )

        }
    ) {

        // Body of chat Layout is here!!
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            // Add the blue background image
            Image(
                painter = painterResource(R.drawable.ic_blue_background),
                contentDescription = null,
                contentScale = ContentScale.Crop, // Adjust the image to cover the entire box
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(5.dp)
            ) {

                // TODO: Fix so that loading animation is in front of background image!!

                when (val result = datingViewModel.chatMessages.value) {
                    is NetworkResult.Error -> {
                        showLoadingAnim = false
                        Log.d("userChat", "Error ${result.message.toString()}.")
                    }

                    NetworkResult.Loading -> {
                        showLoadingAnim = true
                        Log.d("userChat", "Loading.... ${result.toString()}")
                    }

                    is NetworkResult.Success -> {
                        showLoadingAnim = false
                        Log.d("userChat", "Success ${result.data.toString()}.")

                        LaunchedEffect(Unit) {
                            selectedUser?.id?.let {
                                datingViewModel.receiveMessages(it) { messageModel ->
                                    result.data += messageModel
                                }
                            }
                        }

                        LazyColumn(
                            state = rememberLazyListState(),
                            reverseLayout = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f) // Ensures it shrinks when the keyboard appears
                                .padding(horizontal = 16.dp, vertical = 8.dp) // Minimal padding
                        ) {
                            items(result.data.reversed()) { msg ->
                                if (currentActiveUser?.email == msg.sender_email) {

                                    // Sender's Message Layout

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(4.dp),
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .background(
                                                    Color(0xffF3F3F3),
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .padding(12.dp)
                                                .fillMaxWidth(0.8f)
                                        ) {


                                            Text(
                                                text = msg.message.toString(),
                                                color = Color.Black,
                                                fontSize = 16.sp
                                            )

                                            Spacer(modifier = Modifier.height(4.dp)) // Spacing between message and timestamp


                                            Text(
                                                text = formatTimestamp(msg.timestamp ?: ""), // Use the timestamp in the correct format
                                                // This is a test that can be deleted!
//                                              text = messageModel.timestamp.toString(), // Use the timestamp in the correct format
                                                color = Color.Black,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier
                                                    .align(Alignment.End) // Align timestamp to the right
                                                    .padding(end = 5.dp)
                                            )
                                        }
                                    }


                                } else {


                                    // Receiver's Message Layout

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(4.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .background(
                                                    ChatBubbleColor2,
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .padding(12.dp)
                                                .fillMaxWidth(0.8f)
                                        ) {

                                            Text(
                                                text = msg.message.toString(),
                                                color = Color.Black,
                                                fontSize = 16.sp
                                            )

                                            Spacer(modifier = Modifier.height(4.dp)) // Spacing between message and timestamp


                                            Text(
                                                text = messageModel.timestamp.toString(), // Use the timestamp in the correct format
                                                color = Color.Black,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier
                                                    .align(Alignment.End) // Align timestamp to the right
                                                    .padding(end = 5.dp)

                                            )
                                        }
                                    }
                                }

                            }
                        }

                        // Bottom Row for Input
                        Row(
                            verticalAlignment = Alignment.CenterVertically, // Align contents vertically centered
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp) // Add bottom padding to the entire Row
                        ) {

                            // My Previous textfield 2024/12/31
                            OutlinedTextField(
                                value = inputText,
                                onValueChange = { newText ->
                                    inputText = newText // Update state
                                },
                                placeholder = {
                                    Text(text = "Your message")
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.White,
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = OrangeX, // Border color when focused
                                    unfocusedIndicatorColor = Color.Gray // Border color when unfocused
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .focusRequester(focusRequester)
                                    .onFocusChanged { focusState ->
                                        isCollapsed = focusState.isFocused
                                    }
                                    .padding(
                                        start = 20.dp,
                                        end = 10.dp,
                                        bottom = 20.dp
                                    ) // Removed `bottom` padding
                                    .weight(1f),

                                keyboardOptions = KeyboardOptions.Default.copy(),
                                keyboardActions = KeyboardActions(onDone = {

                                    result.data += messages

                                    if (inputText.isNotBlank()) {
                                        datingViewModel.addMessage(messageModel)
                                        inputText = "" // Clear the text field after sending
                                        focusManager.clearFocus()

                                    }
                                })
                            )

                            Spacer(Modifier.width(10.dp))

                            Box(
                                modifier = Modifier
                                    .padding(bottom = 20.dp) // External padding
                                    .size(55.dp) // Matches the height of the TextField
                                    .offset(x = (-5).dp) // Move the button slightly to the left
                                    .clip(CircleShape)
                                    .background(OrangeSendButton) // Background color of the circle

                            ) {

                                Icon(
                                    imageVector = Icons.AutoMirrored.Sharp.Send,
                                    contentDescription = "Send",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.Center)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() },
                                            onClick = {
                                                if (inputText.isNotBlank()) {

                                                    result.data += messages

                                                    // Send message via ViewModel
                                                    datingViewModel.sendMessage(messageModel)

                                                    // Clear the input text
                                                    inputText = ""

                                                    // Optional: Log or handle timestamp
                                                    Log.d(
                                                        "currentTimeStamp",
                                                        messageModel.timestamp.toString()
                                                    )
                                                }
                                            }
                                        )
                                )

                            }

                        }


                    }

                    null -> {
                        Log.d("userChat", "Null: ${result}.")
                    }
                }


            }
        }

        // Loading Animation
        if (showLoadingAnim) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)) // Semi-transparent background for emphasis
                    .zIndex(1f), // Ensure it's on top of other UI elements
                contentAlignment = Alignment.Center // Center the animation
            ) {
                LoadingAnim(isVisible = true)
            }
        }

    }


}


@Composable
fun OnlineDotIcon(size: Dp = 8.dp, color: Color = OnlineButtonColor) {
    Box(
        modifier = Modifier
            .size(size) // Set the size of the dot
            .clip(CircleShape) // Clip it to a circular shape
            .background(color) // Apply the green color as the background
    )
}


private fun formatTimestamp(timestamp: String): String {
    // Input format that matches the given timestamp
    val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.getDefault())

    // Output format to show hours and minutes (HH:mm)
    val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    return try {
        // Parse the timestamp string to a Date object
        val date = inputFormat.parse(timestamp)

        // If parsing is successful, return the formatted timestamp
        if (date != null) {
            return outputFormat.format(date)
        } else {
            ""
        }
    } catch (e: Exception) {
        e.printStackTrace()
        "" // Return empty string in case of error
    }
}






@Composable
fun CheckboxWithLabel(
    label1: String,
    label2: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Checkbox
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
        )

        // Label Text
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = ReportButtonColor)) { // Color for "Remember"
                    append(label1)
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) { // Color for "Wing"
                    append(label2)
                }
            },
            fontSize = 16.sp,
            color = ReportButtonColor,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
fun OnlineDotIcon2(size: Dp = 8.dp, color: Color = OnlineButtonColor) {
    Box(
        modifier = Modifier
            .size(size) // Set the size of the dot
            .clip(CircleShape) // Clip it to a circular shape
            .background(color) // Apply the green color as the background
    )
}

// This was created before 2025/01/01
/*@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatLayout(datingViewModel: DatingViewModel, userDetailHiltViewModel: UserDetailHiltViewModel) {

    // State to track the expanded/collapsed state of the toolbar
    var isCollapsed by remember { mutableStateOf(false) }

    // Animate the toolbar height change
    val toolbarHeight by animateDpAsState(targetValue = if (isCollapsed) 100.dp else 150.dp)

    // Focus state for the OutlinedTextField
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

//    val scope = rememberCoroutineScope()

//    var message by remember { mutableStateOf("") }

    var messages = datingViewModel.messages.collectAsState().value

    // State to handle the input text for OutlinedTextField
    var inputText by remember { mutableStateOf("") }

    val currentActiveUser = userDetailHiltViewModel.loggedInUser.value

    val selectedUser = selectedUserMatch.value
    var showLoadingAnim by remember { mutableStateOf(false) }

    val messageModel = MessageModel(
        receiver_email = currentActiveUser?.email ?: "",
        sender_email = selectedUser?.email ?: "",
        message = inputText,

        // TODO: I re-activated these commands
        receiver_id = currentActiveUser?.id ?: 0,
        sender_id = selectedUser?.id ?: 0,
        timestamp = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.getDefault()).format(Date()) // Set current time

//        receiver_id = 2,
//        sender_id = 1,
    )

    // Load the image resource
    val imageBitmap = ImageBitmap.imageResource(
        id = R.drawable.ic_blue_background
    )

    LaunchedEffect(Unit) {
        datingViewModel.getChatsBetweenUsers(
            currentActiveUser?.email.toString(),
            selectedUser?.email.toString()
        )
    }

    Scaffold(

        // Action for topAppBar Only. Body is below!!

        topBar = {

            TopAppBar(
                title = {

                },
                expandedHeight = if (isCollapsed) 4.dp else 0.dp,
                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent), // Transparent background for pattern
                modifier = Modifier
                    .height(toolbarHeight)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White
                            ) // Optional gradient blend
                        )
                    ),
                actions = {

                    Box(Modifier.fillMaxSize()) {

                        if (isCollapsed) {
                            // If it is collapsed

                            PatternedTopAppBar(
                                name = selectedUser?.name.toString(),
                                image = painterResource(id = if (selectedUser?.gender == "Men") R.drawable.ic_men else R.drawable.ic_women),
                                availability = "Online",
                                sizeOfCircle = 70.dp,
                                btnSize = 44.dp,
                                toolbarHeight = 110.dp,
                                imageBitmap = imageBitmap,
                                modifier = Modifier.height(toolbarHeight)
                            )

                        } else {
                            // If it is not collapsed

                            PatternedTopAppBar(
                                name = selectedUser?.name.toString(),
                                image = painterResource(id = if (selectedUser?.gender == "Men") R.drawable.ic_men else R.drawable.ic_women),
                                availability = "Online",
                                sizeOfCircle = 80.dp,
                                btnSize = 54.dp,
                                toolbarHeight = 150.dp,
                                imageBitmap = imageBitmap,
                                modifier = Modifier.height(toolbarHeight)
                            )

                        }
                    }

                }
            )

        }
    ) {

        // Body of chat Layout is here!!
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            // Add the blue background image
            Image(
                painter = painterResource(R.drawable.ic_blue_background),
                contentDescription = null,
                contentScale = ContentScale.Crop, // Adjust the image to cover the entire box
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(5.dp)
            ) {

                // TODO: Fix so that loading animation is in front of background image!!

                when (val result = datingViewModel.chatMessages.value) {
                    is NetworkResult.Error -> {
                        showLoadingAnim = false
                        Log.d("userChat", "Error ${result.message.toString()}.")
                    }

                    NetworkResult.Loading -> {
                        showLoadingAnim = true
                        Log.d("userChat", "Loading.... ${result.toString()}")
                    }

                    is NetworkResult.Success -> {
                        showLoadingAnim = false
                        Log.d("userChat", "Success ${result.data.toString()}.")

                        LaunchedEffect(Unit) {
                            selectedUser?.id?.let {
                                datingViewModel.receiveMessages(it) { messageModel ->
                                    result.data += messageModel
                                }
                            }
                        }

                        LazyColumn(
                            state = rememberLazyListState(),
                            reverseLayout = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f) // Ensures it shrinks when the keyboard appears
                                .padding(horizontal = 16.dp, vertical = 8.dp) // Minimal padding
                        ) {
                            items(result.data.reversed()) { msg ->
                                if (currentActiveUser?.email == msg.sender_email) {

                                    // Sender's Message Layout

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(4.dp),
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .background(
                                                    Color(0xffF3F3F3),
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .padding(12.dp)
                                                .fillMaxWidth(0.8f)
                                        ) {


                                            Text(
                                                text = msg.message.toString(),
                                                color = Color.Black,
                                                fontSize = 16.sp
                                            )

                                            Spacer(modifier = Modifier.height(4.dp)) // Spacing between message and timestamp


                                            Text(
                                                text = formatTimestamp(msg.timestamp ?: ""), // Use the timestamp in the correct format
                                                color = Color.Black,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier
                                                    .align(Alignment.End) // Align timestamp to the right
                                                    .padding(end = 5.dp)
                                            )
                                        }
                                    }


                                } else {


                                    // Receiver's Message Layout

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(4.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .background(
                                                    ChatBubbleColor2,
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .padding(12.dp)
                                                .fillMaxWidth(0.8f)
                                        ) {

                                            Text(
                                                text = msg.message.toString(),
                                                color = Color.Black,
                                                fontSize = 16.sp
                                            )

                                            Spacer(modifier = Modifier.height(4.dp)) // Spacing between message and timestamp


                                            Text(
                                                text = formatTimestamp(msg.timestamp ?: ""), // Use the timestamp in the correct format
                                                color = Color.Black,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier
                                                    .align(Alignment.End) // Align timestamp to the right
                                                    .padding(end = 5.dp)

                                            )
                                        }
                                    }
                                }

                            }
                        }

                        // Bottom Row for Input
                        Row(
                            verticalAlignment = Alignment.CenterVertically, // Align contents vertically centered
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp) // Add bottom padding to the entire Row
                        ) {

                            // My Previous textfield 2024/12/31
                            OutlinedTextField(
                                value = inputText,
                                onValueChange = { newText ->
                                    inputText = newText // Update state
                                },
                                placeholder = {
                                    Text(text = "Your message")
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.White,
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = OrangeX, // Border color when focused
                                    unfocusedIndicatorColor = Color.Gray // Border color when unfocused
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .focusRequester(focusRequester)
                                    .onFocusChanged { focusState ->
                                        isCollapsed = focusState.isFocused
                                    }
                                    .padding(
                                        start = 20.dp,
                                        end = 10.dp,
                                        bottom = 20.dp
                                    ) // Removed `bottom` padding
                                    .weight(1f),

                                keyboardOptions = KeyboardOptions.Default.copy(),
                                keyboardActions = KeyboardActions(onDone = {

                                    result.data += messageModel

                                    if (inputText.isNotBlank()) {
                                        datingViewModel.addMessage(messageModel)
                                        inputText = "" // Clear the text field after sending
                                        focusManager.clearFocus()

                                    }
                                })
                            )

                            Spacer(Modifier.width(10.dp))

                            Box(
                                modifier = Modifier
                                    .padding(bottom = 20.dp) // External padding
                                    .size(55.dp) // Matches the height of the TextField
                                    .offset(x = (-5).dp) // Move the button slightly to the left
                                    .clip(CircleShape)
                                    .background(OrangeSendButton) // Background color of the circle

                            ) {

                                Icon(
                                    imageVector = Icons.AutoMirrored.Sharp.Send,
                                    contentDescription = "Send",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.Center)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() },
                                            onClick = {
                                                if (inputText.isNotBlank()) {

                                                    result.data += messageModel

                                                    // Send message via ViewModel
                                                    datingViewModel.sendMessage(messageModel)

                                                    // Clear the input text
                                                    inputText = ""

                                                    // Optional: Log or handle timestamp
                                                    Log.d(
                                                        "currentTimeStamp",
                                                        messageModel.timestamp.toString()
                                                    )
                                                }
                                            }
                                        )
                                )

                            }

                        }


                    }

                    null -> {
                        Log.d("userChat", "Null: ${result}.")
                    }
                }


            }
        }

        // Loading Animation
        if (showLoadingAnim) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)) // Semi-transparent background for emphasis
                    .zIndex(1f), // Ensure it's on top of other UI elements
                contentAlignment = Alignment.Center // Center the animation
            ) {
                LoadingAnim(isVisible = true)
            }
        }

    }


}*/

/*                            // My Previous textfield 2024/12/31
                            OutlinedTextField(
                                value = messages.toString(),
                                onValueChange = { },
                                placeholder = {
                                    Text(text = "Your message")
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.White,
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = OrangeX, // Border color when focused
                                    unfocusedIndicatorColor = Color.Gray // Border color when unfocused
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .focusRequester(focusRequester)
                                    .onFocusChanged { focusState ->
                                        isCollapsed = focusState.isFocused
                                    }
                                    .padding(start = 20.dp, end = 10.dp) // Removed `bottom` padding
                                    .weight(1f) // Dynamically adjusts width
                                    .height(55.dp), // Set explicit height for alignment

                                keyboardOptions = KeyboardOptions.Default.copy(),
                                keyboardActions = KeyboardActions(onDone = {

                                    focusManager.clearFocus()

                                    if (messages.toString().isNotBlank()) {
                                        result.data += messageModel
                                        messages = listOf()
                                    }
                                })
                            )*/

/*                            Box(
                                modifier = Modifier
                                    .size(55.dp) // Matches the height of the TextField
                                    .offset(x = (-5).dp) // Move the button slightly to the left
                                    .clip(CircleShape)
                                    .background(OrangeSendButton) // Background color of the circle
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Sharp.Send,
                                    contentDescription = "Send",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.Center)

                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() },
                                            onClick = {

                                                if (messages
                                                        .toString()
                                                        .isNotBlank()
                                                ) {
                                                    result.data += messageModel
                                                    messages = listOf()
                                                    datingViewModel.sendMessage(messageModel)
                                                    Log.d(
                                                        "currentTimeStamp",
                                                        messageModel.timestamp.toString()
                                                    )
                                                }

                                                Log.d(
                                                    "currentTimeStamp",
                                                    messageModel.timestamp.toString()
                                                )
                                            }
                                        )
                                )
                            }*/

// Previous main chatLayout 2024/12/30
/*@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatLayout(datingViewModel: DatingViewModel, userDetailHiltViewModel: UserDetailHiltViewModel) {

    val context = LocalContext.current

    // State to track the expanded/collapsed state of the toolbar
    var isCollapsed by remember { mutableStateOf(false) }

    // Animate the toolbar height change
    val toolbarHeight by animateDpAsState(targetValue = if (isCollapsed) 100.dp else 150.dp)

    // Focus state for the OutlinedTextField
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

//    val sheetState = rememberModalBottomSheetState()
//    val scope = rememberCoroutineScope()

//    var message by remember { mutableStateOf("") }

    var messages = datingViewModel.messages.collectAsState().value

    val currentActiveUser = userDetailHiltViewModel.loggedInUser.value
//    var showBottomSheet by remember { mutableStateOf(false) }

    val selectedUser = selectedUserMatch.value
    var showLoadingAnim by remember { mutableStateOf(false) }

    val messageModel = MessageModel(
        receiver_email = currentActiveUser?.email ?: "",
        sender_email = selectedUser?.email ?: "",
        message = messages?.toString(),

        // TODO: I re-activated these commands
        sender_id = 1,
        receiver_id = 2
    )

    LaunchedEffect(Unit) {
        datingViewModel.  getChatsBetweenUsers(currentActiveUser?.email.toString(),selectedUser?.email.toString())
    }

    Scaffold(
        topBar = {

            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .height(toolbarHeight),
                actions = {

                    if (isCollapsed) {

                        // If it is collapsed


                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Profile picture
                                Box(
                                    modifier = Modifier
                                        .padding(15.dp)
                                        .size(50.dp) // Size of the outer circle
                                        .clip(CircleShape) // Clip to a circular shape
                                        .background(Brush.linearGradient( // Apply gradient background
                                            colors = listOf(Color(0xFFFFA500), Color.Red) // Orange to red gradient
                                        ))
                                        .padding(2.dp),
                                ) {
                                    // Inner Box for the profile picture
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape) // Clip the image into a circle
                                            .background(Color.White) // Optional: Placeholder color for the image
                                            .padding(3.dp)
                                    ) {
                                        Image(
                                            modifier = Modifier
                                                .fillMaxSize() // Make the image fill the inner circle
                                                .clip(CircleShape), // Ensure the image is clipped to a circle
                                            painter = painterResource(id = if (selectedUser?.gender == "Men") R.drawable.ic_men else R.drawable.ic_women),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop // Crop the image to fit the circle
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))

                                // Name and time + Icon
                                Column {

                                    Text(
                                        text = selectedUser?.name.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 25.sp,
                                        modifier = Modifier
                                            .padding(bottom = 1.dp)
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        OnlineDotIcon()

                                        Text(
                                            text = "Online",
                                            color = Color.Gray,
                                            fontSize = 14.sp,
                                            modifier = Modifier
                                                .padding(start = 5.dp)
                                        )
                                    }

                                }

                                // Push the Icon to the right
                                Spacer(modifier = Modifier.weight(1f))

                                // Button/Icon on the right
                                Image(
                                    painter = painterResource(R.drawable.ic_myownbuttondesign),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(55.dp)
                                        .background(Color.Transparent) // Optional styling
                                        .padding(end = 15.dp)
                                        .clickable{

                                            Toast.makeText(context, "Open Menu!", Toast.LENGTH_SHORT).show()

                                        }
                                )


                            }

                        }


                    } else {

                        // If it is not collapsed


                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Profile picture
                                Box(
                                    modifier = Modifier
                                        .padding(15.dp)
                                        .size(80.dp) // Size of the outer circle
                                        .clip(CircleShape) // Clip to a circular shape
                                        .background(Brush.linearGradient( // Apply gradient background
                                            colors = listOf(Color(0xFFFFA500), Color.Red) // Orange to red gradient
                                        ))
                                        .padding(2.dp),
                                ) {
                                    // Inner Box for the profile picture
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape) // Clip the image into a circle
                                            .background(Color.White) // Optional: Placeholder color for the image
                                            .padding(3.dp)
                                    ) {
                                        Image(
                                            modifier = Modifier
                                                .fillMaxSize() // Make the image fill the inner circle
                                                .clip(CircleShape), // Ensure the image is clipped to a circle
                                            painter = painterResource(id = if (selectedUser?.gender == "Men") R.drawable.ic_men else R.drawable.ic_women),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop // Crop the image to fit the circle
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))

                                // Name and time + Icon
                                Column {

                                    Text(
                                        text = selectedUser?.name.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 25.sp,
                                        modifier = Modifier
                                            .padding(bottom = 1.dp)
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        OnlineDotIcon()

                                        Text(
                                            text = "Online",
                                            color = Color.Gray,
                                            fontSize = 14.sp,
                                            modifier = Modifier
                                                .padding(start = 5.dp)
                                        )
                                    }

                                }

                                // Push the Icon to the right
                                Spacer(modifier = Modifier.weight(1f))

                                // Button/Icon on the right
                                Image(
                                    painter = painterResource(R.drawable.ic_myownbuttondesign),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(Color.Transparent) // Optional styling
                                        .padding(end = 15.dp)
                                        .clickable{

                                            Toast.makeText(context, "Open Menu!", Toast.LENGTH_SHORT).show()

                                        }
                                )


                            }

                        }
                    }

                }
            )




        }
    ) {

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(5.dp)
    ) {


        LoadingAnim(isVisible = showLoadingAnim)
        when (val result = datingViewModel.chatMessages.value) {
            is NetworkResult.Error -> {
                showLoadingAnim = false
                Log.d("userChat", "Error ${result.message.toString()}.")
            }

            NetworkResult.Loading -> {
                showLoadingAnim = true
                Log.d("userChat", "Loading.... ${result.toString()}")
            }

            is NetworkResult.Success -> {
                showLoadingAnim = false
                Log.d("userChat", "Success ${result.data.toString()}.")

                LaunchedEffect(Unit) {
                    selectedUser?.id?.let {
                        datingViewModel.receiveMessages(it) { messageModel ->
                            result.data += messageModel
                        }
                    }
                }

                LazyColumn(
                    state = rememberLazyListState(),
                    reverseLayout = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(result.data.reversed()) { msg ->
                        if (currentActiveUser?.email == msg.sender_email) {


                            // Sender's Message Layout


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Column(
                                    modifier = Modifier
                                        .background(Color(0xffF3F3F3), RoundedCornerShape(12.dp))
                                        .padding(12.dp)
                                        .fillMaxWidth(0.8f)
                                ) {

                                    Text(
                                        text = msg.message.toString(),
                                        color = Color.Black,
                                        fontSize = 16.sp
                                    )

                                    Spacer(modifier = Modifier.height(4.dp)) // Spacing between message and timestamp


                                    Text(
                                        text = formatTimestamp(msg.timestamp.toString()), // Call a function to format the timestamp
                                        color = Color.Black,
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .align(Alignment.End) // Align timestamp to the right
                                            .padding(end = 5.dp)

                                    )
                                }
                            }


                        } else {


                            // Receiver's Message Layout


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Column(
                                    modifier = Modifier
                                        .background(ChatBubbleColor2, RoundedCornerShape(12.dp))
                                        .padding(12.dp)
                                        .fillMaxWidth(0.8f)
                                ) {


                                    Text(
                                        text = msg.message.toString(),
                                        color = Color.Black,
                                        fontSize = 16.sp
                                    )

                                    Spacer(modifier = Modifier.height(4.dp)) // Spacing between message and timestamp


                                    Text(
                                        text = formatTimestamp(msg.timestamp.toString()), // Call a function to format the timestamp
                                        color = Color.Black,
                                        fontSize = 12.sp,
                                        modifier = Modifier.align(Alignment.End) // Align timestamp to the right
                                    )
                                }
                            }
                        }

                    }
                }


                // Bottom Row for Input
                Row(
                    verticalAlignment = Alignment.CenterVertically, // Align contents vertically centered
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .imePadding()
                ) {


                    OutlinedTextField(
                        value = messages.toString(),
                        onValueChange = { },
                        placeholder = {
                            Text(text = "Your message")
                        },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = OrangeX, // Border color when focused
                            unfocusedIndicatorColor = Color.Gray // Border color when unfocused
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                isCollapsed = focusState.isFocused
                            }
                            .padding(start = 20.dp, end = 10.dp) // Removed `bottom` padding
                            .weight(1f) // Dynamically adjusts width
                            .height(55.dp), // Set explicit height for alignment

                        keyboardOptions = KeyboardOptions.Default.copy(),
                        keyboardActions = KeyboardActions(onDone = {

                            focusManager.clearFocus()

                            if (messages.toString().isNotBlank()) {
                                result.data += messageModel
                                messages = listOf()
                            }
                        })
                    )

                    Spacer(Modifier.width(10.dp))

                    Box(
                        modifier = Modifier
                            .size(55.dp) // Matches the height of the TextField
                            .offset(x = (-5).dp) // Move the button slightly to the left
                            .clip(CircleShape)
                            .background(OrangeSendButton) // Background color of the circle
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Sharp.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)

                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {

                                        if (messages.toString().isNotBlank()) {
                                            result.data += messageModel
                                            messages = listOf()
                                            datingViewModel.sendMessage(messageModel)
                                            Log.d("currentTimeStamp", messageModel.timestamp.toString())
                                        }

                                        Log.d("currentTimeStamp", messageModel.timestamp.toString())
                                    }
                                )
                        )
                    }
                }
            }

            null -> {
                Log.d("userChat", "Null: ${result}.")
            }
        }


    }

}*/

// Original code created by Umar
/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatLayout(datingViewModel: DatingViewModel, userDetailHiltViewModel: UserDetailHiltViewModel) {
//    val sheetState = rememberModalBottomSheetState()
//    val scope = rememberCoroutineScope()
    var message by remember { mutableStateOf("") }
//    var messages = datingViewModel.messages.collectAsState().value
    val currentActiveUser = userDetailHiltViewModel.loggedInUser.value
//    var showBottomSheet by remember { mutableStateOf(false) }
    val selectedUser = selectedUserMatch.value
    var showLoadingAnim by remember { mutableStateOf(false) }
    val messageModel = MessageModel(
        receiver_email = selectedUser?.email ?: "",
        sender_email = currentActiveUser?.email ?: "",
        message = message,

        // TODO: I re-activated these commands
        sender_id = 2,
        receiver_id = 1
    )
    LaunchedEffect(Unit) {
        datingViewModel.  getChatsBetweenUsers(currentActiveUser?.email.toString(),selectedUser?.email.toString())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Start),
        ) {
            Image(
                painter = painterResource(id = if (selectedUser?.gender == "Men") R.drawable.ic_men else R.drawable.ic_women),
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(56.dp)
                    .clip(CircleShape)
            )
            Text(
                text = selectedUser?.name.toString(),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
        LoadingAnim(isVisible = showLoadingAnim)
        when (val result = datingViewModel.chatMessages.value) {
            is NetworkResult.Error -> {
                showLoadingAnim = false
                Log.d("userChat", "Error ${result.message.toString()}.")
            }

            NetworkResult.Loading -> {
                showLoadingAnim = true
                Log.d("userChat", "Loading.... ${result.toString()}")
            }

            is NetworkResult.Success -> {
                showLoadingAnim = false
                Log.d("userChat", "Success ${result.data.toString()}.")

                LaunchedEffect(Unit) {
                    selectedUser?.id?.let {
                        datingViewModel.receiveMessages(it) { messageModel ->
                            result.data += messageModel
                        }
                    }
                }

                LazyColumn(
                    state = rememberLazyListState(),
                    reverseLayout = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(result.data.reversed()) { msg ->
                        if (currentActiveUser?.email == msg.sender_email) {
                            // Sender's Message Layout
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Column(
                                    modifier = Modifier
                                        .background(Color(0xffF3F3F3), RoundedCornerShape(12.dp))
                                        .padding(12.dp)
                                        .fillMaxWidth(0.7f)
                                ) {
                                    Text(
                                        text = msg.message.toString(),
                                        color = Color.Black,
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp)) // Spacing between message and timestamp
                                    Text(
                                        text = formatTimestamp(msg.timestamp.toString()), // Call a function to format the timestamp
                                        color = Color.Gray,
                                        fontSize = 12.sp,
                                        modifier = Modifier.align(Alignment.End) // Align timestamp to the right

                                    )
                                }
                            }
                        } else {
                            // Receiver's Message Layout
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Column(
                                    modifier = Modifier
                                        .background(Color(0xffFF7A00), RoundedCornerShape(12.dp))
                                        .padding(12.dp)
                                        .fillMaxWidth(0.7f)
                                ) {
                                    Text(
                                        text = msg.message.toString(),
                                        color = Color.Black,
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp)) // Spacing between message and timestamp
                                    Text(
                                        text = formatTimestamp(msg.timestamp.toString()), // Call a function to format the timestamp
                                        color = Color.Gray,
                                        fontSize = 12.sp,
                                        modifier = Modifier.align(Alignment.End) // Align timestamp to the right
                                    )
                                }
                            }
                        }

                    }
                }


                // Bottom Row for Input
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                ) {
                    TextField(
                        value = message,
                        onValueChange = { message = it },
                        placeholder = {
                            Text(text = "Enter message")
                        },
                        modifier = Modifier
                            .width(272.dp)
                            .padding(16.dp, bottom = 40.dp)
                            .background(
                                MaterialTheme.colorScheme.background,
                                RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(),
                        keyboardActions = KeyboardActions(onDone = {
                            if (message.isNotBlank()) {
                                result.data += messageModel
                                message = ""
                            }
                        })
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_send),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(10.dp, bottom = 40.dp)
                            .background(Orange, CircleShape)
                            .padding(10.dp)
                            .size(24.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    if (message.isNotBlank()) {
                                        result.data += messageModel
                                        message = ""
                                        datingViewModel.sendMessage(messageModel)
                                        Log.d("currentTimeStamp", messageModel.timestamp.toString())
                                    }
                                }),
                        colorFilter = ColorFilter.tint(Color.White),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }

            null -> {
                Log.d("userChat", "Null: ${result}.")
            }
        }


    }

}


private fun formatTimestamp(timestamp: String): String {
    // Input format that matches the given timestamp: "Thu, 05 Sep 2024 16:50:55 GMT"
    val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.getDefault())

    // Output format (WhatsApp style format): "hh:mm a"
    val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val calendar = Calendar.getInstance()
    return try {
        // Use the current time if the provided timestamp is empty
        val date: Date = if (timestamp.isEmpty()) {
            calendar.time
        } else {
            // Parse the input timestamp to a Date object
            inputFormat.parse(timestamp) ?: Date() // Fallback to current date if parsing fails
        }

        // Return the formatted timestamp
        outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}*/

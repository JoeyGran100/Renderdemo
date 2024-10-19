package com.example.wingsdatingapp.ui_screens.dating.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wingsdatingapp.R
import com.example.wingsdatingapp.api.NetworkResult
import com.example.wingsdatingapp.model.MessageModel
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.DatingViewModel
import com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel.MainViewModel
import com.example.wingsdatingapp.ui_screens.onboarding.screens.viewmodel.UserDetailHiltViewModel
import com.example.wingsdatingapp.ui_screens.ui.theme.Orange
import com.example.wingsdatingapp.utils.LoadingAnim
import com.example.wingsdatingapp.utils.selectedUserMatch
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
        message = message
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
}

package com.example.wingsdatingapp.ui_screens.dating.screens.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private val _showBottomBar = MutableStateFlow(true)
    val showBottomBar: StateFlow<Boolean> = _showBottomBar

    fun showBottomBar() {
        _showBottomBar.value = true
    }

    fun hideBottomBar() {
        _showBottomBar.value = false
    }
}

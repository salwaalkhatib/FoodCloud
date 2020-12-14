package com.example.foodcloud.donor.navigation_bar.donorprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "  "
    }
    val text: LiveData<String> = _text
}
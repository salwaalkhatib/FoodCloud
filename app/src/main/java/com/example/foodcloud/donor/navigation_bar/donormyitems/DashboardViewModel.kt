package com.example.foodcloud.donor.navigation_bar.donormyitems

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "  "
    }
    val text: LiveData<String> = _text
}
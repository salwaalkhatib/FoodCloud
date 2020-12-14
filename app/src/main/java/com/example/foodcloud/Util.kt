package com.example.foodcloud

import android.content.Context
import android.widget.Toast
import com.example.foodcloud.Item.Item
import com.example.foodcloud.receiverNavigation.homedashrec.HomeFragment

object Util {

    var toast: Toast? = null

    fun show(context: Context, msg: String) {

        if (context != null) {
            if (toast != null) {
                toast!!.cancel()
            }
            toast = Toast.makeText(context, validateString(msg), toastTime)
            toast!!.show()
        }
    }
    private fun validateString(msg: String?): String {
        return msg ?: "null"
    }
    private val toastTime: Int
        get() = Toast.LENGTH_LONG
}
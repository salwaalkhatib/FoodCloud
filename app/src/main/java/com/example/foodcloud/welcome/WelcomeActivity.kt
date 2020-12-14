package com.example.foodcloud.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.foodcloud.donorside.MainActivity
import com.example.foodcloud.R
import com.example.foodcloud.receiver.SignUpReceiver

class welcomeActivity : AppCompatActivity() {
    lateinit var btn_donor: Button
    lateinit var btn_receiver: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        btn_donor = findViewById(R.id.donor)
        btn_receiver=findViewById(R.id.receiver)

        btn_donor.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))        }
        btn_receiver.setOnClickListener {
            startActivity(Intent(this, SignUpReceiver::class.java))
        }


    }
}
package com.example.foodcloud.receiver

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.foodcloud.R
import com.example.foodcloud.donorside.LoginActivity
import com.example.foodcloud.donorside.MainActivity


class SignUpReceiver : AppCompatActivity() {
    lateinit var btnSwitch: ImageButton
    lateinit var nbr: EditText
    lateinit var cntn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_receiver)
        btnSwitch = findViewById(R.id.SwitchTo)
        nbr = findViewById(R.id.phoneNumber)
        cntn = findViewById(R.id.continueNbr)

        cntn.setOnClickListener {
            val mobile: String = nbr.text.toString().trim()
             if(mobile.length != 8){
                nbr.setError(this.getResources().getString(R.string.valid_nbr))
                nbr.requestFocus()
                 return@setOnClickListener
            }
            val intent = Intent(this@SignUpReceiver, VerifyMobile::class.java)
            intent.putExtra("mobile", mobile)
            startActivity(intent)
        }

        btnSwitch.setOnClickListener{
            val intent = Intent(this@SignUpReceiver, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
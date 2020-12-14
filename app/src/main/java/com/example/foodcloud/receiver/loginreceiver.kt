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

class loginreceiver : AppCompatActivity() {
    internal lateinit var btnSwitch: ImageButton
    internal lateinit var SignInbtn: Button
    internal lateinit var phoneNb: EditText
    internal lateinit var password: EditText
    internal lateinit var SignUpbtn: TextView
    internal lateinit var forgotpass: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginreceiver)
        btnSwitch=findViewById(R.id.switchbtn2)
        SignInbtn=findViewById(R.id.buttonsign)
        phoneNb=findViewById(R.id.phonereceiver)
        password=findViewById(R.id.password2)
        SignUpbtn=findViewById(R.id.noaccount)
        forgotpass=findViewById(R.id.forgotpass)
        btnSwitch.setOnClickListener{
            startActivity(Intent(this@loginreceiver, LoginActivity::class.java))
        }
        SignInbtn.setOnClickListener{
            startActivity(Intent(this@loginreceiver,receiver_bottom::class.java))
        }
        SignUpbtn.setOnClickListener{
            startActivity(Intent(this@loginreceiver, SignUpReceiver::class.java))
        }

}}
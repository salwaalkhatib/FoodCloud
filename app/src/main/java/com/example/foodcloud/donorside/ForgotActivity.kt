package com.example.foodcloud.donorside

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.foodcloud.R
import com.example.foodcloud.Util.show
import com.google.firebase.auth.FirebaseAuth

class ForgotActivity : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var btnReset: Button
    lateinit var mFirebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        email = findViewById(R.id.emailToReset)
        btnReset = findViewById(R.id.reset)
        mFirebaseAuth = FirebaseAuth.getInstance()
        btnReset.setOnClickListener {
            if(email.text.toString().isEmpty()){
                email.error = this.resources.getString(R.string.enter_email)
                email.requestFocus()
            } else{
                mFirebaseAuth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener(this) { task ->
                    if(task.isSuccessful){
                        show(this, this.resources.getString(R.string.reset_email_sent))
                        startActivity(Intent(this, LoginActivity::class.java))
                    } else{
                        show(this, this.resources.getString(R.string.error))
                    }
                }

            }
        }
    }
}
package com.example.foodcloud.receiver

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodcloud.R
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import com.example.foodcloud.Util.show

class VerifyMobile : AppCompatActivity() {

    lateinit var login: Button
    private lateinit var otp: EditText
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var mAuth: FirebaseAuth
    var verificationId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_mobile)
        mAuth = FirebaseAuth.getInstance()
        verify ()
        login = findViewById(R.id.login)
        otp = findViewById(R.id.otp)
        login.setOnClickListener {
            authenticate()
        }
    }
    private fun verificationCallbacks () {
        mCallbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signIn(credential)
            }
            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(this@VerifyMobile,p0.toString(), Toast.LENGTH_SHORT).show()

            }
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                verificationId = p0.toString()
            }
        }
    }
    private fun verify () {
        verificationCallbacks()
        var number = intent.getStringExtra("mobile")
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            this.getResources().getString(R.string.phone_code)+number,
            60,
            TimeUnit.SECONDS,
            this,
            mCallbacks
        )
    }

    private fun signIn (credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                    task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    show(this,this.resources.getString(R.string.success_login))
                    startActivity(Intent(this, receiver_bottom::class.java))
                }
            }
    }
    private fun authenticate () {

        val verifiNo = otp.text.toString()

        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, verifiNo)

        signIn(credential)

    }

}
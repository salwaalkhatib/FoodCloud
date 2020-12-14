package com.example.foodcloud.donorside

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.foodcloud.R
import com.example.foodcloud.Util.show
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class passDonor : AppCompatActivity() {
    lateinit var btn_change: Button
    lateinit var current: EditText
    lateinit var newPass: EditText
    lateinit var newPassCon: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass_donor)

        btn_change = findViewById(R.id.button2)
        current = findViewById(R.id.oldPassword)
        newPass = findViewById(R.id.newpass)
        newPassCon = findViewById(R.id.newpass2)
        auth = FirebaseAuth.getInstance()

        btn_change.setOnClickListener {
            changePassword()
        }
    }
    private fun changePassword(){
        if(current.text.isNotEmpty() && newPass.text.isNotEmpty() && newPassCon.text.isNotEmpty()){
            if(newPass.text.toString().equals(newPassCon.text.toString())){
                val user: FirebaseUser? = auth.currentUser
                if(user != null && user.email != null){
                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, current.text.toString())

// Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                show(this, R.string.reauth_success.toString())
                                user.updatePassword(newPass.text.toString())
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            show(this, R.string.pass_change.toString())
                                            auth.signOut()
                                            startActivity(Intent(this, LoginActivity::class.java))
                                            finish()
                                        }
                                    }
                            } else {
                                show(this, R.string.reauth_fail.toString())
                            }
                        }

                } else{
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }

            }else{
                newPass.setError(R.string.pass_nomatch.toString())
                newPassCon.setError(R.string.pass_nomatch.toString())
                newPass.requestFocus()
                newPassCon.requestFocus()
            }

        } else{
            show(this, R.string.please_all_fields.toString())
        }

    }
}
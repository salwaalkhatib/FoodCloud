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
                                show(this, this.resources.getString(R.string.reauth_success))
                                user.updatePassword(newPass.text.toString())
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            show(this,this.resources.getString(R.string.pass_change))
                                            startActivity(Intent(this, Donormain::class.java))
                                        }
                                    }
                            } else {
                                show(this, this.resources.getString(R.string.reauth_fail))
                            }
                        }

                } else{
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }

            }else{
                newPass.error = this.resources.getString(R.string.pass_nomatch)
                newPassCon.error = this.resources.getString(R.string.pass_nomatch)
                newPass.requestFocus()
                newPassCon.requestFocus()
            }

        } else{
            show(this, this.resources.getString(R.string.please_all_fields))
        }

    }
}
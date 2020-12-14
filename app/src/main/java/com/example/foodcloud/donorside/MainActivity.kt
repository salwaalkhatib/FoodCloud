package com.example.foodcloud.donorside

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.content.Intent
import com.example.foodcloud.R
import com.example.foodcloud.Util.show
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var emailId: EditText
    lateinit var password: EditText
    lateinit var tvSignIn: TextView
    lateinit var btnSignUp: Button
    lateinit var confirm: EditText
    lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var tvSwitch: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseAuth = FirebaseAuth.getInstance()
        emailId = findViewById(R.id.email)
        password = findViewById(R.id.password)
        confirm = findViewById(R.id.confirmpwd)
        btnSignUp = findViewById(R.id.button)
        tvSignIn = findViewById(R.id.noaccount)


        btnSignUp.setOnClickListener {
            var email: String = emailId.text.toString()
            var pwd: String = password.text.toString()
            var pwdconfirm: String = confirm.text.toString()
            if(email.isEmpty()){
              emailId.setError(this.getResources().getString(R.string.please_email))
              emailId.requestFocus()
            }
            else if(pwd.isEmpty()){
                password.setError(this.getResources().getString(R.string.please_pass))
                password.requestFocus()
            }
            else if(pwdconfirm.isEmpty()){
                confirm.setError(this.getResources().getString(R.string.please_pass_again))
                confirm.requestFocus()
            }
            else if(email.isEmpty() && pwd.isEmpty()&& pwdconfirm.isEmpty()){
                show(this, this.getResources().getString(R.string.please_all_fields))
            }
            else if(pwd != pwdconfirm){
                confirm.setError(this.getResources().getString(R.string.pass_nomatch))
                confirm.requestFocus()
            }
            else if(!(email.isEmpty() && pwd.isEmpty()&& pwdconfirm.isEmpty())){
                mFirebaseAuth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = mFirebaseAuth.currentUser
                            startActivity(Intent(this@MainActivity, Donormain::class.java))
                            show(this,this.getResources().getString(R.string.success_signup))
                        } else {
                            // If sign in fails, display a message to the user.
                            show(baseContext, this.getResources().getString(R.string.auth_fail)+ task.exception)
                        }

                        // ...
                    }
            }
            else{
                show(this,this.getResources().getString(R.string.error))
            }
        }
        tvSignIn.setOnClickListener{
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}
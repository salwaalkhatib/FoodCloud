package com.example.foodcloud.donorside

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.foodcloud.Item.Item
import com.example.foodcloud.R
import com.example.foodcloud.Util.show
import com.example.foodcloud.receiver.SignUpReceiver
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {
    internal lateinit var emailId: EditText
    internal lateinit var password: EditText
    internal lateinit var tvSignUp: TextView
    internal lateinit var forgot: TextView
    internal lateinit var switching:ImageButton
    internal lateinit var btnSignIn: Button
    internal lateinit var mFirebaseAuth: FirebaseAuth
    lateinit private var mAuthStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mFirebaseAuth = FirebaseAuth.getInstance()
        emailId = findViewById(R.id.email)
        password = findViewById(R.id.password)
        btnSignIn = findViewById(R.id.button)
        tvSignUp = findViewById(R.id.noaccount)
        forgot = findViewById(R.id.forgot)
        switching=findViewById(R.id.switchbtn)
        mAuthStateListener = FirebaseAuth.AuthStateListener {
            val mFirebaseUser = mFirebaseAuth.currentUser
            if(mFirebaseUser != null){
                show(this, this.resources.getString(R.string.success_login))
                startActivity(Intent(this@LoginActivity, Donormain::class.java))
            }
        }
        btnSignIn.setOnClickListener{
            var email: String = emailId.text.toString()
            var pwd: String = password.text.toString()
            if(email.isEmpty()){
                emailId.setError(this.resources.getString(R.string.please_email))
                emailId.requestFocus()
            }
            else if(pwd.isEmpty()){
                password.setError(this.resources.getString(R.string.please_pass))
                password.requestFocus()
            }
            else if(email.isEmpty() && pwd.isEmpty()){
                show(this, this.resources.getString(R.string.please_all_fields))
            }
            else if(!(email.isEmpty() && pwd.isEmpty())){
                mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener ( this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, Donormain::class.java))
                        show(this, this.resources.getString(R.string.success_login))
                    } else {
                        show(this, this.resources.getString(R.string.error_login))
                    }
                })
            }
            else{
                show(this, this.resources.getString(R.string.error))
            }
        }
        tvSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        forgot.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotActivity::class.java))
        }
          switching.setOnClickListener{
                startActivity(Intent(this@LoginActivity, SignUpReceiver::class.java))
                  }
    }
    override fun onStart() {
        super.onStart()
        mFirebaseAuth.addAuthStateListener(mAuthStateListener)
        modifyDatabase()
    }

    private fun modifyDatabase(){
        val nbOrder: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Item")
        val postListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val item = postSnapshot.getValue(Item::class.java)
                    if(item!=null){
                        val formatter = SimpleDateFormat("dd/MM/yyyy")
                        val datestring = formatter.format(Date(item.exdate))

                        val strDate: Date = formatter.parse(datestring)
                        if(Date().after(strDate)){
                            postSnapshot.ref.child("expired").setValue(true)
                        }else{
                            postSnapshot.ref.child("expired").setValue(false)

                        }

                    }


                }


            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        nbOrder.addValueEventListener(postListener)
    }
}
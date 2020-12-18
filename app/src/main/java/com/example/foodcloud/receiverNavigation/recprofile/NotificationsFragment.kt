package com.example.foodcloud.receiverNavigation.recprofile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.foodcloud.R
import com.example.foodcloud.Util.show
import com.example.foodcloud.receiver.SignUpReceiver
import com.google.firebase.auth.FirebaseAuth

class NotificationsFragment : Fragment() {

  private lateinit var notificationsViewModel: NotificationsViewModel
  lateinit var logoutbtn : Button
  lateinit var phonenbr: TextView
  internal lateinit var mFirebaseAuth: FirebaseAuth

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    notificationsViewModel =
    ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_recprofile, container, false)
    val textView: TextView = root.findViewById(R.id.text_notifications)
    notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
      textView.text = it
    })

    logoutbtn = root.findViewById(R.id.button4)
    phonenbr = root.findViewById(R.id.TextPhone)
    mFirebaseAuth = FirebaseAuth.getInstance()
    val userphonenbr = mFirebaseAuth.currentUser?.phoneNumber.toString()
    phonenbr.text = userphonenbr
    logoutbtn.setOnClickListener {
      FirebaseAuth.getInstance().signOut()
      startActivity(Intent(activity, SignUpReceiver::class.java))
      show(root.context, this.resources.getString(R.string.logout))
    }
    return root
  }
}
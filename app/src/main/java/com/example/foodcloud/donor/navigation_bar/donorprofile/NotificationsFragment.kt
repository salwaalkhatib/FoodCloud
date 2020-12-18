package com.example.foodcloud.donor.navigation_bar.donorprofile

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.foodcloud.Item.Item
import com.example.foodcloud.R
import com.example.foodcloud.Util
import com.example.foodcloud.donorside.LoginActivity
import com.example.foodcloud.donorside.passDonor
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import de.hdodenhof.circleimageview.CircleImageView
import java.net.URL


class NotificationsFragment : Fragment() {
    internal lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var notificationsViewModel: NotificationsViewModel
    lateinit var editbtn: Button
    lateinit var logbtn : Button
    lateinit  var pp:CircleImageView
    lateinit var ppchange:Button
    lateinit var imageUri:Uri
    lateinit var downloadURL:String
    lateinit var amount_donated: TextView
    lateinit var storageprofilepictureRef:StorageReference

    lateinit var uploadTask: UploadTask
    lateinit var storage:FirebaseStorage
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile_donor, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
            storageprofilepictureRef=storage.reference
                .child("Profile pictures")
               .child(mFirebaseAuth.currentUser?.email.toString()+".jpg")
                .child(mFirebaseAuth.currentUser?.email.toString()+".jpg")

            storageprofilepictureRef.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.d(TAG, "Download url is:  $uri")
                    Picasso.get().load(uri.toString()).into(pp);
                }

        })
        mFirebaseAuth = FirebaseAuth.getInstance()
        pp=root.findViewById(R.id.profile_image)
        ppchange=root.findViewById(R.id.changepp)
        editbtn=root.findViewById(R.id.edit)
        logbtn=root.findViewById(R.id.logout2)
        amount_donated = root.findViewById(R.id.amount_donated)
        storage= FirebaseStorage.getInstance()

        logbtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
            Util.show(root.context, this.getResources().getString(R.string.logout))
        }
        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Item")
        var amountDB: Int = 0
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (postSnapshot in dataSnapshot.children) {
                        var item = postSnapshot.getValue(Item::class.java)
                        if(item != null && item.userEmail == mFirebaseAuth.currentUser?.email.toString()){
                            amountDB += item.initialQuantity
                        }

                    }
                    amount_donated.text = "Amount Donated: $amountDB"

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                context?.let { Util.show(it, databaseError.message) }
            }
        }
        ref.addListenerForSingleValueEvent(postListener)


        var user = mFirebaseAuth.currentUser;
        var email=""

        if (user != null) {
            email = user.email.toString();
        }
        lateinit var emailtext: TextView
        emailtext=root.findViewById(R.id.textView5)
        emailtext.setText(email)

        editbtn=root.findViewById(R.id.edit)
        editbtn.setOnClickListener {
            startActivity(Intent(activity, passDonor::class.java))
        }
       ppchange.setOnClickListener{
           CropImage.activity()
               .setAspectRatio(1,1)
               .start(this!!.context!!, this);
       }

        return root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE   && resultCode==RESULT_OK && data!=null)
        {
            var result=CropImage.getActivityResult(data)
            imageUri=result.uri
            pp.setImageURI(imageUri)

            val progressDialog = ProgressDialog(activity)
            progressDialog.setTitle(this.resources.getString(R.string.update_pic))
            progressDialog.setMessage(this.resources.getString(R.string.update_pic_text))
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()


            if(imageUri !=null){
                var fileRef:StorageReference=storage.reference
                    .child("Profile pictures")
                    .child(mFirebaseAuth.currentUser?.email.toString()+".jpg")
                   .child(mFirebaseAuth.currentUser?.email.toString()+".jpg")

                uploadTask=fileRef.putFile(imageUri)
                uploadTask.addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot -> // Get a URL to the uploaded content
                     downloadURL = taskSnapshot.storage.downloadUrl.toString()

                    progressDialog.dismiss()
                    })
                    .addOnFailureListener(OnFailureListener {
                        context?.let {
                            Util.show(
                                it,
                                this.resources.getString(R.string.error)
                            )
                        }
                        progressDialog.dismiss()})

            }
        }
        else {
            context?.let {
                Util.show(
                    it,
                    this.resources.getString(R.string.error)
                )
            }

        }


    }
    
}





package com.example.foodcloud.Item

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.storage.StorageManager
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.foodcloud.R
import com.example.foodcloud.Util
import com.example.foodcloud.Util.show
import com.example.foodcloud.receiver.receiver_bottom
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StreamDownloadTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_additem.*
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class ItemDetailsActivity : AppCompatActivity() {
     lateinit var addToCart: FloatingActionButton
     lateinit var itemImage: ImageView
     lateinit var numberButton: ElegantNumberButton
     lateinit var itemName: TextView
    lateinit var itemCategory: TextView
     lateinit var itemQuantity: TextView
    lateinit var itemExdate: TextView
     lateinit var itemID: String

    internal lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var storage: FirebaseStorage
    lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)
        mFirebaseAuth = FirebaseAuth.getInstance()
        addToCart = findViewById(R.id.add_item_cart)
        numberButton = findViewById(R.id.elegant)
        itemImage = findViewById(R.id.item_picture)
        itemName = findViewById(R.id.item_name1)
        itemCategory = findViewById(R.id.item_category1)
        itemQuantity = findViewById(R.id.item_quantity1)
        itemExdate = findViewById(R.id.item_exdate1)
        storage= FirebaseStorage.getInstance()
        storageRef = FirebaseStorage.getInstance().reference
        itemID = intent.getStringExtra("id").toString()!!
        addToCart.setOnClickListener {
            addingToCartList(itemID)
        }

    }

    private fun addingToCartList(itemid: String){
        lateinit var saveCurrentTime: String
        lateinit var saveCurrentDate: String
        val calForDate: Calendar = Calendar.getInstance()
        val currentDate: SimpleDateFormat = SimpleDateFormat("MMM dd, yyyy")
        saveCurrentDate = currentDate.format(calForDate.time)

        val currentTime: SimpleDateFormat = SimpleDateFormat("HH:mm:ss a")
        saveCurrentTime = currentTime.format(calForDate.time)

        val cartListRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Cart List")
        val cartMap: MutableMap<String, String> = mutableMapOf<String, String>()
        cartMap["iid"] = itemID
        cartMap["itemCategory"] = itemCategory.text.toString()
        cartMap["itemName"] = itemName.text.toString()
        cartMap["time"] = saveCurrentTime
        cartMap["date"] = saveCurrentDate
        cartMap["quantity"] = numberButton.number

        cartListRef.child("User View").child(mFirebaseAuth.currentUser?.phoneNumber.toString())
            .child("Item").child(itemID).updateChildren(cartMap as Map<String, Any>).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    cartListRef.child("Admin View").child(mFirebaseAuth.currentUser?.phoneNumber.toString())
                        .child("Item").child(itemID).updateChildren(cartMap as Map<String, Any>)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                show(this, "Added to Cart")
                                startActivity(Intent(this, receiver_bottom::class.java))
                            }
                        }
                } else {
                    show(this, "Error")
                }

            }
    }

    private fun getItemDetails(itemID: String) {
        var initial: Int = 1
        val cartListRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Cart List").child("User View").child(mFirebaseAuth.currentUser?.phoneNumber.toString())
            .child("Item")
        val postListener1 = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    for(postSnapshot in dataSnapshot.children) {
                        var cart = postSnapshot.getValue(Cart::class.java)
                        if(cart!=null && cart.iid==itemID){
                            initial = cart.quantity.toInt()
                        }
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        cartListRef.addValueEventListener(postListener1)
        val itemRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Item")
        val postListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    for(postSnapshot in dataSnapshot.children) {
                        var item = postSnapshot.getValue(Item::class.java)
                        if(item!=null && item.itemId == itemID){

                             val formatter = SimpleDateFormat("dd-MM-yyyy")
                             val datestring = formatter.format(Date(item.exdate))

                            itemName.text = item.name
                            itemCategory.text = item.category
                            itemQuantity.text = "Quantity: ".plus(item.quantity)
                            itemExdate.text = "Expiry Date: ".plus(datestring)
                            numberButton.setRange(1, item.quantity)
                            numberButton.number = initial.toString()
                            val actionBar = supportActionBar
                            actionBar!!.title = item.name
                        }
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        itemRef.addValueEventListener(postListener)
        val imageRef: StorageReference = storageRef.child("item pictures").child("$itemID.jpg")
        val localfile: File = File.createTempFile(itemID, "jpg")
        imageRef.getFile(localfile).addOnSuccessListener {
            val bitmap: Bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            itemImage.setImageBitmap(bitmap)
        }




    }

    override fun onStart() {
        super.onStart()
        getItemDetails(itemID)
    }


}
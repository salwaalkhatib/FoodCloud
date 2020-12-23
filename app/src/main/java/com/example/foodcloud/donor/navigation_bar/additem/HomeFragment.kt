package com.example.foodcloud.donor.navigation_bar.additem

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.icu.text.MessageFormat.format
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat.format
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.foodcloud.Item.Item
import com.example.foodcloud.R
import com.example.foodcloud.Util
import com.example.foodcloud.Util.show
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import okhttp3.internal.Util.format
import okhttp3.internal.http.HttpDate.format
import java.lang.String.format
import java.text.DateFormat
import java.text.MessageFormat.format
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    internal lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var submitbtn: Button
    lateinit var name: EditText
    lateinit var phone: EditText
    lateinit var exdate: DatePicker
    lateinit var quantity: EditText
    lateinit var downloadURL:String
    lateinit var imageUri:Uri
    lateinit var storage:FirebaseStorage
    lateinit var uploadTask: UploadTask
    lateinit var addphoto:Button
    lateinit var image:ImageView
    // lateinit var testt:TextView
    val item: Item = Item()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_additem, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
            (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        })

        var database = FirebaseDatabase.getInstance().getReference("Item")
        imageUri=Uri.parse("http://www.google.com")
        name = root.findViewById(R.id.foodname)
        phone = root.findViewById(R.id.phonenb)
        quantity = root.findViewById(R.id.quantity)
        exdate = root.findViewById(R.id.expiry)
        submitbtn = root.findViewById(R.id.submit)
        addphoto = root.findViewById(R.id.addimage)
        image = root.findViewById(R.id.image)
        storage = FirebaseStorage.getInstance()
        addphoto.setOnClickListener {
            CropImage.activity()
                .setAspectRatio(1, 1)
                .start(this!!.requireContext(), this);
        }


        submitbtn.setOnClickListener {
            mFirebaseAuth = FirebaseAuth.getInstance()
            val Name = name.text.toString()

            val mySpinner = root.findViewById(R.id.category) as Spinner
            val Category = mySpinner.selectedItem.toString()

            val Phone = phone.text.toString()
            val Quantito = quantity.text.toString()
            val Quantity = Quantito.toInt()


            val day: Int = exdate.getDayOfMonth()
            val month: Int = exdate.getMonth()
            val year: Int = exdate.getYear()


            val calendar = Calendar.getInstance()
            calendar[year, month] = day
            val Expirydate = calendar.timeInMillis

            item.itemId = UUID.randomUUID().toString()
            item.userEmail = mFirebaseAuth.currentUser?.email.toString()
            item.name = Name.capitalize()
            item.category = Category
            item.phone = Phone
            item.quantity = Quantity
            item.exdate = Expirydate
            item.initialQuantity = Quantity

            if (imageUri == Uri.parse("http://www.google.com")) {
                context?.let {
                    Util.show(
                        it,
                        "Please add an image. "
                    )
                }

            }
            else {
                var fileRef: StorageReference = storage.reference
                    .child("item pictures")
                    .child(item.itemId + ".jpg")
                uploadTask = fileRef.putFile(imageUri)
                uploadTask.addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot -> // Get a URL to the uploaded content
                    downloadURL = taskSnapshot.storage.downloadUrl.toString()


                })
                    .addOnFailureListener(OnFailureListener {
                        context?.let {
                            Util.show(
                                it,
                                this.resources.getString(R.string.error)
                            )
                        }
                    })



                if (Name.isEmpty() || Category.isEmpty() || Phone.isEmpty() || Quantito.isEmpty()) {
                    context?.let { show(it, this.resources.getString(R.string.please_all_fields)) }

                } else if (Name.isEmpty() && Category.isEmpty() && Phone.isEmpty() && Quantito.isEmpty()) {
                    context?.let { show(it, "Form is empty") }
                } else if (Expirydate < System.currentTimeMillis()) {

                    context?.let { show(it, this.resources.getString(R.string.expired)) }
                } else {
                    database.push().setValue(item)
                    this.resources.getString(R.string.expired)
                    context?.let { show(it, this.resources.getString(R.string.item_added)) }
                    name.getText().clear()
                    phone.getText().clear()
                    quantity.getText().clear()
                    image.setImageDrawable(null)

                    val cal = Calendar.getInstance()
                    exdate.updateDate(
                        cal[Calendar.YEAR],
                        cal[Calendar.MONTH],
                        cal[Calendar.DAY_OF_MONTH]
                    )
                }

            }
        }

        return root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            var result = CropImage.getActivityResult(data)
            imageUri = result.uri
            image.setImageResource(R.drawable.tick)

        }

    }}
package com.example.foodcloud.receiverNavigation.myitemsrec

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcloud.Item.*
import com.example.foodcloud.R
import com.example.foodcloud.Util
import com.example.foodcloud.Util.show
import com.example.foodcloud.receiver.receiver_bottom
import com.firebase.ui.database.FirebaseRecyclerAdapter

import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.Instant.now
import java.util.*


class DashboardFragment() : Fragment() {

  private lateinit var dashboardViewModel: DashboardViewModel
  private lateinit var recyclerView: RecyclerView
  private lateinit var orderBtn: Button
  private lateinit var layoutManager: RecyclerView.LayoutManager
  internal lateinit var mFirebaseAuth: FirebaseAuth
  private lateinit var totalAmount: TextView
  private lateinit var arrayIDs: ArrayList<String>
  private lateinit var arrayQuantity: ArrayList<Int>
  private var amount: Int = 0
  lateinit var noOrder:TextView
  private val MAX: Int = 10
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    dashboardViewModel =
    ViewModelProviders.of(this).get(DashboardViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_dashboard_rec, container, false)
    val textView: TextView = root.findViewById(R.id.text_dashboard)
    dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
      textView.text = it
      (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    })
    noOrder=root.findViewById(R.id.no_order)
    recyclerView = root.findViewById(R.id.cart_list)
    recyclerView.setHasFixedSize(true)
    layoutManager = LinearLayoutManager(root.context)
    recyclerView.layoutManager = layoutManager
    orderBtn = root.findViewById(R.id.order_btn)
    totalAmount = root.findViewById(R.id.total_amount)
    mFirebaseAuth = FirebaseAuth.getInstance()
    arrayIDs = arrayListOf()
    arrayQuantity = arrayListOf()



    orderBtn.setOnClickListener {
      val alertDialog: AlertDialog.Builder = AlertDialog.Builder(root.context)
      alertDialog.setTitle(root.context.resources.getString(R.string.confirm_order))
      alertDialog.setMessage(root.context.resources.getString(R.string.confirm_order_msg))
      alertDialog.setPositiveButton(
        "Place Order"
      ) { _, _ ->
          confirmOrder()
      }
      alertDialog.setNegativeButton(
        "Cancel"
      ) { _, _ ->

      }
      val alert: AlertDialog = alertDialog.create()
      alert.setCanceledOnTouchOutside(false)
      alert.show()
    }
    return root
  }

  override fun onStart() {
    super.onStart()
    amount_in_db()
    val cartListRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Cart List")
    val options = FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("User View")
      .child(mFirebaseAuth.currentUser?.phoneNumber.toString()).child("Item"), Cart::class.java)
      .setLifecycleOwner(this).build()


      val adapter1 = object : FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
          return CartViewHolder(
            LayoutInflater.from(parent.context)
              .inflate(R.layout.cart_item_holder, parent, false)
          )
        }

        override fun onBindViewHolder(holder: CartViewHolder, position: Int, model: Cart) {
          arrayIDs.add(model.iid)
          arrayQuantity.add(model.quantity.toInt())
          holder.txtItemName.text = model.itemName
          holder.txtItemQuantity.text = "Quantity: " + model.quantity
          holder.txtItemCategory.text = model.itemCategory
          var oneItemAmount: Int = ((model.quantity.toInt()))
          amount += oneItemAmount
          totalAmount.text = "Total Amount: $amount"

          holder.itemView.setOnClickListener {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(activity!!)
            alertDialog.setTitle(activity!!.getResources().getString(R.string.dialog_delete))
            alertDialog.setMessage(activity!!.getResources().getString(R.string.dialog_delete_msg))
            alertDialog.setPositiveButton(
              "Remove"
            ) { _, _ ->
              cartListRef.child("User View")
                .child(mFirebaseAuth.currentUser?.phoneNumber.toString()).child("Item")
                .child(model.iid).removeValue().addOnCompleteListener() { task ->
                  if (task.isSuccessful) {
                    context?.let {
                      show(it, activity!!.getResources().getString(R.string.item_removed))
                    }
                  } else {

                  }

                }

            }
            alertDialog.setNegativeButton(
              "Edit"
            ) { _, _ ->
              val intent = Intent(context, ItemDetailsActivity::class.java)
              intent.putExtra("id", model.iid)
              activity!!.startActivity(intent)
            }
            val alert: AlertDialog = alertDialog.create()
            alert.setCanceledOnTouchOutside(false)
            alert.show()
          }
        }

        override fun onDataChanged() {

        }
      }
      recyclerView.adapter=adapter1
      adapter1.startListening()
    }

  private fun confirmOrder() {
    val nbOrder: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Orders")
      .child(mFirebaseAuth.currentUser?.phoneNumber.toString())
    val calForDate1: Calendar = Calendar.getInstance()
    val currentDate1 = SimpleDateFormat("MMM dd, yyyy")
    val saveDate = currentDate1.format(calForDate1.time)
    var amount_db: Int = 0
    var x: Int = 0
    val postListener1 = object: ValueEventListener{
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        for (postSnapshot in dataSnapshot.children) {
          val order= postSnapshot.getValue(Order::class.java)!!
          if (order != null && order?.date.toString() == saveDate) {
            amount_db += order?.totalAmount?.toInt()
          }
          if(amount_db+ amount>MAX) {
            x = MAX - amount_db
            noOrder?.text = "You can only order "+ x.toString()+ " item(s)."
          }else{
            order()

          }

        }


      }
      override fun onCancelled(error: DatabaseError) {

      }
    }
    nbOrder.addValueEventListener(postListener1)

    }

  private fun amount_in_db(){
    val nbOrder: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Orders")
      .child(mFirebaseAuth.currentUser?.phoneNumber.toString())
    val calForDate: Calendar = Calendar.getInstance()
    val currentDate = SimpleDateFormat("MMM dd, yyyy")
    val saveDate = currentDate.format(calForDate.time)
    var amount_db: Int = 0
    val postListener1 = object: ValueEventListener{
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        for (postSnapshot in dataSnapshot.children) {
          val order= postSnapshot.getValue(Order::class.java)!!
          if (order != null && order?.date.toString() == saveDate) {
            amount_db += order?.totalAmount?.toInt()
          }
          if(amount_db>=MAX){
            orderBtn.isEnabled = false
            noOrder.text = "You can't make any more orders today. Kindly visit us tomorrow!"}

        }


      }
      override fun onCancelled(error: DatabaseError) {

      }
    }
    nbOrder.addValueEventListener(postListener1)
  }

  private fun order(){
    val saveCurrentTime: String
    val saveCurrentDate: String
    val calForDate: Calendar = Calendar.getInstance()
    val currentDate: SimpleDateFormat = SimpleDateFormat("MMM dd, yyyy")
    saveCurrentDate = currentDate.format(calForDate.time)
    val currentTime: SimpleDateFormat = SimpleDateFormat("HH:mm:ss a")
    saveCurrentTime = currentTime.format(calForDate.time)
    val key = UUID.randomUUID().toString()
    val orderRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Orders")
      .child(mFirebaseAuth.currentUser?.phoneNumber.toString()).child(key)
    val orderMap: MutableMap<String, String> = mutableMapOf<String, String>()
    orderMap["totalAmount"] = amount.toString()
    orderMap["time"] = saveCurrentTime
    orderMap["date"] = saveCurrentDate
    orderMap["key"] = key
    orderRef.updateChildren(orderMap as Map<String, Any>).addOnCompleteListener() { task ->
      if (task.isSuccessful) {
        FirebaseDatabase.getInstance().reference.child("Cart List")
          .child("User View").child(mFirebaseAuth.currentUser?.phoneNumber.toString())
          .removeValue().addOnCompleteListener() { task ->
            show(requireActivity(), requireActivity().resources.getString(R.string.order_success))
            startActivity(Intent(requireActivity(), receiver_bottom::class.java))
          }

      }
    }
    val dashRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Item")
    val postListener = object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        if (dataSnapshot.exists()) {
          for (postSnapshot in dataSnapshot.children) {
            var item = postSnapshot.getValue(Item::class.java)
            if (arrayIDs.contains(item?.itemId) && item != null) {
              val i: Int = arrayIDs.indexOf(item.itemId)
              val current: Int = item.quantity - arrayQuantity[i]
              if (current > 0) {
                postSnapshot.ref.child("quantity").setValue(current)
                postSnapshot.ref.child("redeemed").setValue(false)
              } else {
                postSnapshot.ref.child("quantity").setValue(current)
                postSnapshot.ref.child("redeemed").setValue(true)
              }
            }
          }
        }
      }

      override fun onCancelled(databaseError: DatabaseError) {
        context?.let { show(it, databaseError.message) }
      }
    }
    dashRef.addListenerForSingleValueEvent(postListener)
  }

  }
package com.example.foodcloud.receiverNavigation.orders

import com.example.foodcloud.AdapterClassOrders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcloud.Item.Order
import com.example.foodcloud.R
import com.example.foodcloud.Util
import com.example.foodcloud.Util.show
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OrdersFragment: Fragment() {
    lateinit var ordersViewModel: OrdersViewModel
    lateinit var ref: DatabaseReference
    lateinit var list:ArrayList<Order>
    lateinit var recyclerView: RecyclerView
    internal lateinit var mFirebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ordersViewModel =
            ViewModelProviders.of(this).get(OrdersViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_receiver_orders, container, false)
        val textView: TextView = root.findViewById(R.id.text_o)
        ordersViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
            (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        })
        mFirebaseAuth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().reference.child("Orders").child(mFirebaseAuth.currentUser?.phoneNumber.toString())
        recyclerView = root.findViewById(R.id.order_list)


        return root
}
    override fun onStart() {
        super.onStart()
        if(ref!= null){
            val postListener = object: ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()){
                        list = arrayListOf()
                        for(postSnapshot in dataSnapshot.children){
                            list.add(postSnapshot.getValue(Order::class.java)!!)
                        }
                        val adapterClass = AdapterClassOrders(list)
                        recyclerView.adapter = adapterClass

                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    context?.let { Util.show(it, databaseError.message) }
                }
            }
            ref.addValueEventListener(postListener)
        }

    }
}
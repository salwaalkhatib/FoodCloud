package com.example.foodcloud.donor.navigation_bar.donormyitems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcloud.AdapterClass
import com.example.foodcloud.Item.Item
import com.example.foodcloud.R
import com.example.foodcloud.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    internal lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var useremail: String
    lateinit var ref: DatabaseReference
    lateinit var list:ArrayList<Item>
    lateinit var recyclerView: RecyclerView
    lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard_donor, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        mFirebaseAuth = FirebaseAuth.getInstance()
//        listView = root.findViewById(R.id.listview)
//        database = FirebaseDatabase.getInstance()
//        ref = database.getReference("Item")
//        list = mutableListOf()
//        adapter = ArrayAdapter<String>(root.context, R.layout.item_info, R.id.itemInfo, list)
//        useremail=mFirebaseAuth.currentUser?.email.toString()
//        val postListener = object: ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for(postSnapshot in snapshot.children){
//                    var item = postSnapshot.getValue(Item::class.java)
//                    if (item != null &&item.userEmail==useremail) {
//                        list.add(item.name)
//                    }
//                }
//                listView.adapter = adapter
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//        }
//
//        ref.addValueEventListener(postListener)
        ref = FirebaseDatabase.getInstance().reference.child("Item")
        recyclerView = root.findViewById(R.id.rv1)
        searchView = root.findViewById(R.id.SearchViewDonor)
        useremail=mFirebaseAuth.currentUser?.email.toString()
        return root
    }

    override fun onStart() {
        super.onStart()
        if(ref!= null){
            val postListener = object: ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI

                    if(dataSnapshot.exists()){
                        list = arrayListOf()
                        for(postSnapshot in dataSnapshot.children){
                            var item = postSnapshot.getValue(Item::class.java)
                            if(item!=null &&item.userEmail == useremail){
                            list.add(postSnapshot.getValue(Item::class.java)!!)
                        }}
                        val adapterClass = AdapterClass(list)
                        recyclerView.adapter = adapterClass

                    }


                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    context?.let { Util.show(it, databaseError.message) }
                }
            }
            ref.addValueEventListener(postListener)
        }
        if(searchView != null){
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {

                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    search(newText)
                    return true
                }
            })
        }

    }

    private fun search(s: String){
        var mylist : ArrayList<Item> = arrayListOf()

        for(object1: Item in list){
            if(object1.category.toLowerCase().contains(s.toLowerCase())){
                mylist.add(object1)
            }
            else if(object1.name.toLowerCase().contains(s.toLowerCase())){
                mylist.add(object1)
            }
        }
        val adapterClass = AdapterClass(mylist)
        recyclerView.adapter = adapterClass

    }
}
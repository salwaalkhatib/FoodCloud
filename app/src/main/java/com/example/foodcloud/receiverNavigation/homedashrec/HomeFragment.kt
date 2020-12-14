package com.example.foodcloud.receiverNavigation.homedashrec

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcloud.Item.Item
import com.example.foodcloud.R
import com.example.foodcloud.Util.show
import androidx.appcompat.widget.SearchView
import com.example.foodcloud.AdapterClassReceiver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment() {

  private lateinit var homeViewModel: HomeViewModel
    lateinit var ref: DatabaseReference
    lateinit var list:ArrayList<Item>
    lateinit var recyclerView: RecyclerView
    lateinit var searchView: SearchView
  private lateinit var infoBtn: ImageButton

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    homeViewModel =
    ViewModelProviders.of(this).get(HomeViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_homedashbrec, container, false)
    val textView: TextView = root.findViewById(R.id.text_home)
    homeViewModel.text.observe(viewLifecycleOwner, Observer {
      textView.text = it
      (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    })

    ref = FirebaseDatabase.getInstance().reference.child("Item")
    recyclerView = root.findViewById(R.id.rv)
    searchView = root.findViewById(R.id.SearchView)
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
              val item = postSnapshot.getValue(Item::class.java)!!
              if(item.quantity!! >0 && !item.expired){
              list.add(item)
            }}
             val adapterClass = AdapterClassReceiver(list)
             recyclerView.adapter = adapterClass

          }

        }

        override fun onCancelled(databaseError: DatabaseError) {
          context?.let { show(it, databaseError.message ) }
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
        } else if(object1.name.toLowerCase().contains(s.toLowerCase())){
          mylist.add(object1)
      } else if(object1.quantity.toString().contains(s.toLowerCase())){
          mylist.add(object1)
        }
    }
    val adapterClass = AdapterClassReceiver(mylist)
    recyclerView.adapter = adapterClass

  }
}
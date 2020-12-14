package com.example.foodcloud

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcloud.Item.Item

class AdapterClass(private val list: ArrayList<Item>): RecyclerView.Adapter<AdapterClass.MyViewHolder>() {
//    private val list: ArrayList<Item> = arrayListOf()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val id: TextView = itemView.findViewById(R.id.dealID)
        val desc : TextView = itemView.findViewById(R.id.descrip)
        val status: TextView = itemView.findViewById(R.id.status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.card_holder, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.id.text = list[position].name
        holder.desc.text = list[position].category
        if(list[position].expired && !list[position].redeemed) {
            holder.status.text = "Expired"
            holder.status.setTextColor(Color.RED)
        }else if(list[position].redeemed){
            holder.status.text = "Redeemed"
            holder.status.setTextColor(Color.parseColor("#5F813C"))
        }
        else{
            holder.status.text = "Pending"
            holder.status.setTextColor(Color.GRAY)
        }
    }
}
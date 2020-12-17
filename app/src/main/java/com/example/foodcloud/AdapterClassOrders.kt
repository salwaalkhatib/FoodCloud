package com.example.foodcloud

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcloud.Item.ItemDetailsActivity
import com.example.foodcloud.Item.Order
import com.example.foodcloud.Item.QRCode
import com.example.foodcloud.Util.show
import kotlin.collections.ArrayList


class AdapterClassOrders(private val list: ArrayList<Order>): RecyclerView.Adapter<AdapterClassOrders.MyViewHolder>() {
    class MyViewHolder(orderView: View) : RecyclerView.ViewHolder(orderView){
        val order: TextView = orderView.findViewById(R.id.itemname)
        val time: TextView = orderView.findViewById(R.id.exdate1)
        val date : TextView = orderView.findViewById(R.id.category1)
        val amount: TextView = orderView.findViewById(R.id.quantity1)
        val infoBtn: ImageButton = orderView.findViewById(R.id.arrowbtn)
        val redeemed: TextView = orderView.findViewById(R.id.redeemed)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.card_holder_receiver, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val nbr: Int = position + 1
        holder.order.text="Order $nbr"
        holder.time.text = list[position].time
        holder.date.text = list[position].date
        if(list[position].redeemed == "true"){
            holder.redeemed.text = "Redeemed"
            holder.redeemed.setTextColor(Color.parseColor("#5F813C"))
        }else if(list[position].redeemed == "false"){
            holder.redeemed.text = "Pending"
            holder.redeemed.setTextColor(Color.GRAY)
        }
        holder.amount.text = "Total Amount: "+list[position].totalAmount
        holder.infoBtn.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, QRCode::class.java)
            intent.putExtra("key", list[position].key)
            context.startActivity(intent)
        }
    }
}
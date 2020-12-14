package com.example.foodcloud

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcloud.Item.Item
import com.example.foodcloud.Item.ItemDetailsActivity
import com.example.foodcloud.Util.show
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AdapterClassReceiver(private val list: ArrayList<Item>): RecyclerView.Adapter<AdapterClassReceiver.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.itemname)
        val Expirydate: TextView = itemView.findViewById(R.id.exdate1)
        val category : TextView = itemView.findViewById(R.id.category1)
        val qt: TextView = itemView.findViewById(R.id.quantity1)
        val infoBtn: ImageButton = itemView.findViewById(R.id.arrowbtn)

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
        val formatter= SimpleDateFormat("dd-MM-yyyy")
        val datestring=formatter.format(Date(list[position].exdate))

        holder.Expirydate.text=datestring
        holder.name.text = list[position].name
        holder.category.text = list[position].category
        holder.qt.text = "Quantity: ".plus(list[position].quantity.toString())
        holder.infoBtn.setOnClickListener {
            val itemId:String = list[position].itemId
            val context = holder.itemView.context
            val intent = Intent(context, ItemDetailsActivity::class.java)
            intent.putExtra("id", itemId)
            context.startActivity(intent)
        }
    }
}
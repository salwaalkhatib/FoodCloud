package com.example.foodcloud.Item

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcloud.R

public class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) , View.OnClickListener {
    val txtItemName = itemView.findViewById(R.id.item_name_cart) as TextView
    val txtItemQuantity = itemView.findViewById(R.id.item_quantity_cart) as TextView
    val txtItemCategory = itemView.findViewById(R.id.item_cateogry_cart) as TextView

    private lateinit var itemClickListener: ItemClickListener


    override fun onClick(v: View?) {
        itemClickListener.onClick(v!!, adapterPosition, false)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

}
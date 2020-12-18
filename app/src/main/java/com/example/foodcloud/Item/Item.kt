package com.example.foodcloud.Item

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


public class Item {
    private var dateFormat: SimpleDateFormat? = null
    var itemId=""
    var userEmail=""
    var name: String =""
    var category: String =""
    var quantity: Int=0
    var initialQuantity: Int =0
    var phone: String =""
     var exdate: Long=0
    var redeemed: Boolean = false
    var expired: Boolean = false


    constructor()
    constructor(
        dateFormat: SimpleDateFormat?,
        itemId: String,
        userEmail: String,
        name: String,
        category: String,
        quantity: Int,
        initialQuantity: Int,
        phone: String,
        exdate: Long,
        redeemed: Boolean,
        expired: Boolean
    ) {
        this.dateFormat = dateFormat
        this.itemId = itemId
        this.userEmail = userEmail
        this.name = name
        this.category = category
        this.quantity = quantity
        this.initialQuantity = initialQuantity
        this.phone = phone
        this.exdate = exdate
        this.redeemed = redeemed
        this.expired = expired
    }

}
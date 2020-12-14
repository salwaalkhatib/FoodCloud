package com.example.foodcloud.Item

public class Cart {
    lateinit var itemName: String
    lateinit var iid: String
    lateinit var quantity: String
    lateinit var itemCategory: String
    lateinit var date: String
    lateinit var time:String
    constructor()
    constructor(
        itemName: String,
        iid: String,
        quantity: String,
        itemCategory: String,
        date: String,
        time: String
    ) {
        this.itemName = itemName
        this.iid = iid
        this.quantity = quantity
        this.itemCategory = itemCategory
        this.date = date
        this.time = time
    }


}
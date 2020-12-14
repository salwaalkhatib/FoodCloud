package com.example.foodcloud.Item

class Order {
    lateinit var totalAmount: String
    lateinit var date: String
    lateinit var time:String
    lateinit var key:String
    lateinit var itemID: String
    constructor()
    constructor(totalAmount: String, date: String, time: String, key: String, itemID: String) {
        this.totalAmount = totalAmount
        this.date = date
        this.time = time
        this.key = key
        this.itemID = itemID
    }


}
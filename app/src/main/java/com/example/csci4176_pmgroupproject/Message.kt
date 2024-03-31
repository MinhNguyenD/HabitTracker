package com.example.csci4176_pmgroupproject

data class Message(var messageId : String, val senderId : String, val receiverId : String, var message : String) {
    constructor(senderId: String, receiverId: String, message: String) : this("",senderId, receiverId, message)
    // this is required by firebase
    constructor() : this("","","")
}
package com.example.csci4176_pmgroupproject.Model

import com.example.csci4176_pmgroupproject.Database.DatabaseAPI

/*TODO: create var of type Array<Habit>*/
/*TODO: create badge feature*/
/*TODO: reward branch */
data class User(val uid:String){ lateinit var username: String
    var friends: Map<String, User> = mapOf()
    var badge : Badge = Badge.NONE

    // this is required by firebase
    constructor() : this("")


    // Method to notify friends about finished activity
    fun notifyFriends(message: String) {
        for(friend in friends){
            var messageObj = Message(this.uid, friend.key, message)
            friend.value.update(messageObj)
        }
    }

    fun update(message : Message) {
        DatabaseAPI.receiveMessage(this, message)
    }
}
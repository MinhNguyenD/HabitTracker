package com.example.csci4176_pmgroupproject

/*TODO: create var of type Array<Habit>*/
/*TODO: create badge feature*/
/*TODO: reward branch */
data class User(val uid:String){ lateinit var username: String
    private var friendsList: ArrayList<User> = arrayListOf()

    // this is required by firebase
    constructor() : this("")

    fun setFriendList(friendList : ArrayList<User>){
        friendsList = friendList
    }

    // Method to notify friends about finished activity
    fun notifyFriends(message: String) {
        for(friend in friendsList){
            var messageObj = Message(this.uid, friend.uid, message)
            friend.update(messageObj)
        }
    }

    fun update(message : Message) {
        DatabaseAPI.receiveMessage(this, message)
    }
}
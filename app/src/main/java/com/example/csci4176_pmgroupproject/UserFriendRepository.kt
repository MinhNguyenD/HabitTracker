package com.example.csci4176_pmgroupproject

import com.google.firebase.database.FirebaseDatabase

class UserFriendRepository {
    private val databaseReference = FirebaseDatabase.getInstance().getReference("users")

    fun getUserByUsername(username: String, callback: (User?) -> Unit) {
        // Add logic to query the database by username
    }

    fun addFriend(currentUserUid: String, friendUid: String) {
        // Add logic to add a friend relationship
    }

    fun removeFriend(currentUserUid: String, friendUid: String) {
        // Add logic to remove a friend relationship
    }
}

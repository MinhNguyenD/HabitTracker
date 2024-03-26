package com.example.csci4176_pmgroupproject

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

val database:FirebaseDatabase = Firebase.database
val users:DatabaseReference = database.getReference("users")
val auth:FirebaseAuth = FirebaseAuth.getInstance()


object DatabaseAPI {

    private lateinit var someUser: User;
    lateinit var currentUser: FirebaseUser

    /**
     * This function will do the Firebase work for [login in]
     * @param email: A string consisting of a valid email address
     * @param password: A string consisting of the users password entered.
     * @return Task<com.google.firebase.auth.AuthResult>: The result from the request made to firebase
     * create an OnCompleteListener for displaying results. This function will automatically set currentUser
     * @currentUser can be accessed from anywhere, as long as DatabaseAPI is imported.
     * TODO: Add validation of strings passed
     * */
    fun emailLogin(email:String, password:String
    ): Task<com.google.firebase.auth.AuthResult> {
        var task: Task<com.google.firebase.auth.AuthResult> = auth.signInWithEmailAndPassword(email, password)
        task.addOnCompleteListener {
            if (task.isSuccessful){
                currentUser = auth.currentUser!!
                /*
                * TODO: create basic User object
                * TODO: push basic User object to [users]
                */
            }
        }
        return task
    }
    /**
     * This function will do the Firebase work for [sign up]
     * @param email: A string consisting of a valid email address
     * @param password: A string consisting of the users password entered.
     * @return Task<com.google.firebase.auth.AuthResult>: The result from the request made to firebase
     * create an OnCompleteListener for displaying results. This function will automatically set currentUser
     * @currentUser can be accessed from anywhere, as long as DatabaseAPI is imported.
     * TODO: Add validation of strings passed
     * */
    fun emailSignup(email:String, password: String): Task<com.google.firebase.auth.AuthResult> {
        var task: Task<com.google.firebase.auth.AuthResult> = auth.createUserWithEmailAndPassword(email, password)
        task.addOnCompleteListener {
            if (task.isSuccessful){
                currentUser = auth.currentUser!!
                /*
                * TODO: create basic User object
                * TODO: push basic User object to [users]
                */
            }
        }
        return task
    }

    // Add a friend connection
    fun addFriend(userId: String, friendUserId: String): Task<Void> {
        return users.child(userId).child("friends").child(friendUserId).setValue(true)
    }

    // Remove a friend connection
    fun removeFriend(userId: String, friendUserId: String): Task<Void> {
        return users.child(userId).child("friends").child(friendUserId).removeValue()
    }

    // Search for a user by username
    fun searchForUserByUsername(username: String, callback: (User?) -> Unit) {
        users.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var foundUser: User? = null
                    for (userSnapshot in snapshot.children) {
                        foundUser = userSnapshot.getValue(User::class.java)
                        break // Assuming usernames are unique, we break after finding the first match
                    }
                    callback(foundUser)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "searchForUserByUsername:onCancelled", databaseError.toException())
                    callback(null)
                }
            })
    }



}

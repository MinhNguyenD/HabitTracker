package com.example.csci4176_pmgroupproject

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
val activities : DatabaseReference = database.getReference("activities")
val users:DatabaseReference = database.getReference("users")
val auth:FirebaseAuth = FirebaseAuth.getInstance()

object DatabaseAPI {

    lateinit var currentUser: FirebaseUser
    lateinit var user: User // The user Object associated with the current user
    private lateinit var someUser: User;
    private lateinit var activityList : ArrayList<TaskModel>

    /**
     * This function will do the Firebase work for [login in]
     * @param email: A string consisting of a valid email address
     * @param password: A string consisting of the users password entered.
     * @return Task<com.google.firebase.auth.AuthResult>: The result from the request made to firebase
     * create an OnCompleteListener for displaying results. This function will automatically set currentUser
     * @currentUser can be accessed from anywhere, as long as DatabaseAPI is imported.
     * TODO: Add validation of strings passed
     * */
    fun emailLogin(email:String, password:String): Task<com.google.firebase.auth.AuthResult> {
        var task: Task<com.google.firebase.auth.AuthResult> = auth.signInWithEmailAndPassword(email, password)
        task.addOnCompleteListener {
            if (task.isSuccessful){
                currentUser = auth.currentUser!!
                /*
                * TODO: create basic User object
                * TODO: pull basic User object from [users]
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
        val task: Task<com.google.firebase.auth.AuthResult> = auth.createUserWithEmailAndPassword(email, password)
        task.addOnCompleteListener {
            if (task.isSuccessful){
                currentUser = auth.currentUser!!
            }
        }
        return task
    }

    fun updateUser(uid: String, username:String): Task<Void> {
        user = User(currentUser.uid)
        user.username = username
        return users.child(uid).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.w("Sign-up", "Successfully created user")
            }else{
                Log.w("Sign-up: Error","An Error occurred!")
            }
        }
    }
    fun getAllActivity() : List<TaskModel>{
        activityList = ArrayList()
        activities.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                activityList.clear()
                for (entry in snapshot.children){
                    val taskType = entry.child("type").value.toString()
                    when(taskType){
                        ActivityModelEnums.CHECKED.toString() -> activityList.add(entry.getValue(CheckedTaskModel::class.java)!!)
                        ActivityModelEnums.COUNTABLE.toString() -> activityList.add(entry.getValue(CountableTaskModel::class.java)!!)
                        ActivityModelEnums.TIMED.toString() -> activityList.add(entry.getValue(TimedTaskModel::class.java)!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        return activityList
    }

    fun saveActivity(activity : TaskModel): Task<Void> {
        return activities.child(activity.taskId)
            .setValue(activity)
    }

    fun deleteActivity(activityId: String): Task<Void> {
        return activities.child(activityId).removeValue()
    }
}

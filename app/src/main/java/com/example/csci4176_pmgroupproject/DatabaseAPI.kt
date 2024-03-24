package com.example.csci4176_pmgroupproject

import android.util.Log
import com.example.csci4176_pmgroupproject.Model.ActivityModel
import com.example.csci4176_pmgroupproject.Model.CheckedActivityModel
import com.example.csci4176_pmgroupproject.Model.CountableActivityModel
import com.example.csci4176_pmgroupproject.Model.TimedActivityModel
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
import java.time.DayOfWeek
import java.time.LocalDate

val database:FirebaseDatabase = Firebase.database
val activities : DatabaseReference = database.getReference("activities")
val dailyActivity : DatabaseReference = database.getReference("dailyActivities")
val users:DatabaseReference = database.getReference("users")
val auth:FirebaseAuth = FirebaseAuth.getInstance()

/**
 * DatabaseAPI: Handles interactions with Firebase Database.
 */
object DatabaseAPI {
    lateinit var currentUser: FirebaseUser
    lateinit var user: User // The user Object associated with the current user
    private lateinit var someUser: User
    private lateinit var activityList : ArrayList<ActivityModel>

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
        val task: Task<com.google.firebase.auth.AuthResult> = auth.signInWithEmailAndPassword(email, password)
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

    /**
     * Updates the user information in the database.
     * @param uid: The user ID.
     * @param username: The username to update.
     * @return Task<Void>: The result of the database operation.
     */
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

    /**
     * Retrieves an activity from the database by its ID.
     * @param id: The ID of the activity to retrieve.
     * @param callback: A callback function to handle the retrieved activity.
     */
    fun getActivityById(id : String, callback: (ActivityModel) -> Unit){
        return activities.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Retrieve the activity object from the dataSnapshot
                val activity = dataSnapshot.getValue(ActivityModel::class.java)
                if (activity != null) {
                    callback(activity)
                } else {
                    // Activity not found
                    Log.w("Error:", "Activity not found" )
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that occurred while retrieving the data
            }
        })
    }

    /**
     * Retrieves all activities from the database.
     * @param callback: A callback function to handle the retrieved activities.
     */
    fun getAllActivity(callback: (ArrayList<ActivityModel>) -> Unit){
        activityList = ArrayList()
        activities.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                activityList.clear()
                for (entry in snapshot.children){
                    val taskType = entry.child("type").value.toString()
                    when(taskType){
                        ActivityModelEnums.CHECKED.toString() -> activityList.add(entry.getValue(CheckedActivityModel::class.java)!!)
                        ActivityModelEnums.COUNTABLE.toString() -> activityList.add(entry.getValue(CountableActivityModel::class.java)!!)
                        ActivityModelEnums.TIMED.toString() -> activityList.add(entry.getValue(TimedActivityModel::class.java)!!)
                    }
                }
                callback(activityList)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    /**
     * Retrieves today's activities from the database.
     * @param callback: A callback function to handle today's activities.
     */
    fun getDailyActivity(callback: (ArrayList<ActivityModel>) -> Unit){
        activityList = ArrayList()
        activities.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                activityList.clear()
                for (entry in snapshot.children){
                    var activity : ActivityModel = CheckedActivityModel()
                    val taskType = entry.child("type").value.toString()
                    when(taskType){
                        ActivityModelEnums.CHECKED.toString() -> activity = entry.getValue(CheckedActivityModel::class.java)!!
                        ActivityModelEnums.COUNTABLE.toString() -> activity = entry.getValue(CountableActivityModel::class.java)!!
                        ActivityModelEnums.TIMED.toString() ->activity = entry.getValue(TimedActivityModel::class.java)!!
                    }
                    if(isTodayActivity(activity)){
                        activityList.add(activity)
                    }
                }
                callback(activityList)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    /**
     * Saves today's activities to the database.
     * @param arrayList: The list of activities to save.
     */
    fun saveDailyActivities(arrayList: ArrayList<ActivityModel>){
        val dateRef = dailyActivity.child(LocalDate.now().toString())
        for(activity in arrayList){
            dateRef.child(activity.taskId).setValue(activity)
        }
    }

    /**
     * Converts the Java DayOfWeek enum to the custom ActivityModelDayOfWeek enum.
     * @param calendarDay: The Java DayOfWeek enum representing the day of the week.
     * @return The corresponding ActivityModelDayOfWeek enum.
     * @throws IllegalArgumentException if the day of the week is invalid.
     */
    private fun convertDayOfWeek(calendarDay: DayOfWeek): ActivityModelDayOfWeek {
        return when (calendarDay) {
            DayOfWeek.MONDAY -> ActivityModelDayOfWeek.MONDAY
            DayOfWeek.TUESDAY -> ActivityModelDayOfWeek.TUESDAY
            DayOfWeek.WEDNESDAY -> ActivityModelDayOfWeek.WEDNESDAY
            DayOfWeek.THURSDAY -> ActivityModelDayOfWeek.THURSDAY
            DayOfWeek.FRIDAY -> ActivityModelDayOfWeek.FRIDAY
            DayOfWeek.SATURDAY -> ActivityModelDayOfWeek.SATURDAY
            DayOfWeek.SUNDAY -> ActivityModelDayOfWeek.SUNDAY
            else -> throw IllegalArgumentException("Invalid day of the week: $calendarDay")
        }
    }

    /**
     * Checks if an activity is scheduled for today.
     * @param activity: The activity to check.
     * @return True if the activity is scheduled for today, false otherwise.
     */
    private fun isTodayActivity(activity: ActivityModel) : Boolean{
        val today = LocalDate.now()
        val startDate = LocalDate.parse(activity.startDate)

        // not matching day of the week and is not daily frequency
        if(activity.dayOfWeek != convertDayOfWeek(today.dayOfWeek) && !(activity.frequency == ActivityModelFrequency.DAILY || activity.frequency == ActivityModelFrequency.MONTHLY)){
            return false
        }

        return when (activity.frequency) {
            ActivityModelFrequency.DAILY -> true
            ActivityModelFrequency.WEEKLY -> true
            ActivityModelFrequency.BIWEEKLY -> {
                val weeksPassed = startDate.until(today, java.time.temporal.ChronoUnit.WEEKS)
                if(weeksPassed % 2 == 0L && weeksPassed >= 0L){
                    return true
                }
                return false
            }
            ActivityModelFrequency.MONTHLY -> today.dayOfMonth == startDate.dayOfMonth
            else -> false
        }
    }

    /**
     * Creates a new activity in the database.
     * @param activity: The activity to create.
     * @return Task<Void>: A task representing the completion of the operation.
     */
    fun createActivity(activity : ActivityModel) : Task<Void> {
        //push will auto generate ID for activity
        val newActivityRef = activities.push()

        // Set the activity ID
        activity.taskId = newActivityRef.key!!

        return newActivityRef.setValue(activity)
    }

    /**
     * Updates an existing activity in the database.
     * @param activity: The updated activity object.
     * @return Task<Void>: A task representing the completion of the operation.
     */
    fun updateActivity(activity : ActivityModel): Task<Void> {
        return activities.child(activity.taskId)
            .setValue(activity)
    }


    /**
     * Deletes an activity from the database.
     * @param activityId: The ID of the activity to delete.
     * @return Task<Void>: A task representing the completion of the operation.
     */
    fun deleteActivity(activityId: String): Task<Void> {
        return activities.child(activityId).removeValue()
    }

    /**
     * Get all of the daily activities on a given date
     *
     */
    fun getDailyActivitiesOnDate(selectedDate : String, callback: (ArrayList<ActivityModel>) -> Unit){
        activityList = ArrayList()
        return dailyActivity.child(selectedDate).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                activityList.clear()
                // Retrieve the activity object from the dataSnapshot
                for (entry in dataSnapshot.children){
                    activityList.add(convertActivity(entry))
                }
                callback(activityList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that occurred while retrieving the data
            }
        })
    }

    private fun convertActivity(entry : DataSnapshot): ActivityModel {
        val taskType = entry.child("type").value.toString()
        when(taskType){
            ActivityModelEnums.CHECKED.toString() -> return entry.getValue(CheckedActivityModel::class.java)!!
            ActivityModelEnums.COUNTABLE.toString() -> return entry.getValue(CountableActivityModel::class.java)!!
            ActivityModelEnums.TIMED.toString() -> return entry.getValue(TimedActivityModel::class.java)!!
            else -> throw IllegalArgumentException("Invalid task type: $taskType")
        }
    }
}

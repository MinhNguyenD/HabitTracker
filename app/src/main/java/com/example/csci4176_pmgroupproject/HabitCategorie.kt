package com.example.csci4176_pmgroupproject

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HabitCategorie : AppCompatActivity() {

    // Reference to the "habits" node in the database
    private val habitsRef = FirebaseDatabase.getInstance().getReference("habits")

    // These habits exist by default
    private val defaultHabits = listOf("Health", "Fitness", "Study", "Lifestyle", "Art", "Financial", "Social")

    // TODO: Check whether this userId is correct or not
    // Get the user ID of the currently logged in user
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.habit_item)

        val addCategoryBtn: ImageButton = findViewById(R.id.addCategoryButton)
        val categories = mutableListOf<String>()

        //
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // If there are no children in the "habit" node, add the default categories in
                // (this is made just in case the habit node is deleted from the database for whatever reason)
                if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                    // Habits exist in the database, do nothing
                    Log.d(TAG, "Habits already exist in the database")

                    // Iterate through all children under "habit" node
                    for (habitSnapshot in dataSnapshot.children) {
                        // Retrieve all the default categories as well as the custom ones this logged in user has created
                        val currUserId = habitSnapshot.child("userId").getValue(String::class.java)
                        if(currUserId == null || currUserId == userId) {
                            // Get the value of "habitName" for each child
                            val habitName = habitSnapshot.child("habitName").getValue(String::class.java)
                            if (habitName != null) {
                                categories.add(habitName)
                            }
                        }
                    }

                    val customAdapter = Habit_Categories_Adapter(categories)
                    val recyclerView: RecyclerView = findViewById(R.id.categoryRecyclerView)
                    connectAdapter(recyclerView, customAdapter)

                    // Set a listener to listen for clicks on each item in the list of the categories
                    customAdapter.setOnClickItemListener(object : Habit_Categories_Adapter.OnClickItemListener {
                        // Display the Create_Activity screen on this activity
                        override fun onClickItem(position: Int) {
                            val selectedCategory = categories[position]

                            val postListener = object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for(habitSnapshot in dataSnapshot.children) {
                                        val currHabitName = habitSnapshot.child("habitName").getValue(String::class.java)
                                        if (currHabitName == selectedCategory) {
                                            val selectedId = habitSnapshot.key.toString()

                                            Log.e(TAG, "Selected habit ID: " + selectedId)

                                            Toast.makeText(
                                                this@HabitCategorie, "You Selected: "+ selectedCategory,
                                                Toast.LENGTH_LONG
                                            ).show()

                                            // Use the root container to iteratively set the visibility
                                            // of each view to GONE so that we can display our fragment
                                            val rootLayout : LinearLayout = findViewById(R.id.root_container)
                                            for(i in 0..rootLayout.childCount-1){
                                                val currView = rootLayout.getChildAt(i)
                                                currView.visibility = View.GONE
                                            }

                                            // The create activity fragment is displayed on the same HabitCategorie activity
                                            // to save memory usage
                                            val createActivityFrag = CreateActivity.newInstance(selectedId, selectedCategory)
                                            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                                            transaction.replace(R.id.root_container, createActivityFrag).commit()

                                        }
                                        else {
                                            Toast.makeText(
                                                this@HabitCategorie,
                                                "OH NO ITS NULL!",
                                                Toast.LENGTH_LONG
                                            )
                                        }
                                    }
                                }
                                // Log database errors that may occur during this
                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle error
                                    Log.e(TAG, "Error getting habit ID from database", databaseError.toException())
                                }

                            }
                            habitsRef.addListenerForSingleValueEvent(postListener)
                        }
                    })

                    // Set a listener to listen for clicks on the save button
                    customAdapter.setOnClickSaveItemListener(object : Habit_Categories_Adapter.OnClickSaveItemListener {
                        override fun onClickSaveItem(newCategoryName: String) {
                            // Add the new category name to the database
                            addCustomCategory(newCategoryName)

                            // Notify the adapter to leave "Adding category" mode
                            customAdapter.setIsAddingCategory(false)

                            // Refresh the page with the newly added category
                            customAdapter.notifyItemInserted(categories.size-1)

                            // Display a pop-up message to the user
                            Toast.makeText(
                                this@HabitCategorie, "The new category ${newCategoryName} " +
                                        "has been added!", Toast.LENGTH_LONG
                            ).show()
                        }
                    })

                    // Set a listener to listen for clicks on the top right "Add category" button
                    addCategoryBtn.setOnClickListener{
                        // Notify the adapter to go to "Adding category" mode
                        customAdapter.setIsAddingCategory(true)
                        categories.add("")
                        customAdapter.setNewItemPosition(categories.size-1)
                        recyclerView.smoothScrollToPosition(categories.size - 1)

                        customAdapter.notifyItemInserted(categories.size)
                    }
                }
                else {
                    // Habits do not exist in the database, add default habits
                    Log.d(TAG, "No habits found in the database, adding default habits")
                    addDefaultCategories()
                }
            }
            // Log database errors that may occur during this
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Log.e(TAG, "Error checking habits in database", databaseError.toException())
            }
        }
        habitsRef.addValueEventListener(postListener)
    }

    fun connectAdapter(recyclerView : RecyclerView, customAdapter : Habit_Categories_Adapter){
        val llm = LinearLayoutManager(this)
        llm.setOrientation(LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = llm
        recyclerView.adapter = customAdapter
    }

    // This function adds a custom category for the currently logged in user to the database
    fun addCustomCategory(habitName: String){
        // Generate a unique key for the new habit
        val habitId = habitsRef.push().key

        val habit = habitId?.let { Habit(it, userId, habitName) }

        // Add the habit to the generated habitId
        habitId?.let{
            habitsRef.child(it).setValue(habit)
                .addOnSuccessListener {
                    Toast.makeText(baseContext, "The new category has been created.", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener{ e ->
                    Toast.makeText(baseContext, "Could not create a new category. Please try again later.",
                        Toast.LENGTH_LONG).show()
                    // Print to log debug messages
                    Log.e(TAG, "Error adding custom habit", e)
                }
        }
    }

    // This function adds the default categories to the database ONLY if the database has no content under the "habit" node for whatever reason
    fun addDefaultCategories(){
        // Iterate through the list of default habits and add them to the database
        for (habitName in defaultHabits) {
            // Generate a unique key for the new habit
            val habitId = habitsRef.push().key

            val habit = habitId?.let { Habit(it, null, habitName) }

            // Add the habit to the generated habitId
            habitId?.let {
                habitsRef.child(it).setValue(habit)
                    .addOnSuccessListener {
                        // Print to log debug messages
                        Log.d(TAG, "Default habit added successfully")
                    }
                    .addOnFailureListener { e ->
                        // Print to log debug messages
                        Log.e(TAG, "Error adding default habit", e)
                    }
            }
        }
    }
}

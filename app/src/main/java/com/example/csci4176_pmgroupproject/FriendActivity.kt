package com.example.csci4176_pmgroupproject
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class FriendActivity : AppCompatActivity() {
    // Views from the layout
//    private lateinit var addButton: Button
//    private lateinit var deleteButton: Button
    private lateinit var searchView: SearchView
//    private lateinit var textView: TextView

    // Assuming 'DatabaseAPI' is a singleton object you can access directly
   private val databaseAPI = DatabaseAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        // Initialize views
//        addButton = findViewById(R.id.AddButton)
//        deleteButton = findViewById(R.id.DeleteButton)
        searchView = findViewById(R.id.searchView)
//        textView = findViewById(R.id.textView3)

        // Set up the search view to listen for user queries
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    databaseAPI.searchForUserByUsername(it) { user ->
                        if (user != null) {
//                            textView.text = user.username
//                            // Show add button if a user is found
//                            addButton.visibility = Button.VISIBLE
                        } else {
//                            textView.text = "User not found"
//                            addButton.visibility = Button.GONE
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle text change if necessary
                return false
            }
        })

        // Set up the add button
//        addButton.setOnClickListener {
//            val friendUsername = searchView.query.toString()
//            databaseAPI.searchForUserByUsername(friendUsername) { user ->
//                user?.let {
//                    databaseAPI.addFriend(databaseAPI.currentUser.uid, it.uid)
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                textView.text = "Added ${it.username} as a friend"
//                            } else {
//                                textView.text = "Failed to add friend"
//                            }
//                        }
//                }
//            }
//        }

        // Set up the delete button
//        deleteButton.setOnClickListener {
//            val friendUsername = searchView.query.toString()
//            databaseAPI.searchForUserByUsername(friendUsername) { user ->
//                user?.let {
//                    databaseAPI.removeFriend(databaseAPI.currentUser.uid, it.uid)
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                textView.text = "Removed ${it.username} as a friend"
//                            } else {
//                                textView.text = "Failed to remove friend"
//                            }
//                        }
//                }
//            }
//        }
    }
}

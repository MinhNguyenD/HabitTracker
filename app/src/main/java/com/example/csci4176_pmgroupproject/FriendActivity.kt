package com.example.csci4176_pmgroupproject
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csci4176_pmgroupproject.Model.ActivityModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class FriendActivity : AppCompatActivity() {
    // Views from the layout
//    private lateinit var friendListView : RecyclerView
    private lateinit var searchListView : RecyclerView
    private lateinit var friendAdapter : FriendAdapter
    private lateinit var searchFriendAdapter : FriendAdapter
//    private lateinit var friendList : ArrayList<User>
    private lateinit var searchUserList : ArrayList<User>
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        // Initialize views
        searchView = findViewById(R.id.searchView)
//        friendListView = findViewById(R.id.friendList)
        searchListView = findViewById(R.id.searchRecyclerList)
//        friendListView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchListView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        friendAdapter =  FriendAdapter(friendList, R.layout.friend_item)
//        friendListView.adapter = friendAdapter

        // Set up the search view to listen for user queries
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                // Handle text change if necessary
                query?.let {
                    DatabaseAPI.searchForUserByUsername(it) { users ->
                        searchUserList = users
                        searchFriendAdapter = FriendAdapter(searchUserList, R.layout.add_friend_item)
                        searchListView.adapter = searchFriendAdapter
                    }
                }
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

package com.example.csci4176_pmgroupproject.Activities
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csci4176_pmgroupproject.Database.DatabaseAPI
import com.example.csci4176_pmgroupproject.Adapters.FriendAdapter
import com.example.csci4176_pmgroupproject.R
import com.example.csci4176_pmgroupproject.Model.User
import com.google.firebase.auth.FirebaseAuth

class FriendActivity : BaseActivity() {
    // Views from the layout
    private lateinit var friendListView : RecyclerView
    private lateinit var searchListView : RecyclerView
    private lateinit var friendAdapter : FriendAdapter
    private lateinit var searchFriendAdapter : FriendAdapter
    private lateinit var friendList : ArrayList<User>
    private lateinit var searchUserList : ArrayList<User>
    private lateinit var searchView: SearchView
    private lateinit var inboxButton: ImageButton
    private lateinit var inboxNumber : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        DatabaseAPI.currentUser = FirebaseAuth.getInstance().currentUser!!
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
        // Initialize views
        searchView = findViewById(R.id.searchView)
        searchListView = findViewById(R.id.searchRecyclerList)
        friendListView = findViewById(R.id.friendList)
        inboxButton = findViewById(R.id.inboxButton)
        inboxNumber = findViewById(R.id.inboxNumber)

        searchListView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        DatabaseAPI.getCurrentUserInbox { messages ->
            if (messages.size > 0) {
                inboxNumber.text = messages.size.toString()
                inboxButton.setColorFilter(ContextCompat.getColor(this, R.color.red))
            }
        }

        inboxButton.setOnClickListener{
            startActivity(Intent(this, InboxActivity::class.java))
        }

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
                        searchFriendAdapter =
                            FriendAdapter(searchUserList, R.layout.add_friend_item)
                        searchListView.adapter = searchFriendAdapter
                    }
                }
                return false
            }
        })


        friendListView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        DatabaseAPI.getCurrentUserFriends { friends ->
            friendList = ArrayList()
            friendList.clear()
            for (friend in friends) {
                friendList.add(friend.value)
            }
            friendAdapter = FriendAdapter(friendList, R.layout.friend_item)
            friendListView.adapter = friendAdapter
        }
    }
}

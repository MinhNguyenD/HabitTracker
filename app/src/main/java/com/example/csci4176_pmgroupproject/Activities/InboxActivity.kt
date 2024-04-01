package com.example.csci4176_pmgroupproject.Activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csci4176_pmgroupproject.Database.DatabaseAPI
import com.example.csci4176_pmgroupproject.Adapters.InboxAdapter
import com.example.csci4176_pmgroupproject.Model.Message
import com.example.csci4176_pmgroupproject.R

class InboxActivity : BaseActivity() {
    private lateinit var inboxRecyclerView: RecyclerView
    private lateinit var messageList : ArrayList<Message>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)
        inboxRecyclerView = findViewById(R.id.inboxRecyclerView)
        inboxRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        DatabaseAPI.getCurrentUserInbox { messages ->
            if (messages.size > 0) {
                messageList = messages
                val inboxAdapter = InboxAdapter(messageList)
                inboxRecyclerView.adapter = inboxAdapter
            }
        }
    }
}
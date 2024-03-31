package com.example.csci4176_pmgroupproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InboxActivity : BaseActivity() {
    private lateinit var inboxRecyclerView: RecyclerView
    private lateinit var messageList : ArrayList<Message>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)
        inboxRecyclerView = findViewById(R.id.inboxRecyclerView)
        inboxRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        DatabaseAPI.getCurrentUserInbox { messages ->
            if(messages.size <= 0){

            }
            else{
                messageList = messages
                val inboxAdapter =  InboxAdapter(messageList)
                inboxRecyclerView.adapter = inboxAdapter
            }
        }
    }
}
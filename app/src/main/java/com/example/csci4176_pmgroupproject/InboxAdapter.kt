package com.example.csci4176_pmgroupproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InboxAdapter (private var messages : ArrayList<Message>) : RecyclerView.Adapter<InboxAdapter.InboxViewHolder>() {
    inner class InboxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderName: TextView = itemView.findViewById(R.id.senderName)
        val message: TextView = itemView.findViewById(R.id.message)

        init {
        }
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxViewHolder {
        // Inflate the city item layout and create a new CityViewHolder
        val messageItemView = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return InboxViewHolder(messageItemView)
    }


    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    override fun getItemCount(): Int {
        return messages.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: InboxViewHolder, position: Int) {
        DatabaseAPI.getUserById(messages[position].senderId){user ->
            holder.senderName.text = user.username
            holder.message.text = messages[position].message
        }
    }
}
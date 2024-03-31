package com.example.csci4176_pmgroupproject
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class FriendAdapter (private var friends : ArrayList<User>, private var itemLayout : Int) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>(){

    inner class FriendViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val friendUserName : TextView = itemView.findViewById(R.id.friendUserName)
        val addFriendButton : Button? = itemView.findViewById(R.id.addFriendButton)
        val unfriendButton : Button? = itemView.findViewById(R.id.unfriendButton)
        init {
            if(unfriendButton != null){
                unfriendButton.setOnClickListener{
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        DatabaseAPI.removeFriend(friends[position].uid)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                } else {

                                }
                            }

                    }
                }
            }
            if(addFriendButton != null){
                addFriendButton.setOnClickListener{
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        DatabaseAPI.addFriend(friends[position])
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                } else {

                                }
                            }
                    }
                }
            }
        }
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        // Inflate the city item layout and create a new CityViewHolder
        val friendItemView = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
        return FriendViewHolder(friendItemView)
    }


    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    override fun getItemCount(): Int {
        return friends.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        // Bind city data to the views in the ViewHolder
        holder.friendUserName.text = friends[position].username
    }
}
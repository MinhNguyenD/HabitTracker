package com.example.csci4176_pmgroupproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.csci4176_pmgroupproject.Model.ActivityModel

class DailyActivityAdapter (private var activities : ArrayList<ActivityModel>, private var clickListener: TodoItemClickListener) : RecyclerView.Adapter<DailyActivityAdapter.DailyActivityViewHolder>(){
    inner class DailyActivityViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val activityTitleView : TextView = itemView.findViewById(R.id.activityTitle)
        val streak : TextView = itemView.findViewById(R.id.streak)
        val progress : TextView = itemView.findViewById(R.id.progress)
        val finishButton : Button = itemView.findViewById(R.id.finishButton)
        init {
            finishButton.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val finishIntent = Intent(itemView.context, FinishActivity::class.java)
                    finishIntent.putExtra("selectedActivityId", activities[position].taskId)
                    itemView.context.startActivity(finishIntent)
                    clickListener.onItemFinishClick(position)
                }
            }
        }
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyActivityViewHolder {
        // Inflate the city item layout and create a new CityViewHolder
        val activityItemView = LayoutInflater.from(parent.context).inflate(R.layout.acitivity_item_checkable, parent, false)
        return DailyActivityViewHolder(activityItemView)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    override fun getItemCount(): Int {
        return activities.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: DailyActivityViewHolder, position: Int) {
        // Bind city data to the views in the ViewHolder
        holder.activityTitleView.text = activities[position].title
        holder.streak.text = activities[position].streak.toString()
    }
}
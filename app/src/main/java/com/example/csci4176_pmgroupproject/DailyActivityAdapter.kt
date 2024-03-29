package com.example.csci4176_pmgroupproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.csci4176_pmgroupproject.Model.ActivityModel
import com.example.csci4176_pmgroupproject.Model.CheckedActivityModel
import com.example.csci4176_pmgroupproject.Model.CountableActivityModel
import com.example.csci4176_pmgroupproject.Model.TimedActivityModel

class DailyActivityAdapter (private var activities : ArrayList<ActivityModel>, private var clickListener: TodoItemClickListener?, private var viewOnly : Boolean) : RecyclerView.Adapter<DailyActivityAdapter.DailyActivityViewHolder>(){
    constructor(activities : ArrayList<ActivityModel>, viewOnly : Boolean) : this(activities, null, viewOnly)
    inner class DailyActivityViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val activityTitleView : TextView = itemView.findViewById(R.id.activityTitle)
        val streak : TextView = itemView.findViewById(R.id.streak)
        val progress : TextView? = itemView.findViewById(R.id.progress)
        val finishButton : Button? = itemView.findViewById(R.id.finishButton)
        init {
            if(finishButton != null){
                finishButton.setOnClickListener{
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val finishIntent = Intent(itemView.context, FinishActivity::class.java)
                        finishIntent.putExtra("selectedActivityId", activities[position].taskId)
                        itemView.context.startActivity(finishIntent)
                        if(clickListener != null){
                            clickListener?.onItemFinishClick(position)
                        }
                    }
                }
            }
        }
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyActivityViewHolder {
        var itemLayout : Int = 0
        if(viewOnly){
//            itemLayout = R.layout.acitivity_item_view_only
        }

        when (viewType) {
            TYPE_CHECK_ACTIVITY -> {
                itemLayout = R.layout.acitivity_item_checkable
            }
            TYPE_TIME_ACTIVITY -> {
                itemLayout = R.layout.activity_item_timed
            }
            TYPE_COUNT_ACTIVITY -> {
                itemLayout = R.layout.activity_item_countable
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

        // Inflate the city item layout and create a new CityViewHolder
        val activityItemView = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
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



    /**
     * Gets the view type of the item at the specified position.
     * @param position The position of the item.
     * @return The view type of the item.
     * @throws IllegalArgumentException if the activity type is invalid.
     */
    override fun getItemViewType(position: Int): Int {
        val activity = activities[position]
        return when (activity) {
            is CheckedActivityModel -> TYPE_CHECK_ACTIVITY
            is TimedActivityModel -> TYPE_TIME_ACTIVITY
            is CountableActivityModel -> TYPE_COUNT_ACTIVITY
            else -> throw IllegalArgumentException("Invalid activity type")
        }
    }

    companion object {
        // Constants for view types
        private const val TYPE_CHECK_ACTIVITY = 0
        private const val TYPE_TIME_ACTIVITY = 1
        private const val TYPE_COUNT_ACTIVITY = 2
    }

}
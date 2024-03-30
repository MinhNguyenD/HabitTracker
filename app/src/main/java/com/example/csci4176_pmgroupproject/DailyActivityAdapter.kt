package com.example.csci4176_pmgroupproject

import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
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
        val remaining : TextView? = itemView.findViewById(R.id.remaining)
        val startToggle : ToggleButton? = itemView.findViewById(R.id.startButton)
        val startTime : TextView? = itemView.findViewById(R.id.startTime)
        init {
            finishButton?.setOnClickListener{
                val position = adapterPosition
                if(activities[position] is CheckedActivityModel){
                    activities[position].isFinished = true
                    activities[position].streak += 1
                    DatabaseAPI.updateActivity(activities[position])
                    val finishIntent = Intent(itemView.context, FinishActivity::class.java)
                    finishIntent.putExtra("selectedActivityId", activities[position].taskId)
                    progress?.text = "Finish"
                    progress?.setTextColor(itemView.resources.getColor(R.color.submit))
                    itemView.context.startActivity(finishIntent)
                    clickListener?.onItemFinishClick(position)
                }
                else if(activities[position] is CountableActivityModel){
                    val countActivity : CountableActivityModel = activities[position] as CountableActivityModel
                    countActivity.decrementRemaining()
                    val currentCount = countActivity.getRemaining()
                    remaining?.text = "${currentCount}"
                    progress?.text = "In Progress"
                    progress?.setTextColor(itemView.resources.getColor(R.color.yellow))
                    if (currentCount == 0) {
                        activities[position].isFinished = true
                        activities[position].streak += 1
                        DatabaseAPI.updateActivity(activities[position])
                        val finishIntent = Intent(itemView.context, FinishActivity::class.java)
                        finishIntent.putExtra("selectedActivityId", activities[position].taskId)
                        progress?.text = "Finish"
                        progress?.setTextColor(itemView.resources.getColor(R.color.submit))
                        itemView.context.startActivity(finishIntent)
                        clickListener?.onItemFinishClick(position)
                    }
                }
            }
            startToggle?.setOnCheckedChangeListener { button, checked ->
                val position = adapterPosition
                val timedActivity : TimedActivityModel = activities[position] as TimedActivityModel
                if (checked) {
                    button.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.toggle_selected))
                    timedActivity.startActivity()
                    startTime?.text = timedActivity.getFormattedStartTime()
                }
                else {
                    button.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.toggle_unselected))
                    timedActivity.endActivity()
                    activities[position].isFinished = true
                    activities[position].streak += 1
                    DatabaseAPI.updateActivity(activities[position])
                    val finishIntent = Intent(itemView.context, FinishActivity::class.java)
                    finishIntent.putExtra("selectedActivityId", activities[position].taskId)
                    itemView.context.startActivity(finishIntent)
                    clickListener?.onItemFinishClick(position)
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
            itemLayout = R.layout.acitivity_item_view_only
        }
        else{
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
        if(holder.progress != null){
            if (activities[position].isFinished){
                holder.progress.text = "Finished"
                holder.progress.setTextColor(holder.itemView.resources.getColor(R.color.submit))
            }
            else{
                holder.progress.text = "Not Started"
                holder.progress.setTextColor(holder.itemView.resources.getColor(R.color.red))
            }
        }
        if(holder.remaining != null){
            val countActivity : CountableActivityModel = activities[position] as CountableActivityModel
            holder.remaining.text = countActivity.getRemaining().toString()
        }
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
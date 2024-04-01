package com.example.csci4176_pmgroupproject.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.csci4176_pmgroupproject.Database.DatabaseAPI
import com.example.csci4176_pmgroupproject.Model.ActivityEnergy
import com.example.csci4176_pmgroupproject.Model.ActivityMood
import com.example.csci4176_pmgroupproject.R
import kotlin.math.roundToInt

class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val onItemListener: OnItemListener,
    private val date: String,
    private val isNightMode: Boolean
): RecyclerView.Adapter<CalendarViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder
    {
        // Inflate the view if the calendar cell to fill the calendar
        val inflater = LayoutInflater.from(parent.context) as LayoutInflater
        val view = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams

        // Set each cell to be 13.25% of the calendar height to fit all cells
        layoutParams.height = (parent.height * 0.1325).toInt()

        return CalendarViewHolder(view, onItemListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int)
    {
        var day = daysOfMonth[position]
        holder.dayOfMonth.text = day
        if (isNightMode) {
            holder.dayOfMonth.setTextColor(Color.WHITE)
        }

        var queryDate: String

        // Format the string to query the DB
        if (day.isEmpty())
        { queryDate = day }

        else if (day.length == 1)
        { queryDate = date.substring(0, date.length-2) + "0" + day }

        else
        { queryDate = date.substring(0, date.length-2) + day  }

        // Update the circle icon for a given day
        updateCircleColour(holder, queryDate)
    }

    override fun getItemCount(): Int
    { return daysOfMonth.size }

    interface OnItemListener
    { fun onItemClick(position: Int, dayText: String) }

    // Method to change the circle color of a day based on average mood
    private fun updateCircleColour(holder: CalendarViewHolder, day: String)
    {
        // If the day is a empty cell make the circle transparent
        if (day == "")
        {
            return
        }

        // If the day is an actual number
        else
        {
            // Get the activities for that day
            DatabaseAPI.getDailyActivitiesOnDate(day) { dayActivity ->
                // If there were activities for the day
                if (dayActivity.size > 0) {
                    // Get the total mood based on enum ordinals and calculate the average
                    var sumMood = 0.0
                    var sumEnergy = 0.0
                    for (activity in dayActivity) {
                        sumMood += activity.mood.ordinal + 1
                        sumEnergy += activity.energy.ordinal + 1
                    }

                    val avgMood: Int = (sumMood / dayActivity.size - 1).roundToInt()
                    val avgEnergy = (sumEnergy /  dayActivity.size - 1).roundToInt()

                    when(ActivityMood.entries[avgMood]){
                        ActivityMood.DISAPPOINTED -> holder.mood.setImageResource(R.drawable.disappoint_icon_24)
                        ActivityMood.NEUTRAL -> holder.mood.setImageResource(R.drawable.neutral_icon_24)
                        ActivityMood.ACCOMPLISHED -> holder.mood.setImageResource(R.drawable.accomplish_icon_24)
                    }

                    when(ActivityEnergy.entries[avgEnergy]){
                        ActivityEnergy.TIRED -> holder.energy.setImageResource(R.drawable.tired_icon_24)
                        ActivityEnergy.NEUTRAL -> holder.energy.setImageResource(R.drawable.neutral_energy_icon_24)
                        ActivityEnergy.ENERGIZED -> holder.energy.setImageResource(R.drawable.energized_icon_24)
                    }
                }
            }
        }
    }
}

// View holder for the days of the month
class CalendarViewHolder(
    itemView: View, private val onItemListener: CalendarAdapter.OnItemListener
)
    : RecyclerView.ViewHolder(itemView), View.OnClickListener
{
    val dayOfMonth = itemView.findViewById(R.id.cellDayText) as TextView
    val mood = itemView.findViewById(R.id.moodCircle) as ImageView
    val energy = itemView.findViewById(R.id.energyCircle) as ImageView

    init
    { itemView.setOnClickListener(this) }

    // OnClick to do something when a day is selected
    override fun onClick(view: View?)
    {
        onItemListener.onItemClick(adapterPosition, dayOfMonth.text.toString())
    }

}

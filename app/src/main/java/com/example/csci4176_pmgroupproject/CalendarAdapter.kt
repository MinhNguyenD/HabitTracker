package com.example.csci4176_pmgroupproject

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val onItemListener: OnItemListener,
    private val date: String
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
        { holder.circle.drawable.setTint(Color.TRANSPARENT) }

        // If the day is an actual number
        else
        {
            // Get the activities for that day
            DatabaseAPI.getDailyActivitiesOnDate(day) { dayActivity ->
                // If there were activities for the day
                if (dayActivity.size > 0)
                {
                    // Get the total mood based on enum ordinals and calculate the average
                    var mood = 0.0
                    for (activity in dayActivity)
                    { mood += activity.mood.ordinal + 1 }

                    val avgMood: Int = (mood / dayActivity.size - 1).roundToInt()
                    System.out.println(avgMood)
                    // Based on the average mood set either red
                    if (avgMood < 1)
                    { holder.circle.drawable.setTint(Color.RED) }

                    // Orange (color.xml didn't work here so I used the string directly)
                    else if (avgMood < 3)
                    { holder.circle.drawable.setTint(Color.parseColor("#FFA500")) }

                    // Or green if the mood for everyday was "accomplished"
                    else
                    { holder.circle.drawable.setTint(Color.GREEN) }
                }

                // If there were no activities for the day set the circle to be transparent
                else
                { holder.circle.drawable.setTint(Color.TRANSPARENT) }
            }
        }
    }
}

// View holder for the days of the month
class CalendarViewHolder(
    itemView: View, private val onItemListener: CalendarAdapter.OnItemListener)
    : RecyclerView.ViewHolder(itemView), View.OnClickListener
{
    val dayOfMonth = itemView.findViewById(R.id.cellDayText) as TextView
    val circle = itemView.findViewById(R.id.moodCircle) as ImageView

    init
    { itemView.setOnClickListener(this) }

    // OnClick to do something when a day is selected
    override fun onClick(view: View?)
    {
        onItemListener.onItemClick(adapterPosition, dayOfMonth.text.toString())
    }

}

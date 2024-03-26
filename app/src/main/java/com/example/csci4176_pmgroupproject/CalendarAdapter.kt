package com.example.csci4176_pmgroupproject

import android.graphics.Color
import android.graphics.ColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val onItemListener: OnItemListener
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
        holder.dayOfMonth.text = daysOfMonth[position]
        updateCircleColour(holder, daysOfMonth[position])
    }

    override fun getItemCount(): Int
    { return daysOfMonth.size }

    interface OnItemListener
    { fun onItemClick(position: Int, dayText: String) }

    private fun updateCircleColour(holder: CalendarViewHolder, day: String)
    {
        if (day == "")
        { holder.circle.drawable.setTint(Color.TRANSPARENT) }

        else
        {
            DatabaseAPI.getDailyActivitiesOnDate(day) { dayActivity ->
                if (dayActivity.size > 0)
                {
                    var mood = 0.0
                    var energy = 0.0
                    for (activity in dayActivity)
                    {
                        mood += activity.mood.ordinal + 1 // Avoid 0 as a sum
                        energy += activity.energy.ordinal + 1
                    }

                    val avgMood: Int = (mood / dayActivity.size - 1).roundToInt()
                    val avgEnergy = (energy / dayActivity.size - 1).roundToInt()
                }

                else
                { holder.circle.drawable.setTint(Color.GREEN) }
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

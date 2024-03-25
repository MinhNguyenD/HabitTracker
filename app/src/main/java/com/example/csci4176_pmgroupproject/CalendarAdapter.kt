package com.example.csci4176_pmgroupproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
    { holder.dayOfMonth.text = daysOfMonth[position] }

    override fun getItemCount(): Int
    { return daysOfMonth.size }

    interface OnItemListener
    { fun onItemClick(position: Int, dayText: String) }

}

// View holder for the days of the month
class CalendarViewHolder(
    itemView: View, private val onItemListener: CalendarAdapter.OnItemListener)
    : RecyclerView.ViewHolder(itemView), View.OnClickListener
{
    val dayOfMonth = itemView.findViewById(R.id.cellDayText) as TextView

    init
    { itemView.setOnClickListener(this) }

    // OnClick to do something when a day is selected
    override fun onClick(view: View?)
    {
        onItemListener.onItemClick(adapterPosition, dayOfMonth.text.toString())
    }

}

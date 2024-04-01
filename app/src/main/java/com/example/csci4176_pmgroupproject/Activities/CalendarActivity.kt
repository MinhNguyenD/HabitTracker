package com.example.csci4176_pmgroupproject.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.csci4176_pmgroupproject.Adapters.CalendarAdapter
import com.example.csci4176_pmgroupproject.R
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarActivity : BaseActivity(), CalendarAdapter.OnItemListener
{
    // Variables to be used throughout the class
    lateinit var monthYearText: TextView
    lateinit var calendarRecyclerView: RecyclerView
    var referenceDate = LocalDate.now() as LocalDate
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // Get the recycler view and the title to display month and year
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        monthYearText = findViewById(R.id.monthYearTV)
        sharedPref = getSharedPreferences("Mode", Context.MODE_PRIVATE)

        // Set the calendar with values
        setMonthView()
    }

    // Method to set the current month view
    private fun setMonthView()
    {
        // Gets the amount of days in the current month
        monthYearText.text = monthYearFromDate(referenceDate)
        val daysInMonth = daysInMonthArray(referenceDate)
        val isNightMode = sharedPref.getBoolean("night",false)

        // Set adapter with those days to display on the calendar with a grid layout
        val calendarAdapter = CalendarAdapter(daysInMonth, this, referenceDate.toString(), isNightMode)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = calendarAdapter
    }

    // Helper method to generate an array containing the days in a given month
    private fun daysInMonthArray(date: LocalDate): ArrayList<String>
    {
        // Initialize the array to return and use the given date to get the YearMonth date-time object
        var daysInMonthArray = ArrayList<String>()
        val yearMonth = YearMonth.from(date)

        // Get the info of the given month to help with displaying it properly
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = referenceDate.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value

        // Loop through the total grid size of a calendar to set elements appropriately (6x7 = 42)
        for (i in 1..42)
        {
            // If the day shouldn't be displayed add a blank space
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            { daysInMonthArray.add("") }

            // If it should be added format it appropriately and add it to the array
            else
            { daysInMonthArray.add((i - dayOfWeek).toString()) }
        }

        // Take the first 7 elements of the array and check to see if they're all empty
        var first7 = daysInMonthArray.take(7)
        if (first7.all { it == ""})
        {
            // If so swap them to the end of the array so the calendar starts in the top row
            daysInMonthArray = daysInMonthArray.drop(7) as ArrayList<String>

            repeat(7)
            { daysInMonthArray.add("") }
        }

        // Return the array containing the days in the month properly padded
        return daysInMonthArray
    }

    // Formats the month and year given a date-time object
    private fun monthYearFromDate(date: LocalDate): String
    {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    // Switches view to the previous month
    fun previousMonthAction(view: View)
    {
        referenceDate = referenceDate.minusMonths(1)
        setMonthView()
    }

    // Switches the view to the next month
    fun nextMonthAction(view: View)
    {
        referenceDate = referenceDate.plusMonths(1)
        setMonthView()
    }

    // Activates the onclick when a day is selected from the calendar
    override fun onItemClick(position: Int, dayText: String) {
        if (dayText != "") {
            var day = dayText
            if(dayText.length == 1){
                day = "0" + dayText
            }
            val selectedDate = "${referenceDate.toString().substringBeforeLast('-')}-$day"
            val dailyActivityIntent = Intent(this, OnDateActivity::class.java)
            dailyActivityIntent.putExtra("selectedDate",selectedDate)
            startActivity(dailyActivityIntent)
        }
    }
}

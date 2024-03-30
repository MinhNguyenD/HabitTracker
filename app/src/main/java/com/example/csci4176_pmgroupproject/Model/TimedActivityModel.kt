package com.example.csci4176_pmgroupproject.Model

import com.example.csci4176_pmgroupproject.ActivityEnergy
import com.example.csci4176_pmgroupproject.ActivityModelDayOfWeek
import com.example.csci4176_pmgroupproject.ActivityModelEnums
import com.example.csci4176_pmgroupproject.ActivityModelFrequency
import com.example.csci4176_pmgroupproject.ActivityMood
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.util.Date
import java.util.TimeZone

class TimedActivityModel(habitId : String, title: String,
                         frequency: ActivityModelFrequency, days: ArrayList<DayOfWeek>)
    : ActivityModel(habitId,title,
    ActivityModelEnums.TIMED,false, LocalDate.now().toString(), frequency, days,
    0,
    ActivityMood.NEUTRAL,
    ActivityEnergy.NEUTRAL, ""){
    private var startTime: Long = 0
    private var endTime : Long = 0
    private var duration : Long = 0
    private var formattedStartTime : String = ""
    private var formattedEndTime : String = ""
    private var formattedDuration : String = ""


    // require by firebase
    constructor() : this("","", ActivityModelFrequency.NEVER, arrayListOf())

    fun startActivity(){
        startTime =  System.currentTimeMillis()
    }

    /*
     * This will return the total time the user spent on
     * this task/activity.
     * @return: [Current Time] - [Start Time]
     */
    fun endActivity(){
        endTime = System.currentTimeMillis()
        duration =  endTime - startTime
    }

    fun getFormattedStartTime() : String {
        formattedStartTime = formatTime(startTime)
        return formattedStartTime
    }

    fun formatTime(time : Long) : String{
        val formatter = SimpleDateFormat("HH:mm:ss")
        if(time == 0.toLong()){
            return "00:00:00"
        }
        val formattedTime = formatter.format(Date(time))
        return formattedTime
    }

    fun getFormattedEndTime() : String {
        formattedEndTime = formatTime(endTime)
        return formattedEndTime
    }

    fun getFormattedDuration() : String {
        val hours = duration / (1000 * 60 * 60)
        val minutes = (duration % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = ((duration % (1000 * 60 * 60)) % (1000 * 60)) / 1000
        formattedDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        return formattedDuration
    }
    override fun complete() {
        TODO("Not yet implemented")
    }

    override fun reset() {
        TODO("Not yet implemented")
    }
}
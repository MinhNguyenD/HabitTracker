package com.example.csci4176_pmgroupproject.Model

import com.example.csci4176_pmgroupproject.ActivityEnergy
import com.example.csci4176_pmgroupproject.ActivityModelDayOfWeek
import com.example.csci4176_pmgroupproject.ActivityModelEnums
import com.example.csci4176_pmgroupproject.ActivityModelFrequency
import com.example.csci4176_pmgroupproject.ActivityMood
import java.time.LocalDate
import java.util.Date

class TimedActivityModel(taskId: String, userId: String,
                         habitId : String, title: String,
                         frequency: ActivityModelFrequency, dayOfWeek: ActivityModelDayOfWeek, var startTime: Long)
    : ActivityModel(taskId, userId, habitId,title,
    ActivityModelEnums.TIMED,false, LocalDate.now().toString(), frequency,dayOfWeek,
    0,
    ActivityMood.NEUTRAL,
    ActivityEnergy.NEUTRAL, ""){
    // require by firebase
    constructor() : this("","","","", ActivityModelFrequency.NEVER, ActivityModelDayOfWeek.MONDAY,0)
    constructor(habitId : String, title: String, frequency: ActivityModelFrequency, dayOfWeek: ActivityModelDayOfWeek, startTime: Long) : this("","",habitId, title, frequency, dayOfWeek, startTime)
    /*
     * This will return the total time the user spent on
     * this task/activity.
     * @return: [Current Time] - [Start Time]
     */
    fun endActivity(): Long{
        return Date().time - startTime
    }

    override fun complete() {
        TODO("Not yet implemented")
    }

    override fun reset() {
        TODO("Not yet implemented")
    }
}
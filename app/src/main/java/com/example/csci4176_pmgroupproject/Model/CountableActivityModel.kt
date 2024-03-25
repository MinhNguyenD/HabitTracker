package com.example.csci4176_pmgroupproject.Model

import com.example.csci4176_pmgroupproject.ActivityEnergy
import com.example.csci4176_pmgroupproject.ActivityModelDayOfWeek
import com.example.csci4176_pmgroupproject.ActivityModelEnums
import com.example.csci4176_pmgroupproject.ActivityModelFrequency
import com.example.csci4176_pmgroupproject.ActivityMood
import java.time.LocalDate


class CountableActivityModel(taskId: String, userId: String,
                             habitId : String, title: String,
                             frequency: ActivityModelFrequency, dayOfWeek: ActivityModelDayOfWeek, var remaining:Int = 0)
    : ActivityModel(taskId, userId, habitId,title,
    ActivityModelEnums.COUNTABLE, false, LocalDate.now().toString(), frequency, dayOfWeek,
    0,
    ActivityMood.NEUTRAL,
    ActivityEnergy.NEUTRAL, ""){
    // require by firebase
    constructor() : this("","","","", ActivityModelFrequency.NEVER, ActivityModelDayOfWeek.MONDAY,0)
    constructor(userId: String, habitId : String, title: String, frequency: ActivityModelFrequency, dayOfWeek: ActivityModelDayOfWeek, remaining: Int) : this("",userId,habitId, title, frequency, dayOfWeek, remaining)
    //    fun setRemaining(remaining: Int){ this.remaining = remaining }
    fun decrementRemaining(){ this.remaining-- }
}
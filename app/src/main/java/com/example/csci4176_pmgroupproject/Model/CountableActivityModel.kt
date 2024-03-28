package com.example.csci4176_pmgroupproject.Model

import com.example.csci4176_pmgroupproject.ActivityEnergy
import com.example.csci4176_pmgroupproject.ActivityModelDayOfWeek
import com.example.csci4176_pmgroupproject.ActivityModelEnums
import com.example.csci4176_pmgroupproject.ActivityModelFrequency
import com.example.csci4176_pmgroupproject.ActivityMood
import java.time.LocalDate


class CountableActivityModel(habitId : String, title: String,
                             frequency: ActivityModelFrequency, dayOfWeek: ActivityModelDayOfWeek, var remaining:Int = 0)
    : ActivityModel(habitId,title,
    ActivityModelEnums.COUNTABLE, false, LocalDate.now().toString(), frequency, dayOfWeek,
    0,
    ActivityMood.NEUTRAL,
    ActivityEnergy.NEUTRAL, ""){
    // require by firebase
    constructor() : this("","", ActivityModelFrequency.NEVER, ActivityModelDayOfWeek.MONDAY,0)

    //    fun setRemaining(remaining: Int){ this.remaining = remaining }
    fun decrementRemaining(){ this.remaining-- }
    override fun complete() {
        TODO("Not yet implemented")
    }

    override fun reset() {
        TODO("Not yet implemented")
    }
}
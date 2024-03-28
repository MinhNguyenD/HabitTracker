package com.example.csci4176_pmgroupproject.Model

import com.example.csci4176_pmgroupproject.ActivityEnergy
import com.example.csci4176_pmgroupproject.ActivityModelDayOfWeek
import com.example.csci4176_pmgroupproject.ActivityModelEnums
import com.example.csci4176_pmgroupproject.ActivityModelFrequency
import com.example.csci4176_pmgroupproject.ActivityMood
import java.time.LocalDate

class CheckedActivityModel(habitId : String, title: String, frequency: ActivityModelFrequency, dayOfWeek: ActivityModelDayOfWeek) : ActivityModel(habitId, title,
    ActivityModelEnums.CHECKED, false, LocalDate.now().toString(), frequency,dayOfWeek,
    0,
    ActivityMood.NEUTRAL,
    ActivityEnergy.NEUTRAL, ""){
    // require by firebase
    constructor() : this("","", ActivityModelFrequency.NEVER, ActivityModelDayOfWeek.MONDAY)
    override fun complete() {
        isFinished = true
    }

    override fun reset() {
        isFinished = false
        TODO("Not yet implemented")
    }
}
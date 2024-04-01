package com.example.csci4176_pmgroupproject.Model

import com.example.csci4176_pmgroupproject.ActivityEnergy
import com.example.csci4176_pmgroupproject.ActivityModelDayOfWeek
import com.example.csci4176_pmgroupproject.ActivityModelEnums
import com.example.csci4176_pmgroupproject.ActivityModelFrequency
import com.example.csci4176_pmgroupproject.ActivityMood
import java.time.DayOfWeek
import java.time.LocalDate


class CountableActivityModel(habitId : String, title: String,
                             frequency: ActivityModelFrequency, days: ArrayList<DayOfWeek>, private var remaining:Int = 0)
    : ActivityModel(habitId,title,
    ActivityModelEnums.COUNTABLE, false, LocalDate.now().toString(), frequency, days,
    0,
    ActivityMood.NEUTRAL,
    ActivityEnergy.NEUTRAL, ""){
    // require by firebase
    constructor() : this("","", ActivityModelFrequency.NEVER, arrayListOf(),0)

    fun setRemaining(value: Int){ this.remaining = value }
    fun decrementRemaining(){
        if(remaining > 0){
            this.remaining--
        }
    }

    fun getRemaining(): Int{ return this.remaining }
    override fun complete() {
        TODO("Not yet implemented")
    }

    override fun reset() {
        TODO("Not yet implemented")
    }
}
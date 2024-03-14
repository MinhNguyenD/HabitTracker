package com.example.csci4176_pmgroupproject

import com.google.android.gms.tasks.Task
import java.util.Date

/*ActivityModel Redesigned*/
open class TaskModel (val title: String, val type: ActivityModelEnums,
                      var streak: Int, var mood: ActivityMood,
                      var energy: ActivityEnergy, var note: String){
    fun updateStreak(){ streak++; }

    fun breakStreak(){ streak = 0 }

    fun updateMood(mood: ActivityMood){ this.mood = mood }
    fun updateEnergy(energy: ActivityEnergy){ this.energy = energy }

    fun clearNote(){ note = "" }
    fun makeNote(note: String){ this.note = note }

    fun appendNote(note: String, tag: String){ this.note += "\n$tag $note"}
}

class CheckedTaskModel(title: String)
    : TaskModel(title, ActivityModelEnums.CHECKED,
    0, ActivityMood.NEUTRAL, ActivityEnergy.NEUTRAL, ""){
}

class TimedTaskModel(title: String, var startTime: Long)
    : TaskModel(title, ActivityModelEnums.TIMED,
    0, ActivityMood.NEUTRAL, ActivityEnergy.NEUTRAL, ""){
        /*
         * This will return the total time the user spent on
         * this task/activity.
         * @return: [Current Time] - [Start Time]
         */
        fun endActivity(): Long{
            return Date().time - startTime
        }
}

class CountableTaskModel(title: String, var remaining:Int = 0)
    : TaskModel(title, ActivityModelEnums.COUNTABLE,
    0, ActivityMood.NEUTRAL, ActivityEnergy.NEUTRAL, ""){
    fun setRemaining(remaining: Int){ this.remaining = remaining }
    fun decrementRemaining(){ this.remaining-- }
}


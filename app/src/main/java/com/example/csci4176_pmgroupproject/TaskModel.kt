package com.example.csci4176_pmgroupproject

import com.google.android.gms.tasks.Task
import java.util.Date

/*ActivityModel Redesigned*/
open class TaskModel (var taskId : String, val userId : String, val habitId : String, var title: String, val type: ActivityModelEnums,
                      var frequency : ActivityModelFrequency, var dayOfWeek: ActivityModelDayOfWeek,
                      var streak: Int, var mood: ActivityMood,
                      var energy: ActivityEnergy, var note: String){
    constructor(userId : String,  habitId : String,  title: String,  type: ActivityModelEnums,
                frequency: ActivityModelFrequency, dayOfWeek: ActivityModelDayOfWeek,
                 streak: Int,  mood: ActivityMood,
                 energy: ActivityEnergy,  note: String) : this("", userId,habitId, title, type, frequency, dayOfWeek, streak,  mood, energy,  note)
    fun updateStreak(){ streak++; }

    fun breakStreak(){ streak = 0 }

    fun updateMood(mood: ActivityMood){ this.mood = mood }
    fun updateEnergy(energy: ActivityEnergy){ this.energy = energy }

    fun clearNote(){ note = "" }
    fun makeNote(note: String){ this.note = note }

    fun appendNote(note: String, tag: String){ this.note += "\n$tag $note"}
}

class CheckedTaskModel(taskId : String, userId: String, habitId : String, title: String, frequency: ActivityModelFrequency, dayOfWeek: ActivityModelDayOfWeek)
    : TaskModel(taskId, userId, habitId, title, ActivityModelEnums.CHECKED, frequency,dayOfWeek,
    0, ActivityMood.NEUTRAL, ActivityEnergy.NEUTRAL, ""){
        // require by firebase
        constructor() : this("","","","", ActivityModelFrequency.NEVER, ActivityModelDayOfWeek.MONDAY)
        constructor(userId: String, habitId : String, title: String, frequency: ActivityModelFrequency, dayOfWeek: ActivityModelDayOfWeek) : this("",userId,habitId, title, frequency,dayOfWeek)
}

class TimedTaskModel(taskId: String, userId: String, habitId : String, title: String, frequency: ActivityModelFrequency, dayOfWeek: ActivityModelDayOfWeek, var startTime: Long)
    : TaskModel(taskId, userId, habitId,title, ActivityModelEnums.TIMED, frequency,dayOfWeek,
    0, ActivityMood.NEUTRAL, ActivityEnergy.NEUTRAL, ""){
        // require by firebase
        constructor() : this("","","","", ActivityModelFrequency.NEVER, ActivityModelDayOfWeek.MONDAY,0)
    constructor(userId: String, habitId : String, title: String, frequency: ActivityModelFrequency, dayOfWeek: ActivityModelDayOfWeek, startTime: Long) : this("",userId,habitId, title, frequency, dayOfWeek, startTime)
    /*
     * This will return the total time the user spent on
     * this task/activity.
     * @return: [Current Time] - [Start Time]
     */
    fun endActivity(): Long{
        return Date().time - startTime
    }
}

class CountableTaskModel(taskId: String, userId: String, habitId : String, title: String, frequency: ActivityModelFrequency, dayOfWeek: ActivityModelDayOfWeek, var remaining:Int = 0)
    : TaskModel(taskId, userId, habitId,title, ActivityModelEnums.COUNTABLE, frequency, dayOfWeek,
    0, ActivityMood.NEUTRAL, ActivityEnergy.NEUTRAL, ""){
    // require by firebase
    constructor() : this("","","","", ActivityModelFrequency.NEVER, ActivityModelDayOfWeek.MONDAY,0)
    constructor(userId: String, habitId : String, title: String,frequency: ActivityModelFrequency, dayOfWeek: ActivityModelDayOfWeek, remaining: Int) : this("",userId,habitId, title,frequency, dayOfWeek, remaining)
    //    fun setRemaining(remaining: Int){ this.remaining = remaining }
    fun decrementRemaining(){ this.remaining-- }
}


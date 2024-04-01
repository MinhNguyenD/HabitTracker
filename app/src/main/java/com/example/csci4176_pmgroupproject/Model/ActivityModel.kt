package com.example.csci4176_pmgroupproject.Model

import com.example.csci4176_pmgroupproject.Database.DatabaseAPI
import java.time.DayOfWeek

abstract class ActivityModel (var taskId : String, val userId : String, val habitId : String,
                              var title: String, val type: ActivityModelEnums,
                              var isFinished : Boolean, val startDate : String,
                              var frequency : ActivityModelFrequency, var days: ArrayList<DayOfWeek>,
                              var streak: Int, var mood: ActivityMood,
                              var energy: ActivityEnergy, var note: String){

    constructor(habitId : String, title: String, type: ActivityModelEnums,
                isFinished : Boolean, startDate : String,
                frequency: ActivityModelFrequency, days: ArrayList<DayOfWeek>,
                streak: Int, mood: ActivityMood,
                energy: ActivityEnergy, note: String) : this("", DatabaseAPI.currentUser.uid,habitId, title, type, isFinished, startDate, frequency, days, streak,  mood, energy,  note)

    constructor() : this("","",
        ActivityModelEnums.CHECKED,false, "", ActivityModelFrequency.NEVER, arrayListOf(), 0, ActivityMood.NEUTRAL, ActivityEnergy.NEUTRAL, "")

    fun updateStreak(){ streak++; }

    fun breakStreak(){ streak = 0 }

    fun updateMood(mood: ActivityMood){ this.mood = mood }
    fun updateEnergy(energy: ActivityEnergy){ this.energy = energy }

    fun clearNote(){ note = "" }
    fun makeNote(note: String){ this.note = note }

    fun appendNote(note: String, tag: String){ this.note += "\n$tag $note"}

    abstract fun complete()

    abstract fun reset()

}
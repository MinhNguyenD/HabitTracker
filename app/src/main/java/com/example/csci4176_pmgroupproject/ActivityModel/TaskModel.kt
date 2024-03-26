package com.example.csci4176_pmgroupproject.ActivityModel

import java.util.Date

/*ActivityModel Redesigned*/
abstract class TaskModel (val title: String, val type: ActivityModelEnums,
                          var days: Array<ActivityModelDays>, var frequency: ActivityModelRepeat)
{
    private var streak: Int = 0
    private lateinit var mood: ActivityMood
    private lateinit var energy: ActivityEnergy
    private var note: String = ""
    fun updateStreak(){ streak++; }

    fun breakStreak(){ streak = 0 }

    fun updateMood(mood: ActivityMood){ this.mood = mood }
    fun updateEnergy(energy: ActivityEnergy){ this.energy = energy }

    fun clearNote(){ note = "" }
    fun makeNote(note: String){ this.note = note }

    fun appendNote(note: String, tag: String){ this.note += "\n$tag $note"}
    /* Must be implemented by children */

    fun getNote(): String{ return this.note }

    abstract fun complete();
}

class CheckedTaskModel(title: String, days: Array<ActivityModelDays>, frequency: ActivityModelRepeat)
    : TaskModel(title, ActivityModelEnums.CHECKED, days, frequency){
    override fun complete() {
        TODO("Not yet implemented")
    }
}

class TimedTaskModel(title: String, days: Array<ActivityModelDays>, frequency: ActivityModelRepeat)
    : TaskModel(title, ActivityModelEnums.TIMED, days, frequency){
        private var startTime:Long = 0;

        fun startActivity(){
            this.startTime = Date().time
        }
        private fun endActivity(): Long{
            return Date().time - startTime
        }

    override fun complete() {
        TODO("Not yet implemented")
    }
}

class CountableTaskModel(title: String, days: Array<ActivityModelDays>, frequency: ActivityModelRepeat)
    : TaskModel(title, ActivityModelEnums.COUNTABLE, days, frequency){
    private var remaining: Int = 0;
    fun getRemaining(): Int { return this.remaining }

    fun setRemaining(value: Int = 0){ this.remaining = value }
    fun decrementRemaining(){ this.remaining-- }
    override fun complete() {
        TODO("Not yet implemented")
    }
}


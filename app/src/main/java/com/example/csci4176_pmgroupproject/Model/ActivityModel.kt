package com.example.csci4176_pmgroupproject.Model

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.example.csci4176_pmgroupproject.ActivityEnergy
import com.example.csci4176_pmgroupproject.ActivityModelEnums
import com.example.csci4176_pmgroupproject.ActivityModelFrequency
import com.example.csci4176_pmgroupproject.ActivityMood
import com.example.csci4176_pmgroupproject.DatabaseAPI
import java.time.DayOfWeek

abstract class ActivityModel (var taskId : String, val userId : String, val habitId : String,
                              var title: String, val type: ActivityModelEnums,
                              var isFinished : Boolean, val startDate : String,
                              var frequency : ActivityModelFrequency, var days: ArrayList<DayOfWeek>,
                              var streak: Int, var mood: ActivityMood,
                              var energy: ActivityEnergy, var note: String) : Parcelable {

    constructor(habitId : String, title: String, type: ActivityModelEnums,
                isFinished : Boolean, startDate : String,
                frequency: ActivityModelFrequency, days: ArrayList<DayOfWeek>,
                streak: Int, mood: ActivityMood,
                energy: ActivityEnergy, note: String) : this("", DatabaseAPI.currentUser.uid,habitId, title, type, isFinished, startDate, frequency, days, streak,  mood, energy,  note)

    constructor() : this("","",ActivityModelEnums.CHECKED,false, "", ActivityModelFrequency.NEVER, arrayListOf(), 0, ActivityMood.NEUTRAL, ActivityEnergy.NEUTRAL, "")

    fun updateStreak(){ streak++; }

    fun breakStreak(){ streak = 0 }

    fun updateMood(mood: ActivityMood){ this.mood = mood }
    fun updateEnergy(energy: ActivityEnergy){ this.energy = energy }

    fun clearNote(){ note = "" }
    fun makeNote(note: String){ this.note = note }

    fun appendNote(note: String, tag: String){ this.note += "\n$tag $note"}

    abstract fun complete()

    abstract fun reset()
    override fun describeContents(): Int { return 0 }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeString(taskId)
        p0.writeString(userId)
        p0.writeString(habitId)
        p0.writeString(title)
        p0.writeInt(type.ordinal)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            p0.writeBoolean(isFinished)
        }else {
            p0.writeByte(if (isFinished) 1 else 0)
        }
        p0.writeString(startDate)
        p0.writeInt(frequency.ordinal)
        p0.writeList(days)
        p0.writeInt(streak)
        p0.writeInt(mood.ordinal)
        p0.writeInt(energy.ordinal)
        p0.writeString(note)
    }
}
package com.example.csci4176_pmgroupproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class EndOfDayReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Check the activities at the end of the day
        // Reset streak for any undone activities
        // You can implement your logic here
        if (context != null) {
            resetStreak(context)
        }
    }

    private fun resetStreak(context: Context?){
        DatabaseAPI.getDailyActivity { dailyList ->
            for(activity in dailyList){
                if(!activity.isFinished){
                    activity.breakStreak()
                }
                else{
                    activity.isFinished = false
                }
                DatabaseAPI.updateActivity(activity)
            }
        }
    }
}

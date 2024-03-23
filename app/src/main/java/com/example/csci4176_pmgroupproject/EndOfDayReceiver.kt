package com.example.csci4176_pmgroupproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * BroadcastReceiver responsible for receiving the end-of-day alarm signal and performing actions accordingly.
 */
class EndOfDayReceiver : BroadcastReceiver() {

    /**
     * Called when the broadcast is received.
     * @param context: The context in which the receiver is running.
     * @param intent: The Intent being received.
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        // Check the activities at the end of the day
        // Reset streak for any undone activities
        // You can implement your logic here
        if (context != null) {
            resetStreak(context)
        }
    }

    /**
     * Resets streaks for activities that were not completed at the end of the day.
     * @param context: The context in which the receiver is running.
     */
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
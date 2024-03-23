package com.example.csci4176_pmgroupproject


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

/**
 * Singleton object responsible for scheduling alarms for various tasks.
 */
object AlarmScheduler {

    /**
     * Schedules an alarm to trigger at the end of the day to perform a specific task.
     * @param context: The context from which the alarm is scheduled.
     */
    fun scheduleEndOfDayCheck(context: Context) {
        // Obtain the system's AlarmManager service
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Create a PendingIntent to be fired when the alarm is triggered
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, EndOfDayReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        // Set the time for the alarm to trigger (end of the day)
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.SECOND] = 59
        calendar[Calendar.MILLISECOND] = 0

        // Schedule the alarm to trigger at the specified time
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}
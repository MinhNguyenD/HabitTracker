package com.example.csci4176_pmgroupproject

import android.content.Context
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csci4176_pmgroupproject.Model.ActivityModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt

/**
 * HomeActivity: Displays the home screen with a list of daily activities and progress bar.
 */
class HomeActivity : BaseActivity(), TodoItemClickListener {
    private lateinit var progressBarView : ProgressBar
    private lateinit var progressTextView : TextView
    private lateinit var todayTextView : TextView
    private lateinit var dailyActivityView : RecyclerView
    private lateinit var activityAdapter : DailyActivityAdapter
    private lateinit var dailyActivityList : ArrayList<ActivityModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        DatabaseAPI.currentUser = FirebaseAuth.getInstance().currentUser!!
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        dailyActivityList = ArrayList()
        todayTextView = findViewById(R.id.todayText)
        progressBarView = findViewById(R.id.progressBar)
        progressTextView = findViewById(R.id.progressText)
        dailyActivityView = findViewById(R.id.dailyActivityList)
        dailyActivityView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // populate daily activities list and display on recycler view
        DatabaseAPI.getDailyActivity { dailyList ->
            if(dailyList.size <= 0){
                displayNoActivity()
            }
            else{
                dailyActivityList = dailyList
                activityAdapter =  DailyActivityAdapter(dailyActivityList, this, false)
                dailyActivityView.adapter = activityAdapter
                DailyProgress.currentNumActivities = DailyProgress.numActivities
                initToday()
                if(DailyProgress.progress == 0){
                    DailyProgress.numActivities = activityAdapter.itemCount
                    initProgress()
                }
                else{
                    DailyProgress.numActivities = dailyActivityList.size + DailyProgress.numFinishedActivity.toInt()
                }
                updateProgress()
            }
        }
        AlarmScheduler.scheduleEndOfDayCheck(this)
    }

    /**
     * Updates the progress bar.
     */
    private fun updateProgress(){
        if(DailyProgress.numActivities > 0){
            DailyProgress.progress = (DailyProgress.numFinishedActivity/DailyProgress.numActivities.toDouble() * 100).roundToInt()
        }
        else{
            DailyProgress.progress = 100
        }
        // since we rounding up percentage, the sum will be more than 100
        if(DailyProgress.progress > 100){
            DailyProgress.progress = 100
        }
        updateProgressView()
    }

    /**
     * Updates the progress bar view.
     */
    private fun updateProgressView(){
        progressBarView.progress = DailyProgress.progress
        progressTextView.text = "${DailyProgress.progress}%"
    }

    /**
     * Initializes the progress bar.
     */
    private fun initProgress(){
        DailyProgress.progress = 0
        DailyProgress.numFinishedActivity = 0
        updateProgressView()
    }

    /**
     * Initializes the text view for today's date.
     */
    private fun initToday(){
        val dateFormat =  SimpleDateFormat("MMM d, y")
        todayTextView.text = dateFormat.format(Date())
    }

    /**
     * Handles the click event when a daily activity is marked as finished.
     * @param position: The position of the clicked item in the RecyclerView.
     */
    override fun onItemFinishClick(position: Int) {
        DailyProgress.currentNumActivities--
        DailyProgress.numFinishedActivity++
        dailyActivityList.removeAt(position)
        // Update the RecyclerView
        dailyActivityView.adapter?.notifyItemRemoved(position)
        updateProgress()
        displayNoActivity()
    }

    /**
     * Displays a message if there are no daily activities left.
     */
    private fun displayNoActivity(){
        val noActivityView: TextView = findViewById(R.id.noActivity)
        if(dailyActivityList.isEmpty()) {
            DailyProgress.progress = 100
            updateProgressView()
            noActivityView.text = "All activities are done!"
        }
        else{
            noActivityView.text = ""
        }
    }
}

class DailyProgress {
    companion object {
        var progress : Int = 0
        var numActivities : Int = 0
        var currentNumActivities : Int = 0
        var numFinishedActivity : Int = 0
    }
}
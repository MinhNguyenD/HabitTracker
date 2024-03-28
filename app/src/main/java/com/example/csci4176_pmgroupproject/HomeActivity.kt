package com.example.csci4176_pmgroupproject

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csci4176_pmgroupproject.Model.ActivityModel
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt

/**
 * HomeActivity: Displays the home screen with a list of daily activities and progress bar.
 */
class HomeActivity : BaseActivity(), TodoItemClickListener {
    private var progress = 0
    private var numActivities = 0
    private var currentNumActivities = 0
    private var numFinishedActivity = 0.0
    private lateinit var progressBarView : ProgressBar
    private lateinit var progressTextView : TextView
    private lateinit var todayTextView : TextView
    private lateinit var dailyActivityView : RecyclerView
    private lateinit var activityAdapter : DailyActivityAdapter
    private lateinit var dailyActivityList : ArrayList<ActivityModel>
    override fun onCreate(savedInstanceState: Bundle?) {
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
            dailyActivityList = dailyList
            activityAdapter =  DailyActivityAdapter(dailyActivityList, this)
            dailyActivityView.adapter = activityAdapter
            currentNumActivities = numActivities
            initToday()
            if(progress == 0){
                numActivities = activityAdapter.itemCount
                initProgress()
            }
            else{
                if(currentNumActivities < dailyActivityList.size){
                    numActivities = dailyActivityList.size + numFinishedActivity.toInt()
                }
            }
            updateProgress()
            displayNoActivity()
        }
        DatabaseAPI.saveDailyActivities(dailyActivityList)
        AlarmScheduler.scheduleEndOfDayCheck(this)
    }

    override fun onResume() {
        super.onResume()
        // populate daily activities list and display on recycler view
        DatabaseAPI.getDailyActivity { dailyList ->
            dailyActivityList = dailyList
            activityAdapter =  DailyActivityAdapter(dailyActivityList, this)
            dailyActivityView.adapter = activityAdapter
            // if new activities added
            if(currentNumActivities < dailyActivityList.size){
                numActivities = dailyActivityList.size + numFinishedActivity.toInt()
            }
            updateProgress()
            displayNoActivity()
        }
    }

    /**
     * Updates the progress bar.
     */
    private fun updateProgress(){
        if(numActivities > 0){
            progress = (numFinishedActivity/numActivities * 100).roundToInt()
        }
        else{
            progress = 100
        }
        // since we rounding up percentage, the sum will be more than 100
        if(progress > 100){
            progress = 100
        }
        updateProgressView()
    }

    /**
     * Updates the progress bar view.
     */
    private fun updateProgressView(){
        progressBarView.progress = progress
        progressTextView.text = "$progress%"
    }

    /**
     * Initializes the progress bar.
     */
    private fun initProgress(){
        progress = 0
        numFinishedActivity = 0.0
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
        currentNumActivities--
        numFinishedActivity++
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
        if(dailyActivityList.isEmpty()) {
            progress = 100
            updateProgressView()
            val noActivityView: TextView = findViewById(R.id.noActivity)
            noActivityView.text = "All activities are done!"
        }
    }
}
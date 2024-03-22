package com.example.csci4176_pmgroupproject

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csci4176_pmgroupproject.Model.ActivityModel
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.ceil

class HomeActivity : AppCompatActivity(), TodoItemClickListener {
    private var progress = 0
    private var numActivities = 0
    private var percentageIncrease = 0
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
        val navbarFragment = NavigationBar()

        // Add navbar fragment to the activity
        supportFragmentManager.beginTransaction()
            .add(R.id.navbarFrame, navbarFragment)
            .commit()

        // populate daily activities list and display on recycler view
        DatabaseAPI.getDailyActivity { dailyList ->
            dailyActivityList = dailyList
            activityAdapter =  DailyActivityAdapter(dailyActivityList, this)
            dailyActivityView.adapter = activityAdapter
            numActivities = activityAdapter.itemCount
            initToday()
            initProgress()
            displayNoActivity()
        }
        DatabaseAPI.saveDailyActivities(dailyActivityList)
        AlarmScheduler.scheduleEndOfDayCheck(this)
    }

    private fun updateProgress(){
        if(progress < 100){
            progress += percentageIncrease
        }
        // since we rounding up percentage, the sum will be more than 100
        if(progress > 100){
            progress = 100
        }
        updateProgressView()
    }
    private fun updateProgressView(){
        progressBarView.progress = progress
        progressTextView.text = "$progress%"
    }

    private fun initProgress(){
        percentageIncrease = ceil(1.0/numActivities * 100).toInt()
        updateProgressView()
    }

    private fun initToday(){
        val dateFormat =  SimpleDateFormat("MMM d, y")
        todayTextView.text = dateFormat.format(Date())
    }

    override fun onItemFinishClick(position: Int) {
        dailyActivityList.removeAt(position)
        // Update the RecyclerView
        dailyActivityView.adapter?.notifyItemRemoved(position)
        updateProgress()
        displayNoActivity()
    }

    private fun displayNoActivity(){
        if(dailyActivityList.isEmpty()) {
            progress = 100
            updateProgressView()
            val noActivityView: TextView = findViewById(R.id.noActivity)
            noActivityView.text = "All activities are done!"
        }
    }
}
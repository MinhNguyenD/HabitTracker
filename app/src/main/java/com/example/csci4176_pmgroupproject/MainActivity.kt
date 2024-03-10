package com.example.csci4176_pmgroupproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.ceil

class MainActivity : AppCompatActivity(), TodoItemClickListener {
    private var progress = 0
    private var numActivities = 0
    private var percentageIncrease = 0
    private lateinit var progressBarView : ProgressBar
    private lateinit var progressTextView : TextView
    private lateinit var todayTextView : TextView
    private lateinit var dailyActivityView : RecyclerView
    private lateinit var activityList : ArrayList<ActivityModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todayTextView = findViewById(R.id.todayText)
        progressBarView = findViewById(R.id.progressBar)
        progressTextView = findViewById(R.id.progressText)
        dailyActivityView = findViewById(R.id.dailyActivityList)
        activityList =  populateData()
        dailyActivityView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val activityAdapter =  DailyActivityAdapter(activityList, this)
        dailyActivityView.adapter = activityAdapter
        numActivities = activityAdapter.itemCount
        initToday()
        initProgress()
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
        progressBarView.progress = progress.toInt()
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
        activityList.removeAt(position)
        // Update the RecyclerView
        dailyActivityView.adapter?.notifyItemRemoved(position)
        updateProgress()
        if(activityList.isEmpty()){
            val noActivityView : TextView = findViewById(R.id.noActivity)
            noActivityView.text = "All activities are done!"
        }
    }

    private fun populateData() : ArrayList<ActivityModel>{
        val activity1 = ActivityModel(
            title = "Morning Jog",
            category = "Exercise",
            streak = 10,
            progress = 50,
            mood = "Energetic",
            energy = "High",
            note = "Enjoyed the fresh air today!",
        )

        val activity2 = ActivityModel(
            title = "Reading",
            category = "Learning",
            streak = 5,
            progress = 100,
            mood = "Calm",
            energy = "Relaxed",
            note = "Finished reading a great book!",
        )

        val activity3 = ActivityModel(
            title = "Cooking",
            category = "Hobby",
            streak = 2,
            progress = 30,
            mood = "Happy",
            energy = "Medium",
            note = "Tried a new recipe today!",
        )

        val activities : ArrayList<ActivityModel> = arrayListOf(
            activity1,
            activity1,
            activity1,
            activity1,
            activity2,
            activity3
        )
        return activities
    }

}
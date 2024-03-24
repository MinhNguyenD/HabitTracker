package com.example.csci4176_pmgroupproject

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csci4176_pmgroupproject.Model.ActivityModel

class OnDateActivity : AppCompatActivity(){
    private lateinit var dailyActivityView : RecyclerView
    private lateinit var activityAdapter : DailyActivityAdapter
    private var activitiesOnDate : ArrayList<ActivityModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_on_date)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dailyActivityView = findViewById(R.id.dailyActivityList)
        dailyActivityView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val selectedDate = intent.extras?.getString("selectedDate")
        if(selectedDate != null){
            DatabaseAPI.getDailyActivitiesOnDate(selectedDate){dailyActivities ->
                activitiesOnDate = dailyActivities
//                activityAdapter =  DailyActivityAdapter(activitiesOnDate)
                dailyActivityView.adapter = activityAdapter
            }
        }
    }
}
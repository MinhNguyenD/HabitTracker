package com.example.csci4176_pmgroupproject

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csci4176_pmgroupproject.Model.ActivityModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.roundToInt

class OnDateActivity : AppCompatActivity(){
    private lateinit var dailyActivityView : RecyclerView
    private lateinit var activityAdapter : DailyActivityAdapter
    private lateinit var dateTitle : TextView
    private lateinit var numActivities : TextView
    private lateinit var averageMood : TextView
    private lateinit var averageEnergy: TextView
    private lateinit var completionPercentage : TextView
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

        // Initialize views
        dateTitle = findViewById(R.id.dateTitle)
        numActivities = findViewById(R.id.numActivities)
        averageMood = findViewById(R.id.averageMood)
        averageEnergy = findViewById(R.id.averageEnergy)
        dailyActivityView = findViewById(R.id.dailyActivityList)
        completionPercentage = findViewById(R.id.completionPercentage)
        dailyActivityView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Retrieve selected date from intent extras
        val selectedDate = LocalDate.parse(intent.extras?.getString("selectedDate"))
        dateTitle.text = "${selectedDate.month.getDisplayName(TextStyle.FULL, Locale.US)} ${selectedDate.dayOfMonth}, ${selectedDate.year}"

        // Fetch daily activities for the selected date from Firebase database
        if (selectedDate != null) {
            DatabaseAPI.getDailyActivitiesOnDate(selectedDate.toString()) { dailyActivities ->
                if (dailyActivities.size > 0) {
                    activitiesOnDate = dailyActivities
                    activityAdapter = DailyActivityAdapter(activitiesOnDate, R.layout.acitivity_item_view_only)
                    dailyActivityView.adapter = activityAdapter

                    // Calculate completion rate, average mood and energy for the activities on the selected date
                    var sumMood = 0.0
                    var sumEnergy = 0.0
                    var numActivitiesDone = 0.0
                    val totalActivities = activitiesOnDate.size
                    for (activity in activitiesOnDate) {
                        sumMood += activity.mood.ordinal + 1 // Avoid 0 as a sum
                        sumEnergy += activity.energy.ordinal + 1
                        if(activity.isFinished){
                            numActivitiesDone++
                        }
                    }
                    val completionRate : Double = (numActivitiesDone/totalActivities)*100
                    val avgMood: Int = (sumMood / totalActivities - 1).roundToInt()
                    val avgEnergy = (sumEnergy / totalActivities - 1).roundToInt()

                    // Update UI with the calculated values
                    numActivities.text = "$totalActivities"
                    completionPercentage.text = "$completionRate%"
                    averageMood.text = "${ActivityMood.entries[avgMood]}"
                    averageEnergy.text = "${ActivityEnergy.entries[avgEnergy]}"
                } else {
                    // Display default values if no activities found for the selected date
                    completionPercentage.text = "0%"
                    numActivities.text = "0"
                    averageMood.text = "None"
                    averageEnergy.text = "None"
                }
            }
        }
    }

}
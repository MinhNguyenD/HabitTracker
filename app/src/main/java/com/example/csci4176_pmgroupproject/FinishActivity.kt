package com.example.csci4176_pmgroupproject

import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.csci4176_pmgroupproject.Model.ActivityModel
import java.time.LocalDate

class FinishActivity : AppCompatActivity() {
    private lateinit var activityTitleTextView : TextView
    private lateinit var noteEditText : EditText
    private lateinit var saveButton : Button
    private lateinit var deleteButton : Button
    private lateinit var selectedActivity : ActivityModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_finish)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        activityTitleTextView = findViewById(R.id.activityTitle)
        noteEditText = findViewById(R.id.noteEditText)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)

        // Retrieve selected activity ID from intent extras
        val bundle : Bundle? = intent.extras
        val selectedActivityId : String? = bundle?.getString("selectedActivityId")

        // Fetch activity details from the database
        DatabaseAPI.getActivityById(selectedActivityId!!){retrievedActivity ->
            selectedActivity = retrievedActivity
            activityTitleTextView.text = selectedActivity.title
            selectedActivity.isFinished = true
            selectedActivity.streak += 1
            DatabaseAPI.updateActivity(selectedActivity)
        }

        // Set onClickListeners for save and delete buttons
        saveButton.setOnClickListener {
            submitForm()
            reward()
            finish()
        }
        deleteButton.setOnClickListener {
            DatabaseAPI.deleteActivity(selectedActivity.taskId)
            finish()
        }
    }

    /**
     * Submits the form by updating the activity's note, energy, mood and saving it to the database.
     */
    fun submitForm(){
        // TODO: update emotion and energy
        selectedActivity.note += "\n[${LocalDate.now()}] ${noteEditText.text}"
        DatabaseAPI.updateActivity(selectedActivity)
    }

    /**
     * When user finish a activity, give a reward
     */
    private fun reward(){
        var reward = ""
        when(selectedActivity.streak){
            7 -> reward = "One Week Streak"
            14 -> reward = "Two Week Streak"
            30 -> reward = "One Month Streak"
            183 -> reward = "Half a Year Streak"
            365 -> reward = "One Year Streak"
            730 -> reward = "Two Year Streak"
            1825 -> reward = "Five Year Streak"
            3650 -> reward = "One Year Streak"
        }
        var rewardText : TextView = findViewById(R.id.textViewFinishReward)
        rewardText.text = reward
    }
}
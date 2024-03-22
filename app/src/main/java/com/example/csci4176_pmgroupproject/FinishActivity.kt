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

class FinishActivity : AppCompatActivity() {
    private lateinit var activityTitleTextView : TextView
    private lateinit var noteEditText : EditText
    private lateinit var saveButton : Button
    private lateinit var deleteButton : Button
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

        val bundle : Bundle? = intent.extras
        val selectedActivityId : String? = bundle?.getString("selectedActivityId")
        DatabaseAPI.getActivityById(selectedActivityId!!){selectedActivity ->
            activityTitleTextView.text = selectedActivity.title
            selectedActivity.isFinished = true
            selectedActivity.streak += 1
            DatabaseAPI.updateActivity(selectedActivity)
        }
        saveButton.setOnClickListener {
            finish()
        }
        deleteButton.setOnClickListener {
            finish()
        }
    }

    fun submitForm(){
        // TODO: Save data to firebase
    }
}
package com.example.csci4176_pmgroupproject

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
    private lateinit var energySeekBar: SeekBar
    private lateinit var moodSeekBar: SeekBar
    private lateinit var energy : ActivityEnergy
    private lateinit var mood : ActivityMood

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
        energySeekBar = findViewById(R.id.energySeekBar)
        moodSeekBar = findViewById(R.id.moodSeekBar)

        // Retrieve selected activity ID from intent extras
        val bundle : Bundle? = intent.extras
        val selectedActivityId : String? = bundle?.getString("selectedActivityId")

        // Fetch activity details from the database
        DatabaseAPI.getActivityById(selectedActivityId!!){retrievedActivity ->
            selectedActivity = retrievedActivity
            activityTitleTextView.text = selectedActivity.title
        }

        // Set change listener for seekbars
        energySeekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var thumbDrawable : Drawable?
                var progressColor : Int
                if(progress < 25){
                    energy = ActivityEnergy.TIRED
                    thumbDrawable = ContextCompat.getDrawable(energySeekBar.context, R.drawable.tired_icon_24)
                    progressColor = ContextCompat.getColor(energySeekBar.context, R.color.red)
                }
                else if(progress < 75){
                    energy = ActivityEnergy.NEUTRAL
                    thumbDrawable = ContextCompat.getDrawable(energySeekBar.context, R.drawable.neutral_energy_icon_24)
                    progressColor = ContextCompat.getColor(energySeekBar.context, R.color.yellow)
                }
                else{
                    energy = ActivityEnergy.ENERGIZED
                    thumbDrawable = ContextCompat.getDrawable(energySeekBar.context, R.drawable.energized_icon_24)
                    progressColor = ContextCompat.getColor(energySeekBar.context, R.color.submit)
                }
                energySeekBar.thumb = thumbDrawable
                val colorStateList = ColorStateList.valueOf(progressColor)
                energySeekBar.progressTintList = colorStateList

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        moodSeekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var thumbDrawable : Drawable?
                var progressColor : Int
                if(progress < 25){
                    mood = ActivityMood.DISAPPOINTED
                    thumbDrawable = ContextCompat.getDrawable(moodSeekBar.context, R.drawable.disappoint_icon_24)
                    progressColor = ContextCompat.getColor(moodSeekBar.context, R.color.red)
                }
                else if(progress < 75){
                    mood = ActivityMood.NEUTRAL
                    thumbDrawable = ContextCompat.getDrawable(moodSeekBar.context, R.drawable.neutral_icon_24)
                    progressColor = ContextCompat.getColor(moodSeekBar.context, R.color.yellow)
                }
                else{
                    mood = ActivityMood.ACCOMPLISHED
                    thumbDrawable = ContextCompat.getDrawable(moodSeekBar.context, R.drawable.accomplish_icon_24)
                    progressColor = ContextCompat.getColor(moodSeekBar.context, R.color.submit)
                }
                moodSeekBar.thumb = thumbDrawable
                val colorStateList = ColorStateList.valueOf(progressColor)
                moodSeekBar.progressTintList = colorStateList
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })


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
        selectedActivity.mood = this.mood
        selectedActivity.energy = this.energy
        if(noteEditText.text.isNotEmpty()){
            selectedActivity.note += "\n[${LocalDate.now()}] ${noteEditText.text}"
        }
        DatabaseAPI.updateActivity(selectedActivity)
    }

    /**
     * When user finish a activity, give a reward
     */
    private fun reward(){
        // TODO: add reward details
    }
}
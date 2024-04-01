package com.example.csci4176_pmgroupproject.Activities

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.csci4176_pmgroupproject.Model.ActivityModel
import com.google.firebase.auth.FirebaseAuth
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.example.csci4176_pmgroupproject.Model.ActivityEnergy
import com.example.csci4176_pmgroupproject.Model.ActivityMood
import com.example.csci4176_pmgroupproject.Model.Badge
import com.example.csci4176_pmgroupproject.Database.DatabaseAPI
import com.example.csci4176_pmgroupproject.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.time.LocalDate

class FinishActivity : AppCompatActivity() {
    private lateinit var activityTitleTextView : TextView
    private lateinit var noteEditText : EditText
    private lateinit var saveButton : Button
    private lateinit var deleteButton : Button
    private lateinit var pictureButton : ImageButton
    private lateinit var pictureIV:  ImageView
    private lateinit var pictureURI : Uri
    private lateinit var selectedActivity : ActivityModel
    private lateinit var energySeekBar: SeekBar
    private lateinit var moodSeekBar: SeekBar
    private lateinit var awardPopUp : Dialog
    private lateinit var awardMessage : TextView
    private lateinit var awardBadge : ImageView
    private lateinit var badge : String
    private lateinit var shareButton : Button
    private lateinit var exitButton: Button
    private var energy : ActivityEnergy = ActivityEnergy.NEUTRAL
    private var mood : ActivityMood = ActivityMood.NEUTRAL

    private lateinit var userId: String
    private lateinit var storageRef: StorageReference
    private var buttonSelected = false
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        pictureIV.setImageURI(null)
        pictureIV.setImageURI(pictureURI)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        DatabaseAPI.currentUser = FirebaseAuth.getInstance().currentUser!!
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
        awardPopUp = Dialog(this)
        awardPopUp.setContentView(R.layout.reward_popup)
        awardPopUp.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        awardPopUp.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        awardPopUp.setCancelable(true)
        awardMessage = awardPopUp.findViewById(R.id.awardMessage)
        awardBadge = awardPopUp.findViewById(R.id.awardBadge)
        shareButton = awardPopUp.findViewById(R.id.shareButton)
        exitButton = awardPopUp.findViewById(R.id.exitButton)


        // Retrieve selected activity ID from intent extras
        val bundle : Bundle? = intent.extras
        val selectedActivityId : String? = bundle?.getString("selectedActivityId")

        DatabaseAPI.getCurrentUser { user ->
            userId = user.uid

            // Storage for the image for that specific activity
            storageRef = selectedActivityId?.let {
                FirebaseStorage.getInstance().getReference("Users").child(userId)
                    .child("Activities").child(it).child("images")
            }!!
        }

        // Fetch activity details from the database
        DatabaseAPI.getActivityById(selectedActivityId!!) { retrievedActivity ->
            selectedActivity = retrievedActivity
            activityTitleTextView.text = selectedActivity.title
        }
        DatabaseAPI.getUserById(DatabaseAPI.user.uid) { user ->

        }
        DatabaseAPI.getAllActivity { allActivities ->
            var totalStreak = 0
            for (activity in allActivities) {
                totalStreak += activity.streak
            }
            if (totalStreak > 10 && totalStreak < 30 && DatabaseAPI.user.badge != Badge.BRONZE) {
                DatabaseAPI.user.badge = Badge.BRONZE
                DatabaseAPI.updateUser(DatabaseAPI.user).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        awardMessage.text =
                            "You have earned a Bronze Badge for maintaining more than 10 streaks for all activities"
                        awardBadge.setImageResource(R.drawable.bronze_badge)
                        badge = "Bronze Badge"
                        awardPopUp.show()
                    } else {
                        Log.e("Finish Activity:", "Error updating user badge")
                    }

                }
            } else if (totalStreak > 30 && totalStreak < 70 && DatabaseAPI.user.badge != Badge.SILVER) {
                DatabaseAPI.user.badge = Badge.SILVER
                DatabaseAPI.updateUser(DatabaseAPI.user).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        awardMessage.text =
                            "You have earned a Sliver Badge for maintaining more than 30 streaks for all activities"
                        awardBadge.setImageResource(R.drawable.sliver_badge)
                        badge = "Silver Badge"
                        awardPopUp.show()
                    } else {
                        Log.e("Finish Activity:", "Error updating user badge")
                    }

                }
            } else if (totalStreak > 70 && DatabaseAPI.user.badge != Badge.GOLD) {
                DatabaseAPI.user.badge = Badge.GOLD
                DatabaseAPI.updateUser(DatabaseAPI.user).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        awardMessage.text =
                            "You have earned a Gold Badge for maintaining more than 70 streaks for all activities"
                        awardBadge.setImageResource(R.drawable.gold_badge)
                        badge = "Gold Badge"
                        awardPopUp.show()
                    } else {
                        Log.e("Finish Activity:", "Error updating user badge")
                    }

                }
            }
        }

        shareButton.setOnClickListener {
            DatabaseAPI.user.notifyFriends("${DatabaseAPI.user.username} has earned a new $badge")
            awardPopUp.dismiss()
        }

        exitButton.setOnClickListener {
            awardPopUp.dismiss()
        }

        // Set change listener for seekbars
        energySeekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var thumbDrawable : Drawable?
                var progressColor : Int
                if(progress < 25){
                    energy = ActivityEnergy.TIRED
                    thumbDrawable = ContextCompat.getDrawable(energySeekBar.context,
                        R.drawable.tired_icon_24
                    )
                    progressColor = ContextCompat.getColor(energySeekBar.context, R.color.red)
                }
                else if(progress < 75){
                    energy = ActivityEnergy.NEUTRAL
                    thumbDrawable = ContextCompat.getDrawable(energySeekBar.context,
                        R.drawable.neutral_energy_icon_24
                    )
                    progressColor = ContextCompat.getColor(energySeekBar.context, R.color.yellow)
                }
                else{
                    energy = ActivityEnergy.ENERGIZED
                    thumbDrawable = ContextCompat.getDrawable(energySeekBar.context,
                        R.drawable.energized_icon_24
                    )
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
                    thumbDrawable = ContextCompat.getDrawable(moodSeekBar.context,
                        R.drawable.disappoint_icon_24
                    )
                    progressColor = ContextCompat.getColor(moodSeekBar.context, R.color.red)
                }
                else if(progress < 75){
                    mood = ActivityMood.NEUTRAL
                    thumbDrawable = ContextCompat.getDrawable(moodSeekBar.context,
                        R.drawable.neutral_icon_24
                    )
                    progressColor = ContextCompat.getColor(moodSeekBar.context, R.color.yellow)
                }
                else{
                    mood = ActivityMood.ACCOMPLISHED
                    thumbDrawable = ContextCompat.getDrawable(moodSeekBar.context,
                        R.drawable.accomplish_icon_24
                    )
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


        // Picture related UI components
        pictureButton = findViewById(R.id.pictureButton)
        pictureIV = findViewById(R.id.capturedImage)
        pictureURI = generatePictureURI()

        // OnClickListener for the camera/remove image button
        pictureButton.setOnClickListener {
            // If the button hasn't been selected then the camera is being displayed
            if (!buttonSelected)
            {
                // Check the camera permissions of the user and proceeds accordingly
                checkCameraPermission()

                // Once the image has been captured display the remove icon and flip the buttonSelected boolean
                pictureButton.setImageResource(R.drawable.baseline_remove_circle_24)
                buttonSelected = true
            }

            // If the button has been selected already
            else
            {
                // The user wants to remove the picture so set the bitmap to null
                pictureIV.setImageBitmap(null)

                // Swap back to displaying the camera icon and flip the buttonSelected boolean
                pictureButton.setImageResource(R.drawable.baseline_photo_camera_24)
                buttonSelected = false
            }
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
        selectedActivity.mood = this.mood
        selectedActivity.energy = this.energy
        if(noteEditText.text.isNotEmpty()){
            selectedActivity.note += "\n[${LocalDate.now()}] ${noteEditText.text}"
        }

        // TODO: update emotion and energy
        selectedActivity.note += "\n[${LocalDate.now()}] ${noteEditText.text}"
        val pictureURL = uploadImagesToStorage(pictureURI)

        DatabaseAPI.updateActivity(selectedActivity)
    }

    /**
     * When user finish a activity, give a reward
     */
    private fun reward(){
        // TODO: add reward details
    }

    // Create the URI for when the picture is captured
    private fun generatePictureURI(): Uri
    {
        var img = File(filesDir, "camera_photos.png")
        return FileProvider.getUriForFile(this, "com.example.csci4176_pmgroupproject.FileProvider", img)
    }

    // Method to check whether or not a user has given camera permissions
    private fun checkCameraPermission()
    {
        // If permission is not yet granted request the permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        { ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE) }

        // If permission has already been granted proceed to open the camera
        else
        { contract.launch(pictureURI) }
    }

    // Handles the result of a user being prompted with camera permissions
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode)
        {
            // If it matches the permission request code
            CAMERA_PERMISSION_REQUEST_CODE -> {
                // If result is not empty and permission has been granted notify the user
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                { Toast.makeText(this, "Camera Permission Accepted", Toast.LENGTH_LONG).show() }

                // If the user hasn't given permission for this function notify them
                else
                { Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_LONG).show() }

                return
            }
        }
    }
    private fun uploadImagesToStorage(imageUri: Uri): String?{
        var imgUrl: String? = null

        imageUri.let{
            storageRef.putFile(imageUri).addOnSuccessListener { task->
                task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url->
                    imgUrl = url.toString()
                }
            }
        }

        return imgUrl
    }
}
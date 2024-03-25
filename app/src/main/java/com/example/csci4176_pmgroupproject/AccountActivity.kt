package com.example.csci4176_pmgroupproject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import android.content.Intent
import android.media.Image
import android.provider.ContactsContract.Data
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class AccountActivity : BaseActivity() {

    private lateinit var avatar : ImageView
    private lateinit var userName : TextView
    private lateinit var switchMode : Switch
    private lateinit var logOutButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        userName = findViewById(R.id.userName)
        switchMode = findViewById(R.id.modeSwitch)
        logOutButton = findViewById(R.id.logOutButton)
        avatar = findViewById(R.id.avatar)
        avatar.setImageResource(R.drawable.avatar)

        DatabaseAPI.getCurrentUser {user ->
            userName.text = user.username
        }

        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getBoolean("night",false)

        val changeThemeSwitch = findViewById<Switch>(R.id.modeSwitch)
        changeThemeSwitch.isChecked = nightMode
        Log.d("Current mode", nightMode.toString())

        val editor = sharedPreferences.edit()

        changeThemeSwitch.setOnCheckedChangeListener { switchButton, isChecked ->
            if (switchButton.isPressed) {
                if (!isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    editor.putBoolean("night", false)
                    editor.apply()

                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    editor.putBoolean("night", true)
                    editor.apply()
                }
            }
        }

        logOutButton.setOnClickListener{
            DatabaseAPI.logOutUser()
            startActivity(Intent(this, loginActivity::class.java))
            finish()
        }
    }
}
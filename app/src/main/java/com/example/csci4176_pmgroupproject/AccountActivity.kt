package com.example.csci4176_pmgroupproject

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class AccountActivity : BaseActivity() {

    private lateinit var avatar : ImageView
    private lateinit var userName : TextView
    private lateinit var switchMode : Switch
    private lateinit var logOutButton : Button
    private lateinit var modeSharedPreferences : SharedPreferences
    private var isNightMode : Boolean = false

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

        modeSharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        isNightMode = modeSharedPreferences.getBoolean("night",false)

        switchMode.isChecked = isNightMode
        Log.d("Current mode", isNightMode.toString())

        val editor = modeSharedPreferences.edit()

        switchMode.setOnCheckedChangeListener { switchButton, isChecked ->
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
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        isNightMode = modeSharedPreferences.getBoolean("night", false)
        switchMode.isChecked = isNightMode
    }
}
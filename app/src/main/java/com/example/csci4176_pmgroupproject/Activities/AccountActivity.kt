package com.example.csci4176_pmgroupproject.Activities

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
import com.example.csci4176_pmgroupproject.Model.Badge
import com.example.csci4176_pmgroupproject.Database.DatabaseAPI
import com.example.csci4176_pmgroupproject.R

class AccountActivity : BaseActivity() {

    private lateinit var avatar : ImageView
    private lateinit var userName : TextView
    private lateinit var switchMode : Switch
    private lateinit var logOutButton : Button
    private lateinit var modeSharedPreferences : SharedPreferences
    private lateinit var badgeImageView: ImageView
    private var isNightMode : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        userName = findViewById(R.id.userName)
        switchMode = findViewById(R.id.modeSwitch)
        logOutButton = findViewById(R.id.logOutButton)
        badgeImageView = findViewById(R.id.badgeImage)
        avatar = findViewById(R.id.avatar)
        avatar.setImageResource(R.drawable.avatar)

        DatabaseAPI.getCurrentUser { user ->
            userName.text = user.username
            when (user.badge) {
                Badge.BRONZE -> badgeImageView.setImageResource(R.drawable.bronze_badge)
                Badge.SILVER -> badgeImageView.setImageResource(R.drawable.sliver_badge)
                Badge.GOLD -> badgeImageView.setImageResource(R.drawable.gold_badge)
                else -> badgeImageView.setImageResource(R.drawable.no_badge)
            }
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
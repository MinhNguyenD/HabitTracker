package com.example.csci4176_pmgroupproject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate

class AccountActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

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
    }
}
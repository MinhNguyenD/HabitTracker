package com.example.csci4176_pmgroupproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        val navigationFragment = NavigationBar()
        supportFragmentManager.beginTransaction().add(R.id.navbarFrame, navigationFragment).commit()

        val changeThemeSwitch = findViewById<Switch>(R.id.modeSwitch)

        // Check the current mode and update the switch position accordingly
        val isNightMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        changeThemeSwitch.isChecked = isNightMode

        changeThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val newNightMode = if (isChecked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }

            if (newNightMode != AppCompatDelegate.getDefaultNightMode()) {
                // Apply the new theme
                try {
                    AppCompatDelegate.setDefaultNightMode(newNightMode)
                    recreate() // Recreate the activity to apply the new theme
                } catch (e: Exception) {
                    Log.e("AccountActivity", "Error changing theme: ${e.message}")
                }
            }
        }
    }
}
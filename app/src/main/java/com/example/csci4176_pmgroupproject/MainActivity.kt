package com.example.csci4176_pmgroupproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Date

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this, loginActivity::class.java))//this is only for testing rn
        val activityList = DatabaseAPI.getAllActivity()
//        val navbarFragment = NavigationBar()
//
//        // Add navbar fragment to the activity
//        supportFragmentManager.beginTransaction()
//            .add(R.id.navbarFrame, navbarFragment)
//            .commit()
    }
}
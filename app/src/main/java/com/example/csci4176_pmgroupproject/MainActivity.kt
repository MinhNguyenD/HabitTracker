package com.example.csci4176_pmgroupproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this, loginActivity::class.java))//this is only for testing rn

//        val navbarFragment = NavigationBar()
//
//        // Add navbar fragment to the activity
//        supportFragmentManager.beginTransaction()
//            .add(R.id.navbarFrame, navbarFragment)
//            .commit()
    }
}
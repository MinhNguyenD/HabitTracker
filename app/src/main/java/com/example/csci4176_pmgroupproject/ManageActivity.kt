package com.example.csci4176_pmgroupproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction


class ManageActivity : BaseActivity() {
    private var habitNameList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)

        DatabaseAPI.getAllHabits { habitList ->
            for(habit in habitList){
                habitNameList.add(habit.habitName)
            }

            // Display the habit fragment at first
            val habitFragment: Fragment = ManageHabits.newInstance(habitNameList)
            supportFragmentManager.beginTransaction()
                .replace(R.id.root_container, habitFragment)
                .commit()
        }
    }

    fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.root_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}

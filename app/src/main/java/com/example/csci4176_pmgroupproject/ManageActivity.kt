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

            val habitFragment: Fragment = CreateHabit.newInstance(habitNameList)
            val fragmentTrans: FragmentTransaction = supportFragmentManager.beginTransaction()

            fragmentTrans.replace(R.id.root_container, habitFragment).commit()
        }

    }
}

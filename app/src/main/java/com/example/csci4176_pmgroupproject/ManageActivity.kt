package com.example.csci4176_pmgroupproject

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
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
            replaceFragment(habitFragment, false)
        }
    }

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = true){
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.root_container, fragment)

        if(addToBackStack){
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    fun hideNavigationBar(){
        val frameLayout: FrameLayout = findViewById(R.id.navbarFrame)
        frameLayout.visibility = View.GONE
    }

    fun showNavigationBar(){
        val frameLayout: FrameLayout = findViewById(R.id.navbarFrame)
        frameLayout.visibility = View.VISIBLE
    }
}

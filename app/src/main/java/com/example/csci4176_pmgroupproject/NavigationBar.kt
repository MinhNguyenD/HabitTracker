package com.example.csci4176_pmgroupproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationBar(context : Context) : Fragment() {
    private lateinit var bottomNavigationBar: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val navBarView = inflater.inflate(R.layout.fragment_navigation_bar, container, false)
        bottomNavigationBar = navBarView.findViewById(R.id.bottomNavbar)

        // Set the selected item based on the current activity
        when (context) {
            is HomeActivity -> bottomNavigationBar.selectedItemId = R.id.home
            is CalendarActivity ->  bottomNavigationBar.selectedItemId = R.id.calendar
//            is ManageActivity -> bottomNavigationBar.selectedItemId = R.id.manage
//            is FriendActivity -> bottomNavigationBar.selectedItemId = R.id.friends
//            is AccountActivity -> bottomNavigationBar.selectedItemId = R.id.account
        }

        bottomNavigationBar.setOnItemSelectedListener {item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this.activity, HomeActivity::class.java))
                    true
                }

                R.id.calendar -> {
                    startActivity(Intent(this.activity, CalendarActivity::class.java))
                    true
                }

                R.id.manage -> {
                    //startActivity(Intent(this.activity, ManagementActivity::class.java))//this is only for testing rn
                    true
                }

                R.id.friends -> {
                    //startActivity(Intent(this.activity, FriendsActivity::class.java))//this is only for testing rn
                    true
                }
                R.id.account -> {
//                    startActivity(Intent(this.activity, ProfileActivity::class.java))
                    true
                }

                else -> false
            }
        }

        return navBarView
    }
}
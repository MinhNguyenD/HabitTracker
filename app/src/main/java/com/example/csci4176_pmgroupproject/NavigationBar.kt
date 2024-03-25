package com.example.csci4176_pmgroupproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationBar : Fragment() {
    private lateinit var bottomNavigationBar: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val navBarView = inflater.inflate(R.layout.fragment_navigation_bar, container, false)
        bottomNavigationBar = navBarView.findViewById(R.id.bottomNavbar)
        bottomNavigationBar.setOnItemSelectedListener {item ->
            when (item.itemId) {
                R.id.home -> {
                    if (this.activity !is HomeActivity) {
                        startActivity(Intent(this.activity, HomeActivity::class.java))
                    }
                    true
                }

                R.id.calendar -> {
                    //startActivity(Intent(this.activity, CalendarActivity::class.java))//this is only for testing rn
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
                    if (this.activity !is AccountActivity) {
                        startActivity(Intent(this.activity, AccountActivity::class.java))
                    }
                    true
                }
                else -> false
            }
        }

        return navBarView
    }
}
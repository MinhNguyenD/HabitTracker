package com.example.csci4176_pmgroupproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationBar: Fragment() {
    private lateinit var bottomNavigationBar: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val navBarView = inflater.inflate(R.layout.fragment_navigation_bar, container, false)
        bottomNavigationBar = navBarView.findViewById(R.id.bottomNavbar)
        when(this.activity){
            is HomeActivity -> bottomNavigationBar.selectedItemId = R.id.home
            is AccountActivity -> bottomNavigationBar.selectedItemId = R.id.account
            is ManageActivity -> bottomNavigationBar.selectedItemId = R.id.manage
        }
//            is ManageActivity -> bottomNavigationBar.selectedItemId = R.id.manage
//            is FriendActivity -> bottomNavigationBar.selectedItemId = R.id.friends
//            is AccountActivity -> bottomNavigationBar.selectedItemId = R.id.account
        }

        bottomNavigationBar.setOnItemSelectedListener {item ->
            when (item.itemId) {
                R.id.home -> {
                    if (this.activity !is HomeActivity) {
                        startActivity(Intent(this.activity, HomeActivity::class.java))
                    }
                    true
                }

                R.id.calendar -> {
//                    if (this.activity !is CalendarActivity) {
//                        startActivity(Intent(this.activity, CalendarActivity::class.java))//this is only for testing rn
//                    }
                    true
                }

                R.id.manage -> {
                    if (this.activity !is ManageActivity) {
                        startActivity(Intent(this.activity, ManageActivity::class.java))//this is only for testing rn
                    }
                    true
                }

                R.id.friends -> {
//                    if (this.activity !is ManagementActivity) {
//                        startActivity(Intent(this.activity, ManagementActivity::class.java))//this is only for testing rn
//                    }
                    //startActivity(Intent(this.activity, FriendsActivity::class.java))//this is only for testing rn
                    true
                }

                R.id.account -> {
                    if (this.activity !is AccountActivity) {
                        startActivity(Intent(this.activity, AccountActivity::class.java))//this is only for testing rn
                    }
                    true
                }
                else -> false
            }
        }

        return navBarView
    }

    override fun onResume() {
        super.onResume()
        when(this.activity){
            is HomeActivity -> bottomNavigationBar.selectedItemId = R.id.home
            is AccountActivity -> bottomNavigationBar.selectedItemId = R.id.account
            is ManageActivity -> bottomNavigationBar.selectedItemId = R.id.manage
        }
    }
}
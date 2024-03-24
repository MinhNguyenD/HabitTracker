package com.example.csci4176_pmgroupproject

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView

class AccountActivity : AppCompatActivity() {
    private lateinit var avatar : ImageView
    private lateinit var userName : TextView
    private lateinit var switchMode : Switch
    private lateinit var logOutButton : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        userName = findViewById(R.id.userName)
        switchMode = findViewById(R.id.modeSwitch)
        logOutButton = findViewById(R.id.logOutButton)
        avatar = findViewById(R.id.avatar)
        avatar.setImageResource(R.drawable.avatar)

        DatabaseAPI.getCurrentUser {user ->
            userName.text = user.username
        }

        val navbarFragment = NavigationBar()

        // Add navbar fragment to the activity
        supportFragmentManager.beginTransaction()
            .add(R.id.navbarFrame, navbarFragment)
            .commit()


        logOutButton.setOnClickListener{
            DatabaseAPI.logOutUser()
            startActivity(Intent(this, loginActivity::class.java))
            finish()
        }
    }
}
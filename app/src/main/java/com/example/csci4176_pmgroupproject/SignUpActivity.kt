package com.example.csci4176_pmgroupproject

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //user clicks login button
        findViewById<Button>(R.id.buttonRegister)
            .setOnClickListener {
                //TODO check user info and add to database
            }
    }
}
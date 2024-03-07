package com.example.csci4176_pmgroupproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class loginActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //user clicks login button
        findViewById<Button>(R.id.buttonLogin)
            .setOnClickListener {
                //TODO check user info
            }

        //user clicks sign up button
        findViewById<Button>(R.id.buttonSignUp)
            .setOnClickListener {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
    }

}
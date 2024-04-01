package com.example.csci4176_pmgroupproject.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.csci4176_pmgroupproject.Database.DatabaseAPI
import com.example.csci4176_pmgroupproject.R

class LoginActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Get the text fields where the user would input their credentials
        val emailField = findViewById(R.id.loginEmail) as EditText
        val passField = findViewById(R.id.loginPassword) as EditText


        // User clicks login button
        findViewById<Button>(R.id.buttonLogin)
            .setOnClickListener {

                // Check user info with DB
                DatabaseAPI.emailLogin(emailField.text.toString(), passField.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful)
                        {
                            /**Temporary**/
                            Toast.makeText(baseContext, "Login Successful!", Toast.LENGTH_LONG).show()
                            /**Temporary**/
                            finish()
                            startActivity(Intent(this, HomeActivity::class.java))
                        } else
                        {
                            // Alert the user the credentials aren't valid
                            Toast.makeText(baseContext,
                                "Email or password is invalid",
                                Toast.LENGTH_LONG).show()
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        }
                    }
            }

        //user clicks sign up button
        findViewById<Button>(R.id.buttonSignUp)
            .setOnClickListener {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
    }

}
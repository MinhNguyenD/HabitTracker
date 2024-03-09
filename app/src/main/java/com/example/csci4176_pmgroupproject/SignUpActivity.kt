package com.example.csci4176_pmgroupproject

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Get the email and password fields from the UI
        var signupEmail = findViewById(R.id.registerEmail) as EditText
        var signupPassword = findViewById(R.id.registerPassword) as EditText

        //user clicks login button
        findViewById<Button>(R.id.buttonRegister)
            .setOnClickListener {
                // Pass the email and password to firebase to make a new user
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    signupEmail.text.toString(), signupPassword.text.toString()
                ).addOnCompleteListener{ task ->

                    // If the user could be created
                    if (task.isSuccessful)
                    {
                        val user = FirebaseAuth.getInstance().currentUser
                        // Start next activity for the user
                    }

                    // If the user could not be created
                    else
                    {
                        // Password needs to be at least 6 characters
                        if (signupPassword.text.toString().length < 6)
                        {
                            // Alert the user that the password needs to be longer
                            Toast.makeText(baseContext,
                                "Password must contain at least 6 characters",
                                Toast.LENGTH_LONG).show()
                        }

                        // Some other error occurred with registering
                        else
                        {
                            Toast.makeText(baseContext, "Signup failed.",
                                Toast.LENGTH_SHORT).show()
                            Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                        }

                    }
                }
            }
    }
}
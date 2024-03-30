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
        var signupEmail: EditText = findViewById(R.id.registerEmail)
        var signUpUsername: EditText = findViewById(R.id.registerUsername)
        var signupPassword: EditText = findViewById(R.id.registerPassword)
        var signupVerifyPassword: EditText = findViewById(R.id.registerRePassword)

        //user clicks login button
        findViewById<Button>(R.id.buttonRegister)
            .setOnClickListener {
                val email = signupEmail.text.toString()
                val username = signUpUsername.text.toString()
                val pass1 = signupPassword.text.toString()
                val pass2 = signupVerifyPassword.text.toString()
                // Pass the email and password to firebase to make a new user
                if (email.isNotEmpty() && username.isNotEmpty() && pass1.isNotEmpty() && pass1 == pass2) {
                    DatabaseAPI.emailSignup(email, pass1).addOnCompleteListener { task ->

                        // If the user could be created
                        if (task.isSuccessful) {
                            // Start next activity for the user
                            /**Temporary**/
                            //Toast.makeText(baseContext, "Account Created!", Toast.LENGTH_LONG).show()
                            /**Temporary**/
                            DatabaseAPI.updateUser(DatabaseAPI.currentUser.uid, username).addOnCompleteListener {comp ->
                                if(comp.isSuccessful){
                                    Toast.makeText(baseContext, "User Created!", Toast.LENGTH_LONG).show()
                                    finish()
                                }else {
                                    Toast.makeText(baseContext, "Error occurred when creating user!", Toast.LENGTH_LONG).show()
                                }
                            }
                        }

                        // If the user could not be created
                        else {
                            // Password needs to be at least 6 characters
                            if (pass1.length < 6) {
                                // Alert the user that the password needs to be longer
                                Toast.makeText(
                                    baseContext,
                                    "Password must contain at least 6 characters",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            // Some other error occurred with registering
                            else {
                                Toast.makeText(
                                    baseContext, "Signup failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.w(
                                    ContentValues.TAG,
                                    "createUserWithEmail:failure",
                                    task.exception
                                )
                            }

                        }
                    }
                } else {
                    Toast.makeText(baseContext, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    Log.w(ContentValues.TAG, "Passwords did not match: ${signupPassword.text} != ${signupVerifyPassword.text}",)
                }
            }
    }
}
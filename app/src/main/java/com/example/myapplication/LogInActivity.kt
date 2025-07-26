package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LogInActivity: AppCompatActivity() {
    lateinit var logInEmail: EditText
    lateinit var logInPassword: EditText
    lateinit var logInButton: Button
    lateinit var goToSignUpButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        logInEmail = findViewById(R.id.logInEmailAddress)
        logInPassword = findViewById(R.id.logInPassword)
        logInButton = findViewById(R.id.logInButton)
        goToSignUpButton = findViewById(R.id.goToSignUpButton)

        logInButton.setOnClickListener{
            val email = logInEmail.text.toString()
            val password = logInPassword.text.toString()
            val prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
            val userId = prefs.getInt("loggedInUserId", -1)

            if (userId != -1){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
                finish()
            }
            Thread {
                val user = AppDatabase.getInstance(this).userDao().getUserByUsername(email)

                if (user != null && user.passwordHash == password) {
                    runOnUiThread {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("userId", user.id)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Your email and password do not match, please retry", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
            }
        goToSignUpButton.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
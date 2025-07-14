package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    lateinit var signUpEmail: EditText
    lateinit var signUpPassword: EditText
    lateinit var signUpConfirmPassword: EditText
    lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signUpEmail = findViewById(R.id.signUpEmailAddress)
        signUpPassword = findViewById(R.id.signUpPassword)
        signUpConfirmPassword = findViewById(R.id.signUpConfirmPassword)
        signUpButton = findViewById(R.id.signUpButton)

        signUpButton.setOnClickListener{
            val username = signUpEmail.text.toString()
            val password = signUpPassword.text.toString()
            val confirmPassword = signUpConfirmPassword.text.toString()

            if(username.isNotBlank() && password == confirmPassword){
                val user = User(username = username, passwordHash=password)
                Thread {
                    AppDatabase.getInstance(this).userDao().insert(user)
                    runOnUiThread{
                        Toast.makeText(this, "Account successfully created! Welcome!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LogInActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }.start()
            } else {
                Toast.makeText(this, "Review sign up info. Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
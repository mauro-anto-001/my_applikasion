package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import kotlin.math.sign

class SignUpActivity : AppCompatActivity() {

    lateinit var firstName: EditText
    lateinit var lastName: EditText
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
        firstName = findViewById(R.id.signUpFirstName)
        lastName = findViewById(R.id.signUpLastName)

        signUpButton.setOnClickListener{
            val firstName = firstName.text.toString()
            val lastName = lastName.text.toString()
            val username = signUpEmail.text.toString()
            val password = signUpPassword.text.toString()
            val confirmPassword = signUpConfirmPassword.text.toString()

            if(username.isNotBlank() && password == confirmPassword){
                val user = User(username = username, passwordHash=password, firstName = firstName, lastName = lastName)
                Thread {
                    val insertedId = AppDatabase.getInstance(this).userDao().insert(user)
                    runOnUiThread{
                        getSharedPreferences("MyAppPrefs", MODE_PRIVATE).edit(){
                            putInt("loggedInUserId", insertedId.toInt())
                        }
                        Toast.makeText(this, "Account successfully created! Welcome!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, WelcomeActivity::class.java)
                        intent.putExtra("name", "$firstName $lastName")
                        startActivity(intent)
                        finish()
                    }
                }.start()
            } else {
                Toast.makeText(this, "Please try again. Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
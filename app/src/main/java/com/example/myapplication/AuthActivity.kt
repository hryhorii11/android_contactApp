package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.doOnTextChanged
import com.example.myapplication.databinding.ActivityAuthBinding

const val APP_PREFERENCES = "APP_PREFERENCES"

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var preferences: SharedPreferences
    private var isEmailValid = false
    private var isPasswordValid = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        loadData()

        binding.registerButton.setOnClickListener { register() }
        binding.emailEditText.doOnTextChanged { text, start, before, count ->
            if (!isValidEmail(text.toString())) {
                binding.emailContainer.error = "invalid email"
            } else {
                binding.emailContainer.error = null
            }
        }
        binding.passwordEditText.doOnTextChanged { text, start, before, count ->
            if (text.toString().length < 8) {
                binding.passwordContainer.error = "password must be at least 8 characters long"
            } else {
                binding.passwordContainer.error = null
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
    private fun loadData()
    {
        binding.emailEditText.setText(preferences.getString("email",""))
        binding.passwordEditText.setText(preferences.getString("password",""))

    }
    private fun register() {
         if(binding.rememberId.isChecked) {
             preferences.edit()
                 .putString("password", binding.passwordEditText.text.toString())
                 .putString("email", binding.emailEditText.text.toString())
                 .apply()
         }
         if(checkData()) {
             val intent = Intent(this, MainActivity::class.java)
             intent.putExtra("email", binding.emailEditText.text.toString())
             startActivity(intent)
             overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
         }
    }

    private fun checkData(): Boolean {
      return binding.passwordEditText.text.toString().length>=8 && isValidEmail(binding.emailEditText.text.toString())
    }
}

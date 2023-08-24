package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.doOnTextChanged
import com.example.myapplication.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {


    private val binding: ActivityAuthBinding by lazy {
        ActivityAuthBinding.inflate(layoutInflater)
    }
    private val preferences: SharedPreferences by lazy {
        getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        loadData()
        setListeners()
        setFieldsValidations()
    }

    private fun loadData()
    {
        binding.emailEditText.setText(preferences.getString("email",""))    //todo key -> const
        binding.passwordEditText.setText(preferences.getString("password",""))  //todo key -> const

    }


    private fun setListeners() {
        binding.registerButton.setOnClickListener { register() }
    }

    private fun setFieldsValidations() {
        binding.emailEditText.doOnTextChanged { text, _, _, _ ->
            if (!isValidEmail(text.toString())) {
                binding.emailContainer.error = "invalid email"  //todo to resources!
            } else {
                binding.emailContainer.error = null
            }
        }
        binding.passwordEditText.doOnTextChanged { text, _, _, _ ->
            if (text.toString().length < 8 ) {   //todo 8 -> const  && decompose pass valid
                binding.passwordContainer.error = getString(R.string.passwordLenghtErrorText, 8)  //todo to resources!
            } else {
                binding.passwordContainer.error = null
            }
        }
    }




    private fun register() {
         if(binding.rememberId.isChecked) saveDataToSP()
         if(checkData()) {
             val intent = Intent(this, MainActivity::class.java)    //todo decompose
             intent.putExtra("email", binding.emailEditText.text.toString())    //todo key -> const
             startActivity(intent)
             //todo finish?
             overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
         }
    }

    private fun saveDataToSP() {
        preferences.edit().putString(
            "password",
            binding.passwordEditText.text.toString()
        )   //todo key -> const
            .putString(
                "email",
                binding.emailEditText.text.toString()
            ) //todo key -> const
            .apply()
    }

    private fun checkData(): Boolean {
      return binding.passwordEditText.text.toString().length>=8 && isValidEmail(binding.emailEditText.text.toString())  //todo 8 -> const
    }


    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"   //todo pattern -> const && read about Patterns.EMAIL_ADDRESS
        return email.matches(emailPattern.toRegex())
    }


    companion object {
       private const val APP_PREFERENCES = "APP_PREFERENCES"
    }
}

package com.example.myapplication.presentation.ui.activities


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityAuthBinding
const val emailKey = "email"
const val passwordKey = "password"
class AuthActivity : AppCompatActivity() {

    private val intentEmailKey = "email"
    private val passwordLength = 8
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

    private fun loadData() {
        binding.edittextEmail.setText(preferences.getString(emailKey, ""))
        binding.edittextPassword.setText(preferences.getString(passwordKey, ""))

    }

    private fun setListeners() {
        binding.buttonRegister.setOnClickListener { register() }
    }

    private fun setFieldsValidations() {
        binding.edittextEmail.doOnTextChanged { text, _, _, _ ->
            if (!isValidEmail(text.toString())) {
                binding.textInputLayoutEmail.error = getString(R.string.invalid_email)
            } else {
                binding.textInputLayoutEmail.error = null
            }
        }
        binding.edittextPassword.doOnTextChanged { text, _, _, _ ->
            if (isValidPassword(text.toString())) {
                binding.textInputLayoutPassword.error =
                    getString(R.string.passwordLenghtErrorText)
            } else {
                binding.textInputLayoutPassword.error = null
            }
        }
    }

    private fun register() {
        if (binding.checkBoxRemember.isChecked) saveDataToSP()
        if (checkData()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(intentEmailKey, binding.edittextEmail.text.toString())
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
    }

    private fun saveDataToSP() {
        preferences.edit().putString(
            passwordKey,
            binding.edittextPassword.text.toString()
        )
            .putString(
                emailKey,
                binding.edittextEmail.text.toString()
            )
            .apply()
    }

    private fun checkData(): Boolean {
        return isValidPassword(binding.edittextEmail.text.toString()) && isValidEmail(binding.edittextEmail.text.toString())
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= passwordLength
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern =
            Patterns.EMAIL_ADDRESS
        return email.matches(emailPattern.toRegex())
    }

    companion object {
        private const val APP_PREFERENCES = "APP_PREFERENCES"
    }
}

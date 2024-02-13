package com.example.myapplication.presentation.ui.auth.fragments.signUp

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.myapplication.R
import com.example.myapplication.data.model.User
import com.example.myapplication.databinding.FragmentSignUpBinding
import com.example.myapplication.presentation.utils.Constants.SUCCESS_CODE
import dagger.hilt.android.AndroidEntryPoint
const val emailKey = "email"
const val passwordKey = "password"
@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private val binding by lazy { FragmentSignUpBinding.inflate(layoutInflater) }
    private val viewModel:SignUpViewModel by  viewModels()

    private val passwordLength = 8

    private val preferences: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("signUpPreferences", AppCompatActivity.MODE_PRIVATE)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        loadData()
        setListeners()
        setObservers()
        setFieldsValidations()
        return binding.root
    }

    private fun setObservers() {
        viewModel._registerResponse.observe(viewLifecycleOwner){
            if(it.code==SUCCESS_CODE) {
                Navigation.findNavController(requireView())
                    .navigate(
                        SignUpFragmentDirections.actionSignInFragment2ToSignInExtendedFragment2(
                            it.data
                        )
                    )
            }
        }
        viewModel._errorMessage.observe(viewLifecycleOwner){
            Toast.makeText(this.context,
                getString(R.string.email_is_already_registered),Toast.LENGTH_SHORT).show()
        }
    }


    private fun setListeners() {
        binding.buttonRegister.setOnClickListener {
            register()
        }
        binding.buttonSignIn.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(SignUpFragmentDirections.actionSignInFragment2ToLoginFragment())
        }
    }

    private fun register() {
        if (binding.checkBoxRemember.isChecked) saveDataToSP()
        if (checkData()) {
            val requestBody = User(
                binding.edittextEmail.text.toString(),
                binding.edittextPassword.text.toString()
            )
            viewModel.register(requestBody)
        }
    }

        private fun loadData() {
        binding.edittextEmail.setText(preferences.getString(emailKey, ""))
        binding.edittextPassword.setText(preferences.getString(passwordKey, ""))

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
            if (!isValidPassword(text.toString())) {
                binding.textInputLayoutPassword.error =
                    getString(R.string.passwordLenghtErrorText)
            } else {
                binding.textInputLayoutPassword.error = null
            }
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

}
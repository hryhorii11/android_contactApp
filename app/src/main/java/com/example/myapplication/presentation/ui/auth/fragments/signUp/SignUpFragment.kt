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
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSignUpBinding
import com.example.myapplication.domain.model.User
import com.example.myapplication.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<SignUpViewModel>(R.layout.fragment_sign_up) {
    private val binding by lazy { FragmentSignUpBinding.inflate(layoutInflater) }
    override val viewModel: SignUpViewModel by viewModels()
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
        viewModel.user.collectUIState(
            onError = {
                Toast.makeText(
                    this.context,
                    getString(R.string.email_is_already_registered), Toast.LENGTH_SHORT
                ).show()
            },
            onSuccess = {
                viewModel.setAuthorizeState()
                Navigation.findNavController(requireView())
                    .navigate(
                        SignUpFragmentDirections.actionSignInFragment2ToSignInExtendedFragment2(
                        )
                    )
            }
        )
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
        binding.edittextEmail.setText(preferences.getString(EMAIL_KEY, ""))
        binding.edittextPassword.setText(preferences.getString(PASSWORD_KEY, ""))

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
            PASSWORD_KEY,
            binding.edittextPassword.text.toString()
        )
            .putString(
                EMAIL_KEY,
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
        const val EMAIL_KEY = "email"
        const val PASSWORD_KEY = "password"
    }
}
package com.example.myapplication.presentation.ui.auth.fragments.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.myapplication.data.model.LoginData
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.presentation.ui.main.activity.MainActivity
import com.example.myapplication.presentation.utils.Constants.SUCCESS_CODE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    val binding: FragmentLoginBinding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private val preferences: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("loginPreferences", AppCompatActivity.MODE_PRIVATE)
    }
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        loadLoginData()
        setListeners()
        setObservers()

        return binding.root
    }

    private fun setObservers() {
        viewModel._loginResponse.observe(viewLifecycleOwner) {
            if (it.code == SUCCESS_CODE) {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.putExtra("token", it.data.accessToken)
                intent.putExtra("user", it.data.user)
                startActivity(intent)
                requireActivity().finish()
            }
        }
        viewModel._errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(this.context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadLoginData() {
        with(binding) {
            edittextEmail.setText(preferences.getString("email", ""))
            edittextPassword.setText(preferences.getString("password", ""))
        }


    }

    private fun setListeners() {
        with(binding) {
            buttonLogin.setOnClickListener { loginUser() }
            buttonSignIn.setOnClickListener {
                Navigation.findNavController(requireView())
                    .navigate(LoginFragmentDirections.actionLoginFragmentToSignInFragment2())
            }
        }
    }

    private fun loginUser() {
        val loginData = LoginData(
            binding.edittextEmail.text.toString(),
            binding.edittextPassword.text.toString()
        )
        rememberLoginData(loginData)
        viewModel.login(loginData)

    }

    private fun rememberLoginData(loginData: LoginData) {
        if (binding.checkBoxRemember.isChecked) {
            val editor = preferences.edit()
            editor.putString("email", loginData.email)
            editor.putString("password", loginData.password)
            editor.apply()
        }
    }


}
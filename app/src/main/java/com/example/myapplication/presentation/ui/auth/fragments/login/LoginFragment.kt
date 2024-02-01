package com.example.myapplication.presentation.ui.auth.fragments.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.myapplication.R
import com.example.myapplication.domain.model.LoginData
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.presentation.ui.BaseFragment
import com.example.myapplication.presentation.ui.main.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel>(R.layout.fragment_login) {
    val binding: FragmentLoginBinding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private val preferences: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("loginPreferences", AppCompatActivity.MODE_PRIVATE)
    }
    override val viewModel: LoginViewModel by viewModels()
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
        viewModel.user.collectUIState(
            onSuccess = {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            },
            onError = {
                Toast.makeText(this.context, it, Toast.LENGTH_SHORT).show()
            }
        )
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
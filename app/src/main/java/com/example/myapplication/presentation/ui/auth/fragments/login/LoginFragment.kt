package com.example.myapplication.presentation.ui.auth.fragments.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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


    override val viewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        viewModel.getLoginData()
        viewModel.getAuthorizeState()
        setListeners()
        setObservers()

        return binding.root
    }

    private fun autologin() {

        val intent = Intent(requireActivity(), MainActivity::class.java)
        viewModel.refreshToken()
        startActivity(intent)
        requireActivity().finish()
    }

    private fun setObservers() {
        viewModel.user.collectUIState(
            onSuccess = {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                viewModel.saveUserData(it.data)
                viewModel.setAuthorizedState(true)
                startActivity(intent)
                requireActivity().finish()

            },
            onError = {
                Toast.makeText(this.context, getString(R.string.network_error), Toast.LENGTH_SHORT)
                    .show()
            }
        )
        viewModel.tokens.collectUIState(
            onSuccess = {
                viewModel.saveNewToken(it.data)
            },
            onError = {}

        )
        viewModel.loginData.observe(viewLifecycleOwner) {
            loadLoginData(it)
        }
        viewModel.isAuthorized.observe(viewLifecycleOwner) {
            if (it) autologin()
        }
    }

    private fun loadLoginData(data: LoginData) {
        with(binding) {
            edittextEmail.setText(data.email)
            edittextPassword.setText(data.password)
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
        viewModel.rememberLoginData(loginData, binding.checkBoxRemember.isChecked)
        viewModel.login(loginData)

    }

}
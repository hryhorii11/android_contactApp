package com.example.myapplication.presentation.ui.auth.fragments.signUpExtended

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.domain.model.EditBody
import com.example.myapplication.databinding.FragmentSignUpExtendedBinding
import com.example.myapplication.presentation.ui.BaseFragment
import com.example.myapplication.presentation.ui.main.activity.MainActivity
import com.example.myapplication.presentation.utils.ext.setPhoto
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpExtendedFragment :
    BaseFragment<SignUpExtendedViewModel>(R.layout.fragment_sign_up_extended) {
    private var imageUri: Uri? = null
    override val viewModel: SignUpExtendedViewModel by viewModels()
    private lateinit var photoActivityResult: ActivityResultLauncher<Intent>
    val binding by lazy { FragmentSignUpExtendedBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        setActivityResultLauncher()
        setListeners()
        setObservers()
        return binding.root
    }

    private fun setObservers() {

        viewModel.user.collectUIState(
            onError = {
                Toast.makeText(this.context, it, Toast.LENGTH_LONG).show()

            },
            onSuccess = {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        )

    }

    private fun setListeners() {
        with(binding) {
            buttonCancelSignUp.setOnClickListener {
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack()
            }
            buttonAddPhoto.setOnClickListener { addPhoto() }
            buttonForward.setOnClickListener { addUserInformation() }
        }
    }

    @SuppressLint("Recycle")
    private fun setActivityResultLauncher() {
        photoActivityResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                imageUri = it.data?.data
                binding.ImageViewUserRegistration.setPhoto(imageUri.toString())
            }
        }
    }

    private fun addPhoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        photoActivityResult.launch(intent)
    }


    private fun addUserInformation() {
        val requestBody = EditBody(
            binding.edittextUserName.text.toString(),
            binding.edittextPhone.text.toString()
        )
        viewModel.editUser(requestBody )

    }


}
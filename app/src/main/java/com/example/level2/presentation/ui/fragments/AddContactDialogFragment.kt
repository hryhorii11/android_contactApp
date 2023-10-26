package com.example.level2.presentation.ui.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.example.level2.R
import com.example.level2.presentation.utils.ext.setPhoto
import com.example.level2.data.model.Contact
import com.example.level2.databinding.ActivityAddContactBinding
import com.example.level2.presentation.utils.Validation.isCareerValid
import com.example.level2.presentation.utils.Validation.isEmailValid
import com.example.level2.presentation.utils.Validation.isPhoneValid
import com.example.level2.presentation.utils.Validation.isUsernameValid
import com.example.level2.presentation.utils.Validation.isValidDate


class AddContactDialogFragment(private val viewModel: ContactListViewModel) : DialogFragment() {

    private val binding: ActivityAddContactBinding by lazy {
        ActivityAddContactBinding.inflate(layoutInflater)
    }
    private var imageUri: Uri? = null
    private lateinit var photoActivityResult: ActivityResultLauncher<Intent>
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        setActivityResultLauncher()
        setListeners()
        setValidations()
        return builder.create()
    }

    private fun setActivityResultLauncher() {
        photoActivityResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {

            if (it.resultCode == Activity.RESULT_OK) {
                imageUri = it.data?.data
                binding.ImageViewAddContactPhoto.setPhoto(imageUri.toString())
            }
        }
    }

    private fun setListeners() {
        with(binding) {
            backButton.setOnClickListener { close() }
            buttonSave.setOnClickListener { addUser() }
            buttonAddPhoto.setOnClickListener { addPhoto() }
        }
    }

    private fun addPhoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        photoActivityResult.launch(intent)
    }

    private fun addUser() {
        if (isUsernameValid(binding.edittextUsername.text.toString()) &&
            isCareerValid(binding.edittextCareer.text.toString()) &&
            isPhoneValid(binding.edittextPhone.text.toString()) &&
            isEmailValid(binding.edittextEmail.text.toString()) &&
            isCareerValid(binding.edittextAdress.text.toString()) &&
            isValidDate(binding.edittextDateOfBirth.text.toString())
        ) {
            val user = Contact(
                binding.edittextUsername.text.toString(),
                binding.edittextCareer.text.toString(),
                binding.edittextAdress.text.toString(),
                imageUri.toString()
            )
            viewModel.addContact(user)
            dismiss()
            close()
        } else {
            Toast.makeText(
                context,
                getString(R.string.fill_in_all_fields_correctly), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun close() {

    }

    private fun setValidations() {
        with(binding) {
            edittextUsername.doOnTextChanged { text, _, _, _ ->
                if (!isUsernameValid(text.toString())) binding.textInputLayoutUsername.error =
                    getString(R.string.invalid_username_there_must_be_a_first_and_last_name) else binding.textInputLayoutUsername.error =
                    null
            }
            binding.edittextCareer.doOnTextChanged { text, _, _, _ ->
                if (!isCareerValid(text.toString()))
                    binding.textInputLayoutCareer.error = getString(R.string.cannot_be_blank)
                else
                    binding.textInputLayoutCareer.error = null
            }
            binding.edittextEmail.doOnTextChanged { text, _, _, _ ->
                if (!isEmailValid(text.toString()))
                    binding.textInputLayoutEmail.error = getString(R.string.invalid_email)
                else
                    binding.textInputLayoutEmail.error = null
            }
            binding.edittextPhone.doOnTextChanged { text, _, _, _ ->
                if (!isPhoneValid(text.toString()))
                    binding.textInputLayoutPhone.error =
                        getString(R.string.must_be_10_numbers)
                else
                    binding.textInputLayoutPhone.error = null
            }
            binding.edittextAdress.doOnTextChanged { text, _, _, _ ->
                if (!isCareerValid(text.toString()))
                    binding.textInputLayoutAdress.error = getString(R.string.cannot_be_blank)
                else
                    binding.textInputLayoutAdress.error = null
            }
            binding.edittextDateOfBirth.doOnTextChanged { text, _, _, _ ->
                if (!isValidDate(text.toString()))
                    binding.textInputLayoutDateOfBirth.error =
                        getString(R.string.incorrect_date)
                else
                    binding.textInputLayoutDateOfBirth.error = null
            }
        }

    }
}
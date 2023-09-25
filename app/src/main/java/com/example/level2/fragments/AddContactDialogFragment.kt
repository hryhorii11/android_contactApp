package com.example.level2.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.example.level2.R
import com.example.level2.screens.ContactListViewModel
import com.example.level2.databinding.ActivityAddContactBinding
import com.example.level2.ext.setPhoto
import com.example.level2.model.Contact
import java.text.SimpleDateFormat
import java.util.Locale

const val usernameValidationPattern = "[a-zA-zа-яА-Я]{2,} +[a-zA-zА-Яа-я]{2,}"
const val phoneNumbers = 10

class AddContactDialogFragment(private val viewModel: ContactListViewModel) : DialogFragment() {

    private lateinit var addContactBinding: ActivityAddContactBinding
    private var imageUri: Uri? = null
    private lateinit var photoActivityResult: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addContactBinding = ActivityAddContactBinding.inflate(inflater, container, false)
        setValidations()
        setListeners()
        setActivityResultLauncher()

        return addContactBinding.root
    }

    private fun setActivityResultLauncher() {
        photoActivityResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {

            if (it.resultCode == Activity.RESULT_OK) {
                imageUri =it.data?.data
                addContactBinding.ImageViewAddContactPhoto.setPhoto(imageUri.toString())
            }
        }
    }

    private fun setListeners() {
        addContactBinding.backButton.setOnClickListener { close() }
        addContactBinding.buttonSave.setOnClickListener { addUser() }
        addContactBinding.buttonAddPhoto.setOnClickListener { addPhoto() }
    }

    private fun addPhoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        photoActivityResult.launch(intent)
    }

    private fun addUser() {
        if (isUsernameValid(addContactBinding.edittextUsername.text.toString()) &&
            isCareerValid(addContactBinding.edittextCareer.text.toString()) &&
            isPhoneValid(addContactBinding.edittextPhone.text.toString()) &&
            isEmailValid(addContactBinding.edittextEmail.text.toString()) &&
            isCareerValid(addContactBinding.edittextAdress.text.toString())&&
            isValidDate(addContactBinding.edittextDateOfBirth.text.toString())
        ) {
            val user = Contact(
                0,
                addContactBinding.edittextUsername.text.toString(),
                addContactBinding.edittextCareer.text.toString(),
                addContactBinding.edittextAdress.text.toString(),
                imageUri.toString()
            )
            viewModel.addContact(user)
            close()
        }
        else
        {
            Toast.makeText(context,
                getString(R.string.fill_in_all_fields_correctly), Toast.LENGTH_SHORT).show()
        }
    }

    private fun close() {
        dialog?.dismiss()
    }

    private fun setValidations() {
        addContactBinding.edittextUsername.doOnTextChanged { text, _, _, _ ->
            if (!isUsernameValid(text.toString()))
                addContactBinding.textInputLayoutUsername.error =
                    getString(R.string.invalid_username_there_must_be_a_first_and_last_name)
            else
                addContactBinding.textInputLayoutUsername.error = null
        }
        addContactBinding.edittextCareer.doOnTextChanged { text, _, _, _ ->
            if (!isCareerValid(text.toString()))
                addContactBinding.textInputLayoutCareer.error = getString(R.string.cannot_be_blank)
            else
                addContactBinding.textInputLayoutCareer.error = null
        }
        addContactBinding.edittextEmail.doOnTextChanged { text, _, _, _ ->
            if (!isEmailValid(text.toString()))
                addContactBinding.textInputLayoutEmail.error = getString(R.string.invalid_email)
            else
                addContactBinding.textInputLayoutEmail.error = null
        }
        addContactBinding.edittextPhone.doOnTextChanged { text, _, _, _ ->
            if (!isPhoneValid(text.toString()))
                addContactBinding.textInputLayoutPhone.error =
                    getString(R.string.must_be_10_numbers)
            else
                addContactBinding.textInputLayoutPhone.error = null
        }
        addContactBinding.edittextAdress.doOnTextChanged { text, _, _, _ ->
            if (!isCareerValid(text.toString()))
                addContactBinding.textInputLayoutAdress.error = getString(R.string.cannot_be_blank)
            else
                addContactBinding.textInputLayoutAdress.error = null
        }
        addContactBinding.edittextDateOfBirth.doOnTextChanged { text, _, _, _ ->
            if (!isValidDate(text.toString()))
                addContactBinding.textInputLayoutDateOfBirth.error =
                    getString(R.string.incorrect_date)
            else
                addContactBinding.textInputLayoutDateOfBirth.error = null
        }
    }

    private fun isUsernameValid(userName: String): Boolean {
        return userName.matches(usernameValidationPattern.toRegex())
    }

    private fun isCareerValid(career: String): Boolean {
        return career.isNotBlank()
    }

    private fun isEmailValid(email: String): Boolean {
        return email.matches(Patterns.EMAIL_ADDRESS.toRegex())
    }

    private fun isPhoneValid(phone: String): Boolean {
        return phone.length == phoneNumbers
    }

    private fun isValidDate(inputDate: String): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false

        return try {
            dateFormat.parse(inputDate)
            true
        } catch (e: Exception) {
            false
        }
    }
}
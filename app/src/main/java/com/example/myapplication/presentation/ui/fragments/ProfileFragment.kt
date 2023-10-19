package com.example.myapplication.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.presentation.utils.ext.setPhoto
import com.example.myapplication.R
import com.example.myapplication.databinding.ProfileFragmentBinding
import com.example.myapplication.presentation.ui.activities.emailKey

class ProfileFragment:Fragment() {
    private val emailSeparator="@"
    private val nameSeparatorsPattern= "[.,_]"
    private lateinit var binding:ProfileFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
          super.onCreate(savedInstanceState)
        binding=ProfileFragmentBinding.inflate(layoutInflater)
        binding.textViewName.text=parseEmail(activity?.intent?.getStringExtra(emailKey))
        val viewPager=activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.imageViewUserPhoto.setPhoto("baseline_person_2_24.xml")
        binding.buttonViewContacts.setOnClickListener{
         viewPager?.currentItem=1
        }
        return binding.root
    }
    private fun parseEmail(email: String?): String? {
        val name = email?.substring(
            0,
            email.indexOf(emailSeparator)
        )
        return name?.replace(
            nameSeparatorsPattern.toRegex(),
            " "
        )
    }
}
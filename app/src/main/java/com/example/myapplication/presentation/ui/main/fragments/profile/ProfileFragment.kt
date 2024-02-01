package com.example.myapplication.presentation.ui.main.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.presentation.utils.ext.setPhoto
import com.example.myapplication.R
import com.example.myapplication.domain.model.UserFromLogin
import com.example.myapplication.databinding.ProfileFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val binding: ProfileFragmentBinding by lazy {
        ProfileFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        setListeners()
        setUserData()
        return binding.root
    }

    private fun setListeners() {
        binding.buttonViewContacts.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 1
        }
    }

    private fun setUserData() {
        binding.imageViewUserPhoto.setPhoto()
        val u: UserFromLogin? =
            requireActivity().intent.getParcelableExtra("user")
        with(binding) {
            textViewName.text = u?.name
            textViewCareer.text = u?.career
        }
    }


}
package com.example.myapplication.presentation.ui.main.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.presentation.utils.ext.setPhoto
import com.example.myapplication.R
import com.example.myapplication.data.model.UserFromLogin
import com.example.myapplication.databinding.ProfileFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: ProfileFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = ProfileFragmentBinding.inflate(layoutInflater)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.imageViewUserPhoto.setPhoto("baseline_person_2_24.xml")
        binding.buttonViewContacts.setOnClickListener {
            viewPager?.currentItem = 1
        }
        setUserData()

        return binding.root
    }

    private fun setUserData() {
        val u: UserFromLogin? =
            requireActivity().intent.getParcelableExtra("user")
        with(binding) {
            textViewName.text = u?.name
            textViewCareer.text = u?.career
        }
    }


}
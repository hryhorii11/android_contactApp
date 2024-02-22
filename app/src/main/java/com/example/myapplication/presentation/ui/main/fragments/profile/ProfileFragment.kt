package com.example.myapplication.presentation.ui.main.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.presentation.utils.ext.setPhoto
import com.example.myapplication.R
import com.example.myapplication.data.users.repository.UsersRepositoryImpl
import com.example.myapplication.databinding.ProfileFragmentBinding
import com.example.myapplication.presentation.ui.auth.activity.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val binding: ProfileFragmentBinding by lazy {
        ProfileFragmentBinding.inflate(layoutInflater)
    }

    private val viewModel: ProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        setListeners()
        viewModel.setCurrentUserData()
        setUserData()
        return binding.root
    }

    private fun setListeners() {
        with(binding) {
            buttonViewContacts.setOnClickListener {
                val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
                viewPager?.currentItem = 1
            }
            buttonLogOut.setOnClickListener {
                viewModel.logout()
                val intent = Intent(requireActivity(), AuthActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    private fun setUserData() {
        with(binding) {
            imageViewUserPhoto.setPhoto()
            textViewName.text = UsersRepositoryImpl.currentUser.name
            textViewCareer.text = UsersRepositoryImpl.currentUser.career

        }
    }


}
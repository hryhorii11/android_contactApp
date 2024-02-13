package com.example.myapplication.presentation.ui.main.fragments.viewPager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentViewPagerBinding
import com.example.myapplication.presentation.ui.main.fragments.contactList.ContactListFragment
import com.example.myapplication.presentation.ui.main.fragments.profile.ProfileFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentViewPagerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        findNavController().clearBackStack(R.id.viewPager)
        binding = FragmentViewPagerBinding.inflate(layoutInflater)

        val fragmentList = arrayListOf(
            ProfileFragment(),
            ContactListFragment()
        )
        val adapter =
            ViewPagerAdapter(fragmentList, childFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ ->
        }.attach()
        setTabText()
        return binding.root
    }


    private fun setTabText() {
        binding.tabLayout.getTabAt(0)?.text=getString(R.string.profile)
        binding.tabLayout.getTabAt(1)?.text=getString(R.string.contacts)
    }

}
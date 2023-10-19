package com.example.myapplication.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentViewPagerBinding
import com.example.myapplication.presentation.ui.fragments.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentViewPagerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentViewPagerBinding.inflate(layoutInflater)


        val fragmentList = arrayListOf(
            ProfileFragment(),
            ContactListFragment()
        )

        val adapter =
            ViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)
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
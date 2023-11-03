package com.example.level2.presentation.ui.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.level2.databinding.FragmentDetailBinding
import com.example.level2.presentation.utils.ext.setPhoto

class DetailViewFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val args: DetailViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)


        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.slide_left)

        binding = FragmentDetailBinding.inflate(layoutInflater)
        setData()
        setListeners()

        return binding.root
    }

    private fun setListeners() {
        binding.buttonBack.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setData() {
        with(binding)
        {
            textViewName.text=args.contactName
            textViewCareer.text=args.career
            textViewAddres.text=args.addres
            imageViewContactPhoto.setPhoto(args.photo)
        }
    }
}

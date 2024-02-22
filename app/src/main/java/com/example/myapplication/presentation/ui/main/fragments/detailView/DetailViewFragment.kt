package com.example.myapplication.presentation.ui.main.fragments.detailView

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.presentation.utils.ext.setPhoto
import com.example.myapplication.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        initializeContactPersonalData()
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.buttonBack.setOnClickListener { closeScreen() }
    }

    private fun initializeContactPersonalData() {
        with(binding)
        {
            textViewName.text = args.contact.name
            textViewCareer.text = args.contact.career
            textViewAddres.text = args.contact.address
            if (args.contact.photo != null)
                imageViewContactPhoto.setPhoto(args.contact.photo!!)
            else imageViewContactPhoto.setPhoto()
        }
    }

    private fun closeScreen() {
        findNavController().popBackStack()
    }

}

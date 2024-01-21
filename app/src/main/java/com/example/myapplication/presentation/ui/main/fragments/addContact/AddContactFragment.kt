package com.example.myapplication.presentation.ui.main.fragments.addContact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.model.Contact
import com.example.myapplication.data.model.UserFromLogin
import com.example.myapplication.databinding.FragmentAddContactBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddContactFragment : Fragment() {
    private val binding: FragmentAddContactBinding by lazy {
        FragmentAddContactBinding.inflate(
            layoutInflater
        )
    }
    private lateinit var adapter: AddContactAdapter
    private val viewModel: AddContactViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setListeners()
        val user: UserFromLogin? = requireActivity().intent.getParcelableExtra("user")
        setRecyclerAdapter(user)

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter


        viewModel.contacts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        }
        requireActivity().intent.getStringExtra("token")?.let {
            if (user != null) {
                viewModel.setContacts(it, user.id)
            }
        }
        return binding.root
    }

    private fun setRecyclerAdapter(user: UserFromLogin?) {
        adapter = AddContactAdapter(object : AddClickListener {
            override fun addContact(id: Int) {
                requireActivity().intent.getStringExtra("token")
                    ?.let {
                        if (user != null) {
                            viewModel.addContact(it, id, user.id)
                        }
                    }
            }

            override fun onItemClick(contact: Contact) {
                findNavController().navigate(
                    AddContactFragmentDirections.actionAddContactFragmentToDetailViewFragment(
                        contact
                    )
                )
            }
        })
    }

    private fun setListeners() {
        with(binding) {
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
            buttonSearch.setOnClickListener { toSearchMode() }
            buttonCloseSearch.setOnClickListener { closeSearchMode() }
            searchViewContact.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        viewModel.search(newText)
                    }
                    return true
                }
            })
        }
    }

    private fun closeSearchMode() {
        with(binding)
        {
            buttonSearch.visibility = View.VISIBLE
            buttonBack.visibility = View.VISIBLE
            textViewUsers.visibility = View.VISIBLE
            searchViewContact.visibility = View.GONE
            buttonCloseSearch.visibility = View.GONE
        }
    }

    private fun toSearchMode() {
        with(binding)
        {
            buttonSearch.visibility = View.GONE
            buttonBack.visibility = View.GONE
            textViewUsers.visibility = View.GONE
            searchViewContact.visibility = View.VISIBLE
            buttonCloseSearch.visibility = View.VISIBLE
        }
    }

}